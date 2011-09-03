package ca.xshade.bukkit.cellwar;

import org.bukkit.DyeColor;

public class CellWarConfig {
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
	
	public static int getCellSize() {
		return 16;
	}
	
	public static DyeColor[] getWoolColors() {
		return woolColor;
	}
	
	public static long getFlagWaitingTime() {
		return 1000 * 10;
	}
	
	public static long getTimeBetweenFlagColorChange() {
		return getFlagWaitingTime() / getWoolColors().length;
	}
	
	public static boolean isDrawingBeacon() {
		return true;
	}

	public static int getBeaconSize() {
		return 20;
	}
}
