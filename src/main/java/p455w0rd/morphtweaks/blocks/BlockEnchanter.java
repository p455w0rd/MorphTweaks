package p455w0rd.morphtweaks.blocks;

import java.util.Random;

import codechicken.lib.model.ModelRegistryHelper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.morphtweaks.MorphTweaks;
import p455w0rd.morphtweaks.api.IModelHolder;
import p455w0rd.morphtweaks.blocks.tiles.TileEnchanter;
import p455w0rd.morphtweaks.client.particle.ParticleEnchanter;
import p455w0rd.morphtweaks.client.render.EnchanterRenderer;
import p455w0rd.morphtweaks.init.ModGlobals;
import p455w0rd.morphtweaks.init.ModGuiHandler;
import p455w0rd.morphtweaks.init.ModGuiHandler.GUIType;

/**
 * @author p455w0rd
 *
 */
public class BlockEnchanter extends BlockContainer implements IModelHolder {

	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
	private static final String NAME = "enchanter";

	public BlockEnchanter() {
		super(Material.ROCK, MapColor.RED);
		setLightOpacity(0);
		setUnlocalizedName(NAME);
		setRegistryName(NAME);
		GameRegistry.registerTileEntity(TileEnchanter.class, ModGlobals.MODID + "" + NAME);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.randomDisplayTick(stateIn, worldIn, pos, rand);

		for (int i = -2; i <= 2; ++i) {
			for (int j = -2; j <= 2; ++j) {
				if (i > -2 && i < 2 && j == -1) {
					//j = 2;
				}

				if (rand.nextInt(16) == 0) {
					for (int k = 0; k <= 1; ++k) {
						BlockPos blockpos = pos.add(i, k, j);

						//if (net.minecraftforge.common.ForgeHooks.getEnchantPower(worldIn, blockpos) > 0) {
						if (!worldIn.isAirBlock(pos.add(i / 2, 0, j / 2))) {
							break;
						}
						MorphTweaks.PROXY.spawnParticle(new ParticleEnchanter(worldIn, new BlockPos(pos.getX(), pos.getY() + 2.0D, pos.getZ()), i + rand.nextFloat() - 0.5D, k - rand.nextFloat() - 1.0F, j + rand.nextFloat() - 0.5D));
						//worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, pos.getX() + 0.5D, pos.getY() + 2.0D, pos.getZ() + 0.5D, i + rand.nextFloat() - 0.5D, k - rand.nextFloat() - 1.0F, j + rand.nextFloat() - 0.5D);
						//}
					}
				}
			}
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEnchanter();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			ModGuiHandler.launchGui(GUIType.ENCHANTER, player, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
		return p_193383_4_ == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEnchanter.class, new EnchanterRenderer());
		ModelRegistryHelper.registerItemRenderer(Item.getItemFromBlock(this), new EnchanterRenderer());
	}
}