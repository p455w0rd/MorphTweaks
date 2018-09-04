package p455w0rd.morphtweaks.integration;

import cofh.core.util.helpers.DamageHelper;
import cofh.core.util.helpers.ServerHelper;
import cofh.thermalfoundation.entity.monster.EntityBlizz;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.morphtweaks.init.ModGlobals;
import p455w0rd.morphtweaks.init.ModIntegration.Mods;

/**
 * @author p455w0rd
 *
 */
public class TFAndTinkers {

	public static String ENDERIUM = "enderium";
	public static Block ENDERIUM_FLUID_BLOCK = null;

	public static void init() {
		if (Mods.THERMALFOUNDATION.isLoaded() && Mods.TINKERS.isLoaded()) {
			if (FluidRegistry.isFluidRegistered(ENDERIUM)) {
				Fluid enderiumFluid = FluidRegistry.getFluid(ENDERIUM);
				if (enderiumFluid != null && enderiumFluid.getBlock() == null) {
					ENDERIUM_FLUID_BLOCK = new BlockFluidClassic(enderiumFluid, new MaterialLiquid(MapColor.GREEN)) {

						@Override
						public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
							entity.extinguish();
							if (entity.motionY < -0.25 || entity.motionY > 0.25) {
								entity.motionY *= 0.25;
							}
							if (entity.motionZ < -0.25 || entity.motionZ > 0.25) {
								entity.motionZ *= 0.25;
							}
							if (entity.motionX < -0.25 || entity.motionX > 0.25) {
								entity.motionX *= 0.25;
							}
							if (ServerHelper.isClientWorld(world)) {
								return;
							}
							if (world.getTotalWorldTime() % 8 != 0) {
								return;
							}
							if (entity instanceof EntityZombie || entity instanceof EntityCreeper) {
								EntitySnowman snowman = new EntitySnowman(world);
								snowman.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
								world.spawnEntity(snowman);
								entity.setDead();
							}
							else if (entity instanceof EntityBlizz || entity instanceof EntitySnowman) {
								((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SPEED, 6 * 20, 0));
								((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 6 * 20, 0));
							}
							else if (entity instanceof EntityBlaze) {
								entity.attackEntityFrom(DamageHelper.CRYOTHEUM, 10.0F);
							}
							else if (entity instanceof EntityZombieHorse) {
								EntitySkeletonHorse skeleHorse = new EntitySkeletonHorse(world);
								skeleHorse.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
								skeleHorse.setTrap(false);
								world.spawnEntity(skeleHorse);
								entity.setDead();
							}
							else if (entity instanceof EntityItem && isValidPortalActivationItem(((EntityItem) entity).getItem())) {
								//do nothing so it doesn't kill the item
							}
							else {
								boolean t = entity.velocityChanged;
								entity.attackEntityFrom(DamageHelper.CRYOTHEUM, 2.0F);
								entity.velocityChanged = t;
							}
						}
					}.setRegistryName(Mods.THERMALFOUNDATION.getId(), ENDERIUM);
					enderiumFluid.setBlock(ENDERIUM_FLUID_BLOCK);
					ForgeRegistries.BLOCKS.register(ENDERIUM_FLUID_BLOCK);
				}
			}
		}
	}

	private static boolean isValidPortalActivationItem(ItemStack stackIn) {
		for (ItemStack stack : TwilightForest.getPortalActivationItemList()) {
			if (ItemStack.areItemStacksEqual(stack, stackIn)) {
				return true;
			}
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public static void registerEnderiumModel() {
		if (ENDERIUM_FLUID_BLOCK != null) {
			ModelLoader.setCustomStateMapper(ENDERIUM_FLUID_BLOCK, new StateMapperBase() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
					return new ModelResourceLocation(new ResourceLocation(ModGlobals.MODID, "fluid"), ENDERIUM);
				}
			});
		}
	}

}
