package p455w0rd.morphtweaks.init;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author p455w0rd
 *
 */
public class ModConfig {

	public static Configuration CONFIG;

	public static final String CAT = "General";

	public static void init() {
		if (CONFIG == null) {
			CONFIG = new Configuration(new File(ModGlobals.CONFIG_FILE));
			MinecraftForge.EVENT_BUS.register(new ModConfig());
			CONFIG.load();
		}

		Options.FLUID_NAME = CONFIG.getString("FluidUsedForPortal", CAT, "minecraft:water", "Fluid which will be used to create the TwilightForest Portal (TF default is water)");
		Options.DISABLE_INFINITE_WATER = CONFIG.getBoolean("DisableInfiniteWater", CAT, true, "Disables vanilla infinite water block mechanic");
		Options.ENABLE_ENDERIUM_BLOCKS = CONFIG.getBoolean("EnableEnderiumFluidBlock", CAT, true, "If enabled, adds fluid enderium block to the world (requires Thermal Foundation and TiC)");
		Options.DISABLE_CAPTURING_HOSTILE_MOBS = CONFIG.getBoolean("DisableHostileMobCapture", CAT, true, "If true, the Capture Wand will only capture neutral and friendly mobs");

		if (CONFIG.hasChanged()) {
			CONFIG.save();
		}
	}

	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent e) {
		if (e.getModID().equals(ModGlobals.MODID)) {
			init();
		}
	}

	public static class Options {

		public static String FLUID_NAME = "minecraft:water";
		public static boolean DISABLE_INFINITE_WATER = true;
		public static boolean ENABLE_ENDERIUM_BLOCKS = true;
		public static boolean DISABLE_CAPTURING_HOSTILE_MOBS = true;

	}

}
