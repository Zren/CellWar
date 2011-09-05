package ca.xshade.bukkit.cellwar.listener;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

import ca.xshade.bukkit.cellwar.CellUnderAttack;
import ca.xshade.bukkit.cellwar.CellWar;
import ca.xshade.bukkit.cellwar.event.CellAttackEvent;

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
				//Player player = cellAttackEvent.getPlayer();
				//plugin.getServer().broadcastMessage(String.format("%s is under attack by %s.", cell.getCellString(), player.getDisplayName()));
			} catch (Exception e) {
				cellAttackEvent.setException(e);
				//cellAttackEvent.getPlayer().sendMessage(e.getMessage());
			}
		}
		/*
		 * Examples
		 * 
		if (event.getEventName().equals("CellDefended")) {
			CellDefendedEvent cellDefendedEvent = (CellDefendedEvent)event;
			Player player = cellDefendedEvent.getPlayer();
			CellUnderAttack cell = cellDefendedEvent.getCell().getAttackData();
			plugin.getServer().broadcastMessage(String.format("%s defended %s.", player.getDisplayName(), cell.getCellString()));
			cell.cancel();
		} else if (event.getEventName().equals("CellWon")) {
			CellWonEvent cellWonEvent = (CellWonEvent)event;
			CellUnderAttack cell = cellWonEvent.getCellAttackData();
			Player player = plugin.getServer().getPlayer(cell.getNameOfFlagOwner());
			plugin.getServer().broadcastMessage(String.format("%s won %s.", player.getDisplayName(), cell.getCellString()));
		}
		*/
	}
}
