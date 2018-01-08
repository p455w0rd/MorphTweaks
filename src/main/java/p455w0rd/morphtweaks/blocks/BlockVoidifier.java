package p455w0rd.morphtweaks.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.morphtweaks.api.IModelHolder;
import p455w0rd.morphtweaks.blocks.tiles.TileVoidifier;
import p455w0rd.morphtweaks.init.ModGlobals;
import p455w0rd.morphtweaks.init.ModGuiHandler;
import p455w0rd.morphtweaks.init.ModGuiHandler.GUIType;

/**
 * @author p455w0rd
 *
 */
public class BlockVoidifier extends Block implements ITileEntityProvider, IModelHolder {

	private static final String NAME = "voidifier";

	public BlockVoidifier() {
		super(Material.ROCK, MapColor.PURPLE);
		setUnlocalizedName(NAME);
		setRegistryName(NAME);
		setResistance(20.0F);
		setHardness(5.0F);
		GameRegistry.registerTileEntity(TileVoidifier.class, ModGlobals.MODID + ":voidifier");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft
	 * .world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileVoidifier();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			ModGuiHandler.launchGui(GUIType.VOIDIFIER, player, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.ITALIC + "Voids Items, Fluids, and Forge Energy");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}