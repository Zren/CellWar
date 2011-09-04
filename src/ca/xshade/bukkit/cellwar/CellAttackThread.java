package ca.xshade.bukkit.cellwar;


public class CellAttackThread extends Thread {
	CellUnderAttack cell;
	boolean running = false;
	
	public CellAttackThread(CellUnderAttack cellUnderAttack) {
		this.cell = cellUnderAttack;
	}

	@Override
	public void run() {
		running = true;
		cell.drawFlagBase();
		cell.drawFlag();
		if (CellWarConfig.isDrawingBeacon())
			cell.drawBeacon();
		while (running && !cell.hasEnded()) {
			try {
				Thread.sleep(CellWarConfig.getTimeBetweenFlagColorChange());
			} catch (InterruptedException e) {
				return;
			}
			if (running) {
				cell.changeFlag();
				if (cell.hasEnded())
					CellWar.attackWon(cell);
			}
		}
		cell.destroyFlag();
		cell.destroyFlagBase();
		if (CellWarConfig.isDrawingBeacon())
			cell.destroyBeacon();
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
}
