package ca.xshade.bukkit.cellwar;

import java.io.File;
import java.io.IOException;

import org.bukkit.DyeColor;
import org.bukkit.util.config.Configuration;

import ca.xshade.util.JavaUtil;

public class CellWarConfig {
	public static Configuration config;
	public static final DyeColor[] woolColor = new DyeColor[] {
		DyeColor.LIME,
		DyeColor.GREEN,
		DyeColor.BLUE,
		DyeColor.CYAN,
		DyeColor.LIGHT_BLUE,
		DyeColor.SILVER,
		DyeColor.WHITE,
		DyeColor.PINK,
		DyeColor.ORANGE,
		DyeColor.RED
	};
	
	public static boolean loadConfig(File file) {
		if (!(file.exists() && file.isFile())) {
			try {
				JavaUtil.saveFileFromJar("/config.yml", file);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		config = new Configuration(file);
		config.load();
		
		return true;
	}
	
	public static String getLangString(String root){
        String data = config.getString(root.toLowerCase());
        if (data == null) {
            System.out.println("[CellWar] Could not read config value: " + root.toLowerCase());
            return "";
        }
        return parseSingleLineString(data);
    }
	
	public static String parseSingleLineString(String str) {
        return str.replaceAll("&", "\u00A7");
	}
	
	public static int getCellSize() {
		return config.getInt("cell_size", 16);
	}
	
	public static DyeColor[] getWoolColors() {
		return woolColor;
	}
	
	public static long getFlagWaitingTime() {
		return 1000 * config.getInt("flag_waiting_time", 60);
	}
	
	public static long getTimeBetweenFlagColorChange() {
		return getFlagWaitingTime() / getWoolColors().length;
	}
	
	public static boolean isDrawingBeacon() {
		return config.getBoolean("draw_beacon", true);
	}

	public static int getBeaconSize() {
		return config.getInt("beacon_size", 20);
	}
	
	public static int getMaxActiveFlagsPerPerson() {
		return config.getInt("max_active_flags_per_player", 1);
	}
}
