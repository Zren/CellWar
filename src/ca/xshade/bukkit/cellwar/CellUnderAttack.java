package ca.xshade.bukkit.cellwar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CellUnderAttack extends Cell {
	private String nameOfFlagOwner;
	private List<Block> beaconBlocks = new ArrayList<Block>();
	private Block flagBaseBlock, flagBlock;
	private int flagColorId;
	private CellAttackThread thread;
	
	public CellUnderAttack(String nameOfFlagOwner, Block flagBaseBlock) {
		super(flagBaseBlock.getLocation());
		this.nameOfFlagOwner = nameOfFlagOwner;
		this.flagBaseBlock = flagBaseBlock;
		this.flagColorId = 0;
		this.thread = new CellAttackThread(this);
		
		int x = flagBaseBlock.getX();
		int z = flagBaseBlock.getZ();
		World world = flagBaseBlock.getWorld();
		int maxY = world.getMaxHeight();
		
		this.flagBlock = flagBaseBlock.getWorld().getBlockAt(flagBaseBlock.getX(), flagBaseBlock.getY() + 1, flagBaseBlock.getZ());
		
		if (CellWarConfig.isDrawingBeacon()) {
			int beaconStartY = world.getMaxHeight() - CellWarConfig.getBeaconSize() - 1;
			if (beaconStartY <= flagBlock.getY() + 1)
				beaconStartY = flagBlock.getY() + 2;
			for (int y = beaconStartY; y < maxY; y++) {
				beaconBlocks.add(world.getBlockAt(x, y, z));
			}
			
			if (flagBaseBlock.getY() + 1 <= world.getMaxHeight())
				beaconBlocks.add(world.getBlockAt(x, flagBlock.getY() + 1, z));
		}
	}
	
	public Block getFlagBaseBlock() {
		return flagBaseBlock;
	}
	
	public String getNameOfFlagOwner() {
		return nameOfFlagOwner;
	}
	
	public boolean hasEnded() {
		return flagColorId >= CellWarConfig.getWoolColors().length;
	}

	public void changeFlag() {
		flagColorId += 1;
		drawFlag();
	}
	
	public void drawFlagBase() {
		flagBaseBlock.setType(Material.FENCE);
	}
	
	public void destroyFlagBase() {
		flagBaseBlock.setType(Material.AIR);
	}
	
	public void drawFlag() {
		DyeColor[] woolColors = CellWarConfig.getWoolColors();
		if (flagColorId < woolColors.length) {
			this.flagBlock.setTypeIdAndData(Material.WOOL.getId(), woolColors[flagColorId].getData(), false);
			//System.out.println(String.format("Flag at %s turned %s.", getCellString(), woolColors[flagColorId].toString()));
		}
	}
	
	public void destroyFlag() {
		this.flagBlock.setType(Material.AIR);
		if (CellWarConfig.isDrawingBeacon()) {
			destroyBeacon();
		}
	}
	
	public void drawBeacon() {
		for (Block block : beaconBlocks) {
			block.setType(Material.GLOWSTONE);
		}
	}
	
	public void destroyBeacon() {
		for (Block block : beaconBlocks) {
			block.setType(Material.AIR);
		}
	}
	
	public void begin() {
		this.thread.start();
	}
	
	public void cancel() {
		this.thread.setRunning(false);
	}

	public String getCellString() {
		return String.format("%s (%d, %d)", getWorldName(), getX(), getZ());
	}

	public boolean isFlag(Block block) {
		return this.flagBlock.equals(block);
	}
	
	public boolean isFlagBase(Block block) {
		return this.flagBaseBlock.equals(block);
	}
	
	public boolean isPartOfBeacon(Block block) {
		return beaconBlocks.contains(block);
	}
	
	public boolean isUneditableBlock(Block block) {
		return isPartOfBeacon(block) || isFlagBase(block);
	}
}
