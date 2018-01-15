package p455w0rd.morphtweaks.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import p455w0rd.morphtweaks.init.ModBlocks;
import p455w0rd.morphtweaks.init.ModCreativeTab;
import p455w0rd.morphtweaks.init.ModIntegration.Mods;
import p455w0rd.morphtweaks.init.ModItems;
import p455w0rd.morphtweaks.util.MTweaksUtil;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		ModCreativeTab.init();
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		if (Mods.TWILIGHTFOREST.isLoaded()) {
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
				return tintIndex == 0 ? ColorizerGrass.getGrassColor(0.5D, 1.0D) : -1;
			}, ModBlocks.NEW_TF_PORTAL);
		}
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
			return tintIndex != 2 ? MTweaksUtil.getCapturedMobInWand(stack) == null ? -1 : MTweaksUtil.getMobEggColor(MTweaksUtil.getCapturedMobInWand(stack), tintIndex) : -1;
		}, ModItems.CAPTURE_WAND);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	@Override
	public void serverStarting(FMLServerStartingEvent e) {
		super.serverStarting(e);
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}

	@Override
	public World getWorld() {
		return Minecraft.getMinecraft().world;
	}

	@Override
	public void spawnParticle(Object particle) {
		if (particle instanceof Particle) {
			Minecraft.getMinecraft().effectRenderer.addEffect((Particle) particle);
		}
	}

}
