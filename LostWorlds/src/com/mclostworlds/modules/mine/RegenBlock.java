package com.mclostworlds.modules.mine;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.mclostworlds.LostWorlds;

public class RegenBlock {

	private int delay;
	private int id;
	private Location location;
	private String sLocation;
	private Collection<ItemStack> drops;

	public RegenBlock(int delay, Block block, Collection<ItemStack> drops) {
		this.delay = delay;
		id = block.getTypeId();
		location = block.getLocation();
		sLocation = new SerializableLocation(location).serialize();
		this.drops = drops;
	}

	public void regen() {
		for (ItemStack i : drops) {
			location.getWorld().dropItemNaturally(location, i);
		}
		Mine.blocks.add(sLocation);
		Mine.oreConfig.getConfig().set("Blocks." + sLocation, id);
		Mine.oreConfig.saveConfig();
		Bukkit.getServer()
				.getScheduler()
				.scheduleSyncDelayedTask(LostWorlds.instance,
						new CobbleReplace(sLocation, location, id), delay * 20);
	}

}
