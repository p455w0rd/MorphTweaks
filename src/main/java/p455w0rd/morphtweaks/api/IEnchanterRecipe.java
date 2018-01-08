package p455w0rd.morphtweaks.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;
import p455w0rd.morphtweaks.inventory.InventoryEnchanter;

/**
 * @author p455w0rd
 *
 */
public interface IEnchanterRecipe extends IForgeRegistryEntry<IEnchanterRecipe> {

	boolean matches(InventoryEnchanter inv);

	ItemStack getCraftingResult(InventoryEnchanter inv);

	ItemStack getRecipeItem();

	int getRequiredItemCount();

	int getRequiredLapis();

	ItemStack getLapis();

	ItemStack getEnchantedBook();

	void craftItem(InventoryEnchanter craftInput, EntityPlayer player);

	int getEnchantmentLevel();

	int getRequiredExperience();

}
