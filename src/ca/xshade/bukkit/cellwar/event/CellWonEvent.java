package ca.xshade.bukkit.cellwar.event;

import org.bukkit.event.Event;

import ca.xshade.bukkit.cellwar.CellUnderAttack;

public class CellWonEvent extends Event {
	private static final long serialVersionUID = 4691420283914184122L;
	private CellUnderAttack cellAttackData;
	
	public CellWonEvent(CellUnderAttack cellAttackData) {
		super("CellWon");
		this.cellAttackData = cellAttackData;
	}
	
	public CellUnderAttack getCellAttackData() {
		return cellAttackData;
	}
}
