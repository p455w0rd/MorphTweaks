package p455w0rd.morphtweaks.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import p455w0rd.morphtweaks.init.ModConfig;
import p455w0rd.morphtweaks.init.ModEvents;
import p455w0rd.morphtweaks.init.ModGuiHandler;
import p455w0rd.morphtweaks.init.ModIntegration;
import p455w0rd.morphtweaks.init.ModNetworking;
import p455w0rd.morphtweaks.init.ModRegistries;
import p455w0rd.morphtweaks.init.ModTweaks;
import p455w0rd.morphtweaks.init.ModConfig.Options;
import p455w0rd.morphtweaks.init.ModIntegration.Mods;
import p455w0rd.morphtweaks.integration.TFAndTinkers;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		ModConfig.init();
		ModEvents.init();
		ModIntegration.preInit();
		ModNetworking.init();
		ModRegistries.buildRegistries();
		if (Options.ENABLE_ENDERIUM_BLOCKS && Mods.TINKERS.isLoaded() && Mods.THERMALFOUNDATION.isLoaded()) {
			TFAndTinkers.init();
		}
	}

	public void init(FMLInitializationEvent e) {
		ModIntegration.init();
		ModTweaks.init();
	}

	public void postInit(FMLPostInitializationEvent e) {
		ModGuiHandler.init();
		ModIntegration.postInit();
	}

	public void serverStarting(FMLServerStartingEvent e) {

	}

	public EntityPlayer getPlayer() {
		return null;
	}

	public World getWorld() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0];
	}

	public void spawnParticle(Object particle) {
	}

}
