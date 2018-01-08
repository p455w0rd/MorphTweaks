package p455w0rd.morphtweaks.init;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import p455w0rd.morphtweaks.MorphTweaks;
import p455w0rd.morphtweaks.blocks.tiles.TileEnchanter;
import p455w0rd.morphtweaks.blocks.tiles.TileVoidifier;
import p455w0rd.morphtweaks.client.gui.GuiEnchanter;
import p455w0rd.morphtweaks.client.gui.GuiVoidifier;
import p455w0rd.morphtweaks.container.ContainerVoidifier;

/**
 * @author p455w0rd
 *
 */
public class ModGuiHandler implements IGuiHandler {

	public static void init() {
		ModLogger.info("Registering GUI Handler");
		NetworkRegistry.INSTANCE.registerGuiHandler(ModGlobals.MODID, new ModGuiHandler());
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World worldIn, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		switch (GUIType.values()[id]) {
		case VOIDIFIER:
			if (getVoidifier(worldIn, pos) != null) {
				TileVoidifier te = getVoidifier(worldIn, pos);
				return new ContainerVoidifier(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), te.invItem, te.invFluid, player);
			}
			break;
		case ENCHANTER:
			if (getEnchanter(worldIn, pos) != null) {
				TileEnchanter te = getEnchanter(worldIn, pos);
				return te.getContainerForPlayer(player);
			}
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World worldIn, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		switch (GUIType.values()[id]) {
		case VOIDIFIER:
			if (getVoidifier(worldIn, pos) != null) {
				TileVoidifier te = getVoidifier(worldIn, pos);
				return new GuiVoidifier(new ContainerVoidifier(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), te.invItem, te.invFluid, player));
			}
			break;
		case ENCHANTER:
			if (getEnchanter(worldIn, pos) != null) {
				TileEnchanter te = getEnchanter(worldIn, pos);
				return new GuiEnchanter(te.getContainerForPlayer(player));
			}
			break;
		default:
			break;
		}
		return null;
	}

	public static void launchGui(GUIType type, EntityPlayer playerIn, World worldIn, int x, int y, int z) {
		playerIn.openGui(MorphTweaks.INSTANCE, type.ordinal(), worldIn, x, y, z);
	}

	public static enum GUIType {
			VOIDIFIER, ENCHANTER;
	}

	private TileVoidifier getVoidifier(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileVoidifier) {
			return (TileVoidifier) te;
		}
		return null;
	}

	private TileEnchanter getEnchanter(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEnchanter) {
			return (TileEnchanter) te;
		}
		return null;
	}

}