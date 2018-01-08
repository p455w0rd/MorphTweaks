package p455w0rd.morphtweaks.init;

import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.morphtweaks.api.IModelHolder;
import p455w0rd.morphtweaks.init.ModIntegration.Mods;
import p455w0rd.morphtweaks.items.ItemBlockNewTFPortal;
import p455w0rd.morphtweaks.items.ItemCaptureWand;

/**
 * @author p455w0rd
 *
 */
public class ModItems {

	private static final NonNullList<Item> ITEM_LIST = NonNullList.<Item>create();

	public static final ItemCaptureWand CAPTURE_WAND = new ItemCaptureWand();
	public static ItemBlockNewTFPortal PORTAL_ITEM = null;

	@SideOnly(Side.CLIENT)
	public static void registerModels() {
		for (Item item : getList()) {
			if (item != null && item instanceof IModelHolder) {
				((IModelHolder) item).initModel();
			}
		}
	}

	public static NonNullList<Item> getList() {
		if (ITEM_LIST.isEmpty()) {
			ITEM_LIST.addAll(Arrays.asList(CAPTURE_WAND));
			if (Mods.TWILIGHTFOREST.isLoaded()) {
				ITEM_LIST.add(PORTAL_ITEM = new ItemBlockNewTFPortal(ModBlocks.NEW_TF_PORTAL));
			}
		}
		return ITEM_LIST;
	}
}
