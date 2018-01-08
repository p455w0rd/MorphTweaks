package p455w0rd.morphtweaks.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Shadows
 * @author p455w0rd
 *
 */
public class ArrayGen {

	private final IBlockState[][][] states;

	public ArrayGen(IBlockState[][][] statesToPlace) {
		states = statesToPlace;
	}

	public ArrayGen(Block[][][] blocks) {
		IBlockState[][][] states = new IBlockState[blocks.length][][];
		for (int i = 0; i < blocks.length; i++) {
			states[i] = new IBlockState[blocks[i].length][];

			for (int j = 0; j < blocks[i].length; j++) {
				states[i][j] = new IBlockState[blocks[i][j].length];

				for (int k = 0; k < blocks[i][j].length; k++) {
					states[i][j][k] = blocks[i][j][k] == null ? null : blocks[i][j][k].getDefaultState();
				}
			}
		}
		this.states = states;
	}

	public void placeStateArray(BlockPos pos, World world) {
		placeStateArray(pos, world, EnumFacing.NORTH);
	}

	public void placeStateArray(BlockPos pos, World world, EnumFacing playerFacing) {
		int xOffset = 0;
		int zOffset = 0;

		for (int y = 0; y < states.length; y++) {
			for (int x = 0; x < states[y].length; x++) {
				for (int z = 0; z < states[y][x].length; z++) {
					if (states[y][x][z] != null) {
						switch (playerFacing) {
						case NORTH:
							xOffset = 0;
							zOffset = -(states[y][x].length - 1);
							break;
						case SOUTH:
							xOffset = -(states[y][x].length - 1);
							zOffset = 0;
							break;
						case WEST:
							xOffset = -(states[y][x].length - 1);
							zOffset = -(states[y][x].length - 1);
							break;
						case EAST:
						default:
							break;
						}
						world.setBlockState(pos.add(x + xOffset, y, z + zOffset), states[y][x][z]);
					}
				}
			}
		}
	}

}