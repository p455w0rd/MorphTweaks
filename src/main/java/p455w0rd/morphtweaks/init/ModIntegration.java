package p455w0rd.morphtweaks.init;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import p455w0rd.morphtweaks.integration.TOP;
import p455w0rd.morphtweaks.integration.TwilightForest;
import p455w0rd.morphtweaks.integration.WAILA;

/**
 * @author p455w0rd
 *
 */
public class ModIntegration {

	public static void preInit() {
		if (Mods.TOP.isLoaded()) {
			TOP.init();
		}
		else {
			ModLogger.info(Mods.TOP.getName() + " Integation: Disabled");
		}
	}

	public static void init() {
		if (FMLCommonHandler.instance().getSide().isClient()) {
			if (Mods.WAILA.isLoaded()) {
				WAILA.init();
			}
			else {
				ModLogger.info("Waila  : Disabled");
			}
		}

	}

	public static void postInit() {
		if (Mods.TWILIGHTFOREST.isLoaded()) {
			TwilightForest.postInit();
		}
	}

	public static enum Mods {
			TOP("theoneprobe", "The One Probe"), WAILA("waila", "WAILA"), JEI("jei", "JEI"),
			TWILIGHTFOREST("twilightforest", "The Twilight Forest"),
			THERMALFOUNDATION("thermalfoundation", "Thermal Foundation"),
			LOSTCITIES("lostcities", "Lost Cities"), TINKERS("tconstruct", "Tinkers' Construct"),
			DANKNULL("danknull", "/dank/null"), BAUBLES("baubles", "Baubles");

		private String modid, name;

		Mods(String modidIn, String nameIn) {
			modid = modidIn;
			name = nameIn;
		}

		public String getId() {
			return modid;
		}

		public String getName() {
			return name;
		}

		public boolean isLoaded() {
			return Loader.isModLoaded(getId());
		}
	}

}