package p455w0rd.morphtweaks.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * @author p455w0rd
 *
 */
public class WorldTypeMorphCraft extends WorldType {

	public static final String NAME = "morphcraft";

	public WorldTypeMorphCraft() {
		super(NAME);
	}

	public WorldTypeMorphCraft(String name) {
		super(NAME + "_" + name);
	}

	@Override
	public boolean isCustomizable() {
		return false;
	}

	@Override
	public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
		return new ChunkGeneratorMorphCraft(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
	}

}
