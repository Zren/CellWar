package ca.xshade.bukkit.cellwar;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ca.xshade.bukkit.cellwar.event.CellDefendedEvent;
import ca.xshade.bukkit.cellwar.event.CellWonEvent;
import ca.xshade.bukkit.cellwar.listener.CellWarBlockListener;
import ca.xshade.bukkit.cellwar.listener.CellWarCustomListener;

public class CellWar extends JavaPlugin {
	private static Map<Cell, CellUnderAttack> cellsUnderAttack;
	
	private static CellWar instance;
	private static CellWarBlockListener blockListener;
	private static CellWarCustomListener customListener;
	
	
	public void onEnable() {
		// Initialize
		instance = this;
		cellsUnderAttack = new HashMap<Cell, CellUnderAttack>();
		
		blockListener = new CellWarBlockListener(this);
		customListener = new CellWarCustomListener(this);
		
		// Register Events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.CUSTOM_EVENT, customListener, Priority.Normal, this);
		
		// Plugin Ready
		System.out.println("[CellWar] Enabled - v" + getDescription().getVersion());
	}
	
	public void onDisable() {
		for (CellUnderAttack cell : cellsUnderAttack.values()) {
			
			cell.destroyFlag();
		}
		
		System.out.println("[CellWar] Disabled");
	}
	
	public static void registerAttack(CellUnderAttack cell) throws Exception {
		CellUnderAttack currentData = cellsUnderAttack.get(cell); 
		if (currentData != null) {
			throw new Exception(String.format("This cell is already under attack by %s.", currentData.getNameOfFlagOwner()));
		} else {
			cellsUnderAttack.put(cell, cell);
			cell.begin();
		}
		
	}

	public static boolean isUnderAttack(Cell cell) {
		return cellsUnderAttack.containsKey(cell);
	}

	public static CellUnderAttack getAttackData(Cell cell) {
		return cellsUnderAttack.get(cell);
	}

	public static void attackWon(CellUnderAttack cell) {
		CellWonEvent cellWonEvent = new CellWonEvent(cell);
		instance.getServer().getPluginManager().callEvent(cellWonEvent);
		cellsUnderAttack.remove(cell);
	}
	
	public static void attackDefended(Player player, CellUnderAttack cell) {
		CellDefendedEvent cellDefendedEvent = new CellDefendedEvent(player, cell);
		instance.getServer().getPluginManager().callEvent(cellDefendedEvent);
		cellsUnderAttack.remove(cell);
	}
}
