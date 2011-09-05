package ca.xshade.bukkit.cellwar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CellUnderAttack extends Cell {
	private String nameOfFlagOwner;
	private List<Block> beaconFlagBlocks;
	private List<Block> beaconWireframeBlocks;
	private Block flagBaseBlock, flagBlock, flagLightBlock;
	private int flagColorId;
	private CellAttackThread thread;
	
	public CellUnderAttack(String nameOfFlagOwner, Block flagBaseBlock) {
		super(flagBaseBlock.getLocation());
		this.nameOfFlagOwner = nameOfFlagOwner;
		this.flagBaseBlock = flagBaseBlock;
		this.flagColorId = 0;
		this.thread = new CellAttackThread(this);
		
		World world = flagBaseBlock.getWorld();
		this.flagBlock = world.getBlockAt(flagBaseBlock.getX(), flagBaseBlock.getY() + 1, flagBaseBlock.getZ());
		this.flagLightBlock = world.getBlockAt(flagBaseBlock.getX(), flagBaseBlock.getY() + 2, flagBaseBlock.getZ());
	}
	
	public void loadBeacon() {
		beaconFlagBlocks = new ArrayList<Block>();
		beaconWireframeBlocks = new ArrayList<Block>();
		
		if (!CellWarConfig.isDrawingBeacon())
			return;
		
		int beaconSize = CellWarConfig.getBeaconSize();
		if (CellWarConfig.getCellSize() < beaconSize)
			return;
		
		Block minBlock = getBeaconMinBlock(getFlagBaseBlock().getWorld());
		if (flagBaseBlock.getY() + 4 > minBlock.getY())
			return;
		
		int outerEdge = beaconSize - 1;
		for (int y = 0; y < beaconSize; y++) {
			for (int z = 0; z < beaconSize; z++) {
				for (int x = 0; x < beaconSize; x++) {
					Block block = flagBaseBlock.getWorld().getBlockAt(minBlock.getX() + x, minBlock.getY() + y, minBlock.getZ() + z);
					if (block.isEmpty()) {
						int edgeCount = getEdgeCount(x, y, z, outerEdge);
						if (edgeCount == 1) {
							beaconFlagBlocks.add(block);
						} else if (edgeCount > 1) {
							beaconWireframeBlocks.add(block);
						}
					}
				}
			}
		}
	}
	
	private int getEdgeCount(int x, int y, int z, int outerEdge) {
		return (zeroOr(x, outerEdge) ? 1 : 0) + (zeroOr(y, outerEdge) ? 1 : 0) + (zeroOr(z, outerEdge) ? 1 : 0);
	}
	
	private boolean zeroOr(int n, int max) {
		return n == 0 || n == max;
	}
	
	private Block getBeaconMinBlock(World world) {
		int middle = (int) Math.floor(CellWarConfig.getCellSize() / 2.0);
		int radiusCenterExpansion = CellWarConfig.getBeaconRadius() - 1;
		int fromCorner = middle - radiusCenterExpansion;
		int maxY = world.getMaxHeight();
		
		int cellSize = CellWarConfig.getCellSize();
		
		int x = (getX() * cellSize) + fromCorner;
		int y = maxY - CellWarConfig.getBeaconSize();
		int z = (getZ() * cellSize) + fromCorner;
		
		return world.getBlockAt(x, y, z);
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
		updateFlag();
	}
	
	public void drawFlag() {
		loadBeacon();
		
		flagBaseBlock.setType(CellWarConfig.getFlagBaseMaterial());
		updateFlag();
		flagLightBlock.setType(CellWarConfig.getFlagLightMaterial());
		for (Block block : beaconWireframeBlocks)
			block.setType(CellWarConfig.getBeaconWireFrameMaterial());
	}
	
	public void updateFlag() {
		DyeColor[] woolColors = CellWarConfig.getWoolColors();
		if (flagColorId < woolColors.length) {
			//System.out.println(String.format("Flag at %s turned %s.", getCellString(), woolColors[flagColorId].toString()));
			int woolId = Material.WOOL.getId();
			byte woolData = woolColors[flagColorId].getData();
			
			flagBlock.setTypeIdAndData(woolId, woolData, false);
			for (Block block : beaconFlagBlocks)
				block.setTypeIdAndData(woolId, woolData, false);
		}
	}
	
	public void destroyFlag() {
		flagLightBlock.setType(Material.AIR);
		flagBlock.setType(Material.AIR);
		flagBaseBlock.setType(Material.AIR);
		for (Block block : beaconFlagBlocks)
			block.setType(Material.AIR);
		for (Block block : beaconWireframeBlocks)
			block.setType(Material.AIR);
	}
	
	public void begin() {
		this.thread.start();
	}
	
	public void cancel() {
		this.thread.setRunning(false);
		destroyFlag();
	}

	public String getCellString() {
		return String.format("%s (%d, %d)", getWorldName(), getX(), getZ());
	}

	public boolean isFlagLight(Block block) {
		return this.flagLightBlock.equals(block);
	}
	
	public boolean isFlag(Block block) {
		return this.flagBlock.equals(block);
	}
	
	public boolean isFlagBase(Block block) {
		return this.flagBaseBlock.equals(block);
	}
	
	public boolean isPartOfBeacon(Block block) {
		return beaconFlagBlocks.contains(block) || beaconWireframeBlocks.contains(block);
	}
	
	public boolean isUneditableBlock(Block block) {
		return isPartOfBeacon(block) || isFlagBase(block) || isFlagLight(block);
	}
}
