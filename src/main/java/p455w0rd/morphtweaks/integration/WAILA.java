package p455w0rd.morphtweaks.integration;

import java.util.List;

import com.google.common.collect.Lists;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import p455w0rd.morphtweaks.blocks.BlockNewTFPortal;
import p455w0rd.morphtweaks.init.ModLogger;
import p455w0rd.morphtweaks.init.ModIntegration.Mods;

/**
 * @author p455w0rd
 *
 */
public class WAILA implements IWailaDataProvider {

	private static WAILA INSTANCE = new WAILA();

	public static String toolTipEnclose = TextFormatting.BOLD + "" + TextFormatting.GREEN + "=====================";
	public static String doSneak = TextFormatting.BOLD + "" + TextFormatting.ITALIC + "<Sneak for Info> ";

	public static void init() {
		ModLogger.info("Waila Integation: Enabled");
		FMLInterModComms.sendMessage(Mods.WAILA.getId(), "register", WAILA.class.getName() + ".callbackRegister");
	}

	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.registerHeadProvider(INSTANCE, BlockNewTFPortal.class);
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		if (itemStack.getItem() instanceof ItemBlock) {
			ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
			return Lists.newArrayList(I18n.translateToLocal(itemBlock.getBlock().getUnlocalizedName() + ".name"));
		}
		return currenttip;
	}

}
