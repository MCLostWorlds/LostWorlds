package com.mclostworlds.modules.mine;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mclostworlds.LostWorlds;

public class Config {

	private File file;
	private FileConfiguration config;

	public Config(File file) {
		this.file = file;
		reloadConfig();
	}

	public FileConfiguration getConfig() {
		if (config == null) {
			this.reloadConfig();
		}
		return config;
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
	}

	public void saveConfig() {
		if (config == null || file == null) {
			return;
		}
		try {
			getConfig().save(file);
		} catch (IOException ex) {
			LostWorlds.instance.getLogger().log(Level.SEVERE,
					"Could not save config to " + file, ex);
		}
	}

	public void saveDefaultConfig() {
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

}
