package com.mclostworlds.modules;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.mclostworlds.LostWorlds;
import com.mclostworlds.modules.command.CommandRegistrar;
import com.mclostworlds.modules.mine.Mine;

public class ModuleLoader implements CommandExecutor {

	public static CommandRegistrar commandRegistrar;
	private static Map<String, Module> modules;
	private static Logger logger;
	private static Permission permissionApi = null;
	private static Economy economyApi = null;
	private static Chat chatApi = null;

	public static void enable(LostWorlds lostWorlds) {
		logger = lostWorlds.logger;

		commandRegistrar = new CommandRegistrar();
		modules = new TreeMap<String, Module>();

		lostWorlds.getDataFolder().mkdirs();

		RegisteredServiceProvider<Permission> permissionProvider = lostWorlds
				.getServer().getServicesManager()
				.getRegistration(Permission.class);
		if (permissionProvider != null) {
			permissionApi = permissionProvider.getProvider();
		} else {
			logger.severe("[LostWorlds] Vault's Permissions is Brokeded!");
		}
		RegisteredServiceProvider<Economy> economyProvider = lostWorlds
				.getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (economyProvider != null) {
			economyApi = economyProvider.getProvider();
		} else {
			logger.severe("[LostWorlds] Vault's Economy is Brokeded!");
		}
		RegisteredServiceProvider<Chat> chatProvider = lostWorlds.getServer()
				.getServicesManager().getRegistration(Chat.class);
		if (chatProvider != null) {
			chatApi = chatProvider.getProvider();
		} else {
			logger.severe("[LostWorlds] Vault's Chat is Brokeded!");
		}

		addModule(new Mine());

		for (Map.Entry<String, Module> entry : modules.entrySet()) {
			String name = moduleName(entry.getKey());
			try {
				entry.getValue().onEnable(lostWorlds,
						new File(lostWorlds.getDataFolder(), name));
			} catch (Throwable t) {
				t.printStackTrace();
				logger.severe("[LostWorlds] Unable to load module! Stack trace follows!");
				Module m = entry.getValue();
				modules.put(m.getClass().getName(), m);
				try {
					entry.getValue().onEnable(lostWorlds,
							new File(lostWorlds.getDataFolder(), name));
				} catch (Throwable t2) {
				}
			}
		}
	}

	public static void disable() {
		commandRegistrar = null;
		permissionApi = null;
		economyApi = null;
		chatApi = null;
		logger = null;

		for (Module m : modules.values()) {
			try {
				m.onDisable();
			} catch (Throwable t) {
			}
		}

		modules = null;
	}

	private static void addModule(Module module) {
		modules.put(module.getClass().getName(), module);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		commandRegistrar.callCommand(sender, cmd, label, args);
		return false;
	}

	public Permission getPermissionApi() {
		return permissionApi;
	}

	public Economy getEconomyApi() {
		return economyApi;
	}

	public Chat getChatApi() {
		return chatApi;
	}

	@SuppressWarnings("unused")
	private void listModules(CommandSender sender, int page) {
		int i = 0;
		int max = page * 5;
		int min = max - 5;
		if (page == 0) {
			min = 0;
			max = modules.size();
		}
		for (Map.Entry<String, Module> entry : modules.entrySet()) {
			i++;
			if (i >= max)
				break;
			if (i >= min)
				sender.sendMessage(new StringBuilder()
						.append(ChatColor.translateAlternateColorCodes('&',
								"&7[&3Module&7] "))
						.append(ChatColor.WHITE)
						.append(moduleName((String) entry.getKey()))
						.append(": ")
						.append(entry.getValue() != null ? new StringBuilder()
								.append(ChatColor.GREEN).append("OK")
								.toString() : new StringBuilder()
								.append(ChatColor.RED).append("Bad").toString())
						.toString());
		}
	}

	private static String moduleName(String fqn) {
		String[] bits = fqn.split("\\.");
		return bits[(bits.length - 1)];
	}

	@SuppressWarnings("unused")
	private String findModule(String input) {
		for (Map.Entry<String, Module> entry : modules.entrySet()) {
			if (moduleName((String) entry.getKey()).equalsIgnoreCase(input)) {
				return (String) entry.getKey();
			}
		}
		return "";
	}

}
