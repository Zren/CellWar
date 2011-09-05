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
		cell.drawFlag();
		while (running) {
			try {
				Thread.sleep(CellWarConfig.getTimeBetweenFlagColorChange());
			} catch (InterruptedException e) {
				return;
			}
			if (running) {
				cell.changeFlag();
				if (cell.hasEnded()) {
					CellWar.attackWon(cell);
					cell.cancel();
				}
			}
		}
	}
	
	protected void setRunning(boolean running) {
		this.running = running;
	}
}
