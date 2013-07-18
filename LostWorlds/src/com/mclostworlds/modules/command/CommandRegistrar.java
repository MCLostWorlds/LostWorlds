package com.mclostworlds.modules.command;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mclostworlds.modules.Module;

public class CommandRegistrar {

	private Map<String, Module> commandMap;

	public CommandRegistrar() {
		commandMap = new HashMap<String, Module>();
	}

	public void registerCommand(String commandName, Module module) {
		commandMap.put(commandName, module);
	}

	public boolean unregisterCommand(String commandName, Module module) {
		if (((Module) commandMap.get(commandName)).equals(module)) {
			commandMap.remove(commandName);
			return true;
		}
		return false;
	}

	public boolean callCommand(CommandSender sender, Command command,
			String label, String[] args) {

		Module module = (Module) commandMap.get(command.getName());

		if (module != null) {
			return module.callCommand(sender, command, label, args);
		}
		sender.sendMessage("Unknown command. Type \"help\" for help.");
		Logger.getLogger("Minecraft").severe(
				"[LostWorlds] Command Registrar did not find corrosponding module for command "
						+ command.getName());
		return false;
	}

}
