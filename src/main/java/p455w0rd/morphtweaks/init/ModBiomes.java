package p455w0rd.morphtweaks.init;

import java.util.Arrays;

import net.minecraft.util.NonNullList;
import net.minecraft.world.biome.Biome;
import p455w0rd.morphtweaks.world.biome.BiomeOceanReplacement;
import p455w0rd.morphtweaks.world.biome.BiomeRiverReplacement;

/**
 * @author p455w0rd
 *
 */
public class ModBiomes {

	private static final NonNullList<Biome> BIOME_LIST = NonNullList.<Biome>create();
	public static final Biome OCEAN_REPLACEMENT = new BiomeOceanReplacement().setRegistryName("minecraft:ocean");
	public static final Biome RIVER_REPLACEMENT = new BiomeRiverReplacement().setRegistryName("minecraft:river");

	public static NonNullList<Biome> getList() {
		if (BIOME_LIST.isEmpty()) {
			BIOME_LIST.addAll(Arrays.asList(OCEAN_REPLACEMENT, RIVER_REPLACEMENT));
		}
		return BIOME_LIST;
	}

}
