package ca.xshade.bukkit.cellwar.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import ca.xshade.bukkit.cellwar.Cell;
import ca.xshade.bukkit.cellwar.CellUnderAttack;
import ca.xshade.bukkit.cellwar.CellWar;
import ca.xshade.bukkit.cellwar.CellWarConfig;
import ca.xshade.bukkit.cellwar.event.CellAttackEvent;

public class CellWarBlockListener extends BlockListener {
	private CellWar plugin;
	
	public CellWarBlockListener(CellWar plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlockPlaced();
		
		if (block == null)
			return;
		
		if (block.getType() == Material.WOOL) {
			int topY = block.getWorld().getHighestBlockYAt(block.getX(), block.getZ()) - 1;
			if (block.getY() >= topY) {
				CellAttackEvent cellAttackEvent = new CellAttackEvent(player, block);
				this.plugin.getServer().getPluginManager().callEvent(cellAttackEvent);
			}
		}
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if (block.getType() == Material.WOOL) {
			Cell cell = Cell.parse(block.getLocation());
			if (cell.isUnderAttack()) {
				CellUnderAttack cellAttackData = cell.getAttackData();
				if (cellAttackData.isFlag(block)) {
					CellWar.attackDefended(player, cellAttackData);
					event.setCancelled(true);
				}
			}
		} else if (CellWarConfig.isDrawingBeacon() && block.getType() == Material.GLOWSTONE) {
			Cell cell = Cell.parse(block.getLocation());
			if (cell.isUnderAttack()) {
				CellUnderAttack cellAttackData = cell.getAttackData();
				if (cellAttackData.isPartOfBeacon(block)) {
					event.setCancelled(true);
				}
			}
		}
	}
}
