package p455w0rd.morphtweaks.init;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import p455w0rd.morphtweaks.init.ModConfig.Options;
import p455w0rd.morphtweaks.util.MTweaksUtil;

/**
 * @author p455w0rd
 *
 */
public class ModCreativeTab extends CreativeTabs {

	public static CreativeTabs TAB;
	public static List<ItemStack> WAND_LIST = Lists.<ItemStack>newArrayList();

	public ModCreativeTab() {
		super(ModGlobals.MODID);
	}

	public static void init() {
		TAB = new ModCreativeTab();
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModBlocks.NEW_TF_PORTAL);
	}

	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ModBlocks.NEW_TF_PORTAL));
		items.add(new ItemStack(ModBlocks.VOIDIFIER));
		items.add(new ItemStack(ModBlocks.ENCHANTER));
		items.add(MTweaksUtil.getEnchantedBook(ModEnchantments.SOUL_BOUND, 1));
		items.add(new ItemStack(ModItems.CAPTURE_WAND));
		items.addAll(getWandList());
	}

	public static List<ItemStack> getWandList() {
		if (WAND_LIST.isEmpty()) {
			for (EntityEntry entry : ForgeRegistries.ENTITIES.getValues()) {
				Class<? extends Entity> tempEntity = entry.getEntityClass();
				if (EntityCreature.class.isAssignableFrom(tempEntity)) {
					if (Options.DISABLE_CAPTURING_HOSTILE_MOBS && EntityMob.class.isAssignableFrom(tempEntity)) {
						continue;
					}
					ItemStack wand = new ItemStack(ModItems.CAPTURE_WAND);
					MTweaksUtil.storeMobInWand(wand, tempEntity, true, true);
					WAND_LIST.add(wand);
				}
			}
		}
		return WAND_LIST;
	}

}
