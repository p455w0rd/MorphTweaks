package p455w0rd.morphtweaks.recipes;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;
import p455w0rd.morphtweaks.api.IEnchanterRecipe;
import p455w0rd.morphtweaks.init.ModRecipes;
import p455w0rd.morphtweaks.inventory.InventoryEnchanter;

/**
 * @author p455w0rd
 *
 */
public class RecipeEnchanter extends IForgeRegistryEntry.Impl<IEnchanterRecipe> implements IEnchanterRecipe {

	ItemStack recipeItem;
	int numLapis;
	int numRecipeItems;
	Enchantment enchantment;
	int enchantmentLevel;
	int requiredExperience;

	public RecipeEnchanter(ResourceLocation registryName, ItemStack recipeItem, int numRecipeItems, int numLapis, Enchantment enchantment, int enchantmentLevel, int requiredExperience) {
		this.recipeItem = recipeItem;
		this.numLapis = numLapis;
		this.numRecipeItems = numRecipeItems;
		this.enchantment = enchantment;
		this.enchantmentLevel = enchantmentLevel;
		this.requiredExperience = requiredExperience;
		setRegistryName(registryName);
	}

	@Override
	public boolean matches(InventoryEnchanter inv) {
		ItemStack bookSlot = inv.getStackInSlot(0);
		ItemStack itemSlot = inv.getStackInSlot(1);
		ItemStack lapisSlot = inv.getStackInSlot(2);
		if (bookSlot.isEmpty() || bookSlot.getItem() != Items.WRITABLE_BOOK) {
			return false;
		}
		if (itemSlot.isEmpty() || itemSlot.getCount() < numRecipeItems || !ModRecipes.isValidEnchanterRecipeItem(itemSlot)) {
			return false;
		}
		if (lapisSlot.isEmpty() || !isLapis(lapisSlot) || (isLapis(lapisSlot) && lapisSlot.getCount() < numLapis)) {
			return false;
		}
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryEnchanter inv) {
		return matches(inv) ? getEnchantedBook() : ItemStack.EMPTY;
	}

	@Override
	public ItemStack getRecipeItem() {
		ItemStack item = recipeItem.copy();
		item.setCount(getRequiredItemCount());
		return item;
	}

	@Override
	public int getRequiredItemCount() {
		return numRecipeItems;
	}

	@Override
	public int getRequiredLapis() {
		return numLapis;
	}

	@Override
	public ItemStack getLapis() {
		ItemStack lapis = new ItemStack(Items.DYE, 1, 4);
		lapis.setCount(getRequiredLapis());
		return lapis;
	}

	private boolean isLapis(ItemStack stack) {
		List<ItemStack> ores = OreDictionary.getOres("gemLapis");
		for (ItemStack ore : ores) {
			if (net.minecraftforge.oredict.OreDictionary.itemMatches(ore, stack, false)) {
				return true;
			}
		}
		return false;
		//return stack.getItem() == Items.DYE && stack.getItemDamage() == 4;
	}

	@Override
	public ItemStack getEnchantedBook() {
		ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
		Map<Enchantment, Integer> enchantmentMap = Maps.newHashMap();
		enchantmentMap.put(enchantment, enchantmentLevel);
		EnchantmentHelper.setEnchantments(enchantmentMap, enchantedBook);
		return enchantedBook;
	}

	@Override
	public void craftItem(InventoryEnchanter craftInput, EntityPlayer player) {
		//player.onEnchant(getEnchantedBook(), getRequiredExperience());
		craftInput.getStackInSlot(0).shrink(1);
		craftInput.getStackInSlot(1).shrink(getRequiredItemCount());
		craftInput.getStackInSlot(2).shrink(numLapis);
	}

	@Override
	public int getEnchantmentLevel() {
		return enchantmentLevel;
	}

	@Override
	public int getRequiredExperience() {
		return requiredExperience;
	}

}
