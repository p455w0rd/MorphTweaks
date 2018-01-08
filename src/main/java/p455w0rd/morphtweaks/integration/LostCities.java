package p455w0rd.morphtweaks.integration;

import mcjty.lostcities.dimensions.world.LostCitiesTerrainGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

/**
 * @author p455w0rd
 *
 */
public class LostCities {

	public static void changeWaterToAir() {
		LostCitiesTerrainGenerator.setupChars();
		LostCitiesTerrainGenerator.liquidChar = (char) Block.BLOCK_STATE_IDS.get(Blocks.AIR.getDefaultState());
	}

}
