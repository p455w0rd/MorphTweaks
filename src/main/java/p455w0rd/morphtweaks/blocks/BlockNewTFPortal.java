package p455w0rd.morphtweaks.blocks;

import java.util.Random;

import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.morphtweaks.MorphTweaks;
import p455w0rd.morphtweaks.api.IModelHolder;
import p455w0rd.morphtweaks.api.ITOPBlockDisplayOverride;
import p455w0rd.morphtweaks.client.particle.ParticlePortal2;
import p455w0rd.morphtweaks.init.ModBlocks;
import p455w0rd.morphtweaks.init.ModGlobals;
import p455w0rd.morphtweaks.init.ModIntegration.Mods;
import p455w0rd.morphtweaks.integration.TwilightForest;

/**
 * @author p455w0rd
 *
 */
public class BlockNewTFPortal extends BlockBreakable implements IModelHolder, ITOPBlockDisplayOverride {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
	private static final String NAME = "new_tf_portal";

	public BlockNewTFPortal() {
		super(Material.PORTAL, false);
		setUnlocalizedName(NAME);
		setRegistryName(NAME);
		setHardness(-1F);
		setSoundType(SoundType.GLASS);
		setLightLevel(0.75F);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	@Deprecated
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block notUsed, BlockPos fromPos) {
		boolean good = world.getBlockState(pos.down()).getMaterial().isSolid();
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (!good) {
				break;
			}
			IBlockState neighboringState = world.getBlockState(pos.offset(facing));

			good = TwilightForest.isGrassOrDirt(neighboringState) || neighboringState.getBlock() == ModBlocks.NEW_TF_PORTAL;
		}
		if (!good) {
			world.playEvent(2001, pos, Block.getStateId(state));
			world.setBlockState(pos, TwilightForest.getFluidBlock().getDefaultState(), 3);
		}
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (!entity.isRiding() && entity.getPassengers().isEmpty() && entity.timeUntilPortal <= 0) {
			if (entity instanceof EntityPlayerMP && Mods.TWILIGHTFOREST.isLoaded()) {
				EntityPlayerMP playerMP = (EntityPlayerMP) entity;
				TwilightForest.sendToTwilightForest(playerMP);
			}
			else {
				if (entity.dimension != TwilightForest.getTFDimensionID()) {
					TwilightForest.changeDimension(entity, TwilightForest.getTFDimensionID());
				}
				else {
					TwilightForest.changeDimension(entity, 0);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (rand.nextInt(100) == 0) {
			worldIn.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
		}

		for (int i = 0; i < 4; ++i) {
			double d0 = pos.getX() + rand.nextFloat();
			double d1 = pos.getY() + rand.nextFloat();
			double d2 = pos.getZ() + rand.nextFloat();
			double d3 = (rand.nextFloat() - 0.5D) * 0.5D;
			double d4 = (rand.nextFloat() - 0.5D) * 0.5D;
			double d5 = (rand.nextFloat() - 0.5D) * 0.5D;
			int j = rand.nextInt(2) * 2 - 1;

			if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this) {
				d0 = pos.getX() + 0.5D + 0.25D * j;
				d3 = rand.nextFloat() * 2.0F * j;
			}
			else {
				d2 = pos.getZ() + 0.5D + 0.25D * j;
				d5 = rand.nextFloat() * 2.0F * j;
			}

			MorphTweaks.PROXY.spawnParticle(new ParticlePortal2(worldIn, d0, d1, d2, d3, d4, d5));

		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(new ResourceLocation(ModGlobals.MODID, "new_tf_portal"), "inventory"));
	}

	@Override
	public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		ItemStack stack = new ItemStack(ModBlocks.NEW_TF_PORTAL);
		probeInfo.horizontal().item(stack).vertical().text(I18n.translateToLocal(getUnlocalizedName() + ".name")).text(TextStyleClass.MODNAME.toString() + Tools.getModName(blockState.getBlock()));
		return true;
	}

}
