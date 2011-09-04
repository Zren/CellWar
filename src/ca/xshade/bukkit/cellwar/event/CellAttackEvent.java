package ca.xshade.bukkit.cellwar.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import ca.xshade.bukkit.cellwar.CellUnderAttack;

public class CellAttackEvent extends Event implements Cancellable {
	private static final long serialVersionUID = -6413227132896218785L;
	private Player player;
	private Block flagBaseBlock;
	private boolean cancel;
	private Throwable exception = null;
	
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

	public boolean isCancelled() {
	    return cancel;
	}
	
	public void setCancelled(boolean cancel) {
	    this.cancel = cancel;
	}
	
	public Throwable getException() {
		return exception;
	}
	
	public void setException(Throwable exception) {
		this.exception = exception;
	}
	
	public boolean hasException() {
		return exception != null;
	}
}
