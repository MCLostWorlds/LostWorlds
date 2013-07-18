package com.mclostworlds.modules.mine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SerializableLocation {

	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;
	private String world;

	public SerializableLocation(Location l) {
		x = l.getX();
		y = l.getY();
		z = l.getZ();
		pitch = l.getPitch();
		yaw = l.getYaw();
		world = l.getWorld().getName();
	}

	public static Location deserialize(String m) {
		String loc = m.replace("_", ".");
		String[] things = loc.split("=");
		double x = Double.valueOf(things[0]);
		double y = Double.valueOf(things[1]);
		double z = Double.valueOf(things[2]);
		float pitch = Float.valueOf(things[3]);
		float yaw = Float.valueOf(things[4]);
		World w = Bukkit.getWorld(things[5]);

		Location l = new Location(w, x, y, z, pitch, yaw);
		return l;
	}

	public String serialize() {
		String serialize = x + "=" + y + "=" + z + "=" + pitch + "=" + yaw
				+ "=" + world;
		return serialize.replace(".", "_");
	}

}
