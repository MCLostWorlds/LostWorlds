package com.mclostworlds.modules.mine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.mclostworlds.LostWorlds;
import com.mclostworlds.modules.Module;
import com.mclostworlds.modules.ModuleLoader;

public class Mine implements Module, Listener {

	private LostWorlds plugin;
	public static Config oreConfig;
	public static List<String> blocks;

	@Override
	public void onEnable(LostWorlds plugin, File file) {
		this.plugin = plugin;
		oreConfig = new Config(new File(file, "Ores.yml"));
		oreConfig.saveDefaultConfig();
		blocks = new ArrayList<String>();
		ModuleLoader.commandRegistrar.registerCommand("mine", this);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.logger.info("[LostWorlds] [Mine] Mine Module Has Been Enabled!");

		regen();
	}

	@Override
	public void onDisable() {
		plugin.logger
				.info("[LostWorlds] [Mine] Mine Module Has Been Disabled!");
		ModuleLoader.commandRegistrar.unregisterCommand("mine", this);
		HandlerList.unregisterAll(this);
		plugin = null;
		oreConfig = null;
		blocks = null;
	}

	@Override
	public boolean callCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("mine") && sender instanceof Player) {

		}
		return false;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		if (blocks.contains((new SerializableLocation(event.getBlock()
				.getLocation()).serialize()))) {
			event.setCancelled(true);
		} else {
			event.setCancelled(true);
			event.getPlayer().giveExp(event.getExpToDrop());
			if (event.getExpToDrop() > 0)
				event.getBlock()
						.getWorld()
						.playSound(event.getPlayer().getLocation(),
								Sound.ORB_PICKUP, 1, 1);
			RegenBlock block = new RegenBlock(30, event.getBlock(), event
					.getBlock().getDrops());
			event.getBlock().setType(Material.COBBLESTONE);
			block.regen();
		}
	}

	private void regen() {
		Config config = new Config(new File(
				LostWorlds.instance.getDataFolder(), "Mine" + File.separator
						+ "Ores.yml"));
		if (config.getConfig().getConfigurationSection("Blocks") == null)
			return;
		for (String s : config.getConfig().getConfigurationSection("Blocks")
				.getKeys(false)) {
			SerializableLocation.deserialize(s).getBlock()
					.setTypeId(config.getConfig().getInt("Blocks." + s));
			config.getConfig().getConfigurationSection("Blocks").set(s, null);
			config.saveConfig();
		}
	}

}
