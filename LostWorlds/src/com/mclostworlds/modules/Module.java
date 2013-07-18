package com.mclostworlds.modules;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mclostworlds.LostWorlds;

public abstract interface Module {

	public abstract void onEnable(LostWorlds plugin, File file);

	public abstract void onDisable();

	public abstract boolean callCommand(CommandSender sender, Command cmd,
			String label, String[] args);

}
