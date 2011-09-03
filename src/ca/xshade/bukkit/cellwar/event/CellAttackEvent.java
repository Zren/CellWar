package ca.xshade.bukkit.cellwar.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import ca.xshade.bukkit.cellwar.CellUnderAttack;

public class CellAttackEvent extends Event {
	private static final long serialVersionUID = -6413227132896218785L;
	private Player player;
	private Block flagBaseBlock;
	
	public CellAttackEvent(Player player, Block flagBaseBlock) {
		super("CellAttack");
		this.player = player;
		this.flagBaseBlock = flagBaseBlock;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Block getFlagBaseBlock() {
		return flagBaseBlock;
	}
	
	public CellUnderAttack getData() {
		return new CellUnderAttack(player.getName(), flagBaseBlock);
	}
}
