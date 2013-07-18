package com.mclostworlds.modules.mine;

import net.minecraft.server.v1_6_R2.Packet61WorldEvent;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CobbleReplace implements Runnable {

	private final String sLocation;
	private final Location location;
	private final int id;

	public CobbleReplace(String sLocation, Location location, int id) {
		this.sLocation = sLocation;
		this.location = location;
		this.id = id;
	}

	@Override
	public void run() {
		Packet61WorldEvent packet = new Packet61WorldEvent(2001,
				location.getBlockX(), location.getBlockY(),
				location.getBlockZ(), id, false);
		for (Player p : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
		Mine.blocks.remove(sLocation);
		location.getBlock().setTypeId(id);
		location.getWorld().playEffect(location, Effect.MOBSPAWNER_FLAMES, 1);
		Mine.oreConfig.getConfig().set("Blocks." + sLocation, null);
		Mine.oreConfig.saveConfig();
	}

}
