package com.mclostworlds.modules;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mclostworlds.LostWorlds;

public class Attributes implements Module {

	private LostWorlds plugin;

	@Override
	public void onEnable(LostWorlds plugin, File file) {
		this.plugin = plugin;
		plugin.logger
				.info("[LostWorlds] [Attributes] Attributes Module Has Been Enabled!");
		ModuleLoader.commandRegistrar.registerCommand("attribute", this);
	}

	@Override
	public void onDisable() {
		plugin.logger
				.info("[LostWorlds] [Attributes] Attributes Module Has Been Disabled!");
		ModuleLoader.commandRegistrar.unregisterCommand("attribute", this);
		plugin = null;
	}

	@Override
	public boolean callCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		return false;
	}

}
