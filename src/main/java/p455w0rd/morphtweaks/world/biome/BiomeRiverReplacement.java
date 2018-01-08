package p455w0rd.morphtweaks.world.biome;

/**
 * @author p455w0rd
 *
 */
public class BiomeRiverReplacement extends BiomeReplacement {

	public BiomeRiverReplacement() {
		super(new BiomeProperties("ReverReplacement").setBaseHeight(-0.5F).setHeightVariation(0.0F));
		spawnableCreatureList.clear();
	}

}
