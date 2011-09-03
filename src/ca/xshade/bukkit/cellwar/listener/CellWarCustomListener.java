package ca.xshade.bukkit.cellwar.listener;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

import ca.xshade.bukkit.cellwar.CellUnderAttack;
import ca.xshade.bukkit.cellwar.CellWar;
import ca.xshade.bukkit.cellwar.event.CellAttackEvent;
import ca.xshade.bukkit.cellwar.event.CellDefendedEvent;
import ca.xshade.bukkit.cellwar.event.CellWonEvent;

public class CellWarCustomListener extends CustomEventListener {
	private CellWar plugin;
	
	public CellWarCustomListener(CellWar plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCustomEvent(Event event) {
		if (event.getEventName().equals("CellAttack")) {
			CellAttackEvent cellAttackEvent = (CellAttackEvent)event;
			try {
				CellUnderAttack cell = cellAttackEvent.getData();
				CellWar.registerAttack(cell);
				plugin.getServer().broadcastMessage(String.format("%s is under attack by %s.", cell.getCellString(), cell.getNameOfFlagOwner()));
			} catch (Exception e) {
				cellAttackEvent.getPlayer().sendMessage(e.getMessage());
			}
		} else if (event.getEventName().equals("CellDefended")) {
			CellDefendedEvent cellDefendedEvent = (CellDefendedEvent)event;
			CellUnderAttack cell = cellDefendedEvent.getCell().getAttackData();
			plugin.getServer().broadcastMessage(String.format("The attack on %s was defended.", cell.getCellString()));
			cell.cancel();
		} else if (event.getEventName().equals("CellWon")) {
			CellWonEvent cellWonEvent = (CellWonEvent)event;
			CellUnderAttack cell = cellWonEvent.getCellAttackData();
			plugin.getServer().broadcastMessage(String.format("%s won %s.", cell.getNameOfFlagOwner(), cell.getCellString()));
		}
	}
}
