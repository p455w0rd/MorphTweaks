package p455w0rd.morphtweaks.init;

import codechicken.lib.CodeChickenLib;

public class ModGlobals {

	public static final String MODID_PWLIB = "p455w0rdslib";

	public static final String MODID = "morphtweaks";
	public static final String VERSION = "1.3.6";
	public static final String NAME = "Morph Tweaks";
	public static final String SERVER_PROXY = "p455w0rd.morphtweaks.proxy.CommonProxy";
	public static final String CLIENT_PROXY = "p455w0rd.morphtweaks.proxy.ClientProxy";
	public static final String GUI_FACTORY = "p455w0rd.morphtweaks.init.ModGuiFactory";
	public static final String CONFIG_FILE = "config/MorphTweaks.cfg";
	public static final String DEPENDANCIES = "after:twilightforest;" + CodeChickenLib.MOD_VERSION_DEP + "after:thermalfoundation;after:tconstruct;after:*";

	//public static float TIME = 0.0F;
	private static int CLIENT_TICKS = 0;

	public static int getClientTicks() {
		return CLIENT_TICKS;
	}

	public static void incClientTicks() {
		CLIENT_TICKS++;
	}

}
