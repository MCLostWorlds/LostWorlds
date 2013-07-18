package com.mclostworlds;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.mclostworlds.modules.ModuleLoader;

public class LostWorlds extends JavaPlugin {

	public static LostWorlds instance;
	public Logger logger;

	@Override
	public void onEnable() {
		instance = this;
		logger = Logger.getLogger("Minecraft");
		ModuleLoader.enable(this);
	}

	@Override
	public void onDisable() {
		ModuleLoader.disable();
		logger = null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		ModuleLoader.commandRegistrar.callCommand(sender, cmd, label, args);
		return false;
	}

}
