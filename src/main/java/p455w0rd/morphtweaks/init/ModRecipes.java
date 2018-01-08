package p455w0rd.morphtweaks.init;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import p455w0rd.morphtweaks.api.IEnchanterRecipe;
import p455w0rd.morphtweaks.recipes.RecipeEnchanter;

/**
 * @author p455w0rd
 *
 */
public class ModRecipes {

	private static List<ItemStack> ENCHANTER_RECIPE_ITEMS = Lists.newArrayList();
	private static List<ItemStack> ENCHANTER_RECIPE_ENCHANTED_BOOKS = Lists.newArrayList();

	public static final List<IEnchanterRecipe> getEnchanterRecipes() {
		List<IEnchanterRecipe> recipes = Lists.newArrayList();
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("silk_touch"), new ItemStack(Items.SLIME_BALL), 1, 15, Enchantments.SILK_TOUCH, 1, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("efficiency1"), new ItemStack(Items.REDSTONE), 12, 3, Enchantments.EFFICIENCY, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("efficiency2"), new ItemStack(Items.REDSTONE), 24, 6, Enchantments.EFFICIENCY, 2, 10));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("efficiency3"), new ItemStack(Items.REDSTONE), 36, 9, Enchantments.EFFICIENCY, 3, 18));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("efficiency4"), new ItemStack(Items.REDSTONE), 48, 12, Enchantments.EFFICIENCY, 4, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("efficiency5"), new ItemStack(Items.REDSTONE), 64, 15, Enchantments.EFFICIENCY, 5, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("fire_aspect1"), new ItemStack(Items.BLAZE_ROD), 8, 3, Enchantments.FIRE_ASPECT, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("fire_aspect2"), new ItemStack(Items.BLAZE_ROD), 16, 6, Enchantments.FIRE_ASPECT, 2, 10));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("knockback1"), new ItemStack(Blocks.PISTON), 1, 3, Enchantments.KNOCKBACK, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("knockback2"), new ItemStack(Blocks.PISTON), 2, 6, Enchantments.KNOCKBACK, 2, 10));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("bane1"), new ItemStack(Items.SPIDER_EYE), 12, 3, Enchantments.BANE_OF_ARTHROPODS, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("bane2"), new ItemStack(Items.SPIDER_EYE), 24, 6, Enchantments.BANE_OF_ARTHROPODS, 2, 10));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("bane3"), new ItemStack(Items.SPIDER_EYE), 36, 9, Enchantments.BANE_OF_ARTHROPODS, 3, 18));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("bane4"), new ItemStack(Items.SPIDER_EYE), 48, 12, Enchantments.BANE_OF_ARTHROPODS, 4, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("bane5"), new ItemStack(Items.SPIDER_EYE), 64, 15, Enchantments.BANE_OF_ARTHROPODS, 5, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("sharpness1"), new ItemStack(Items.QUARTZ), 12, 3, Enchantments.SHARPNESS, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("sharpness2"), new ItemStack(Items.QUARTZ), 24, 6, Enchantments.SHARPNESS, 2, 10));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("sharpness3"), new ItemStack(Items.QUARTZ), 36, 9, Enchantments.SHARPNESS, 3, 18));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("sharpness4"), new ItemStack(Items.QUARTZ), 48, 12, Enchantments.SHARPNESS, 4, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("sharpness5"), new ItemStack(Items.QUARTZ), 64, 15, Enchantments.SHARPNESS, 5, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("smite1"), new ItemStack(Items.ROTTEN_FLESH), 12, 3, Enchantments.SMITE, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("smite2"), new ItemStack(Items.ROTTEN_FLESH), 24, 6, Enchantments.SMITE, 2, 10));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("smite3"), new ItemStack(Items.ROTTEN_FLESH), 36, 9, Enchantments.SMITE, 3, 18));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("smite4"), new ItemStack(Items.ROTTEN_FLESH), 48, 12, Enchantments.SMITE, 4, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("smite5"), new ItemStack(Items.ROTTEN_FLESH), 64, 15, Enchantments.SMITE, 5, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("thorns1"), new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), 1, 3, Enchantments.THORNS, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("thorns2"), new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), 2, 6, Enchantments.THORNS, 2, 15));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("thorns3"), new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), 3, 9, Enchantments.THORNS, 3, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("thorns4"), new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), 4, 12, Enchantments.THORNS, 4, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("frost1"), new ItemStack(Blocks.ICE), 16, 3, Enchantments.FROST_WALKER, 1, 15));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("frost2"), new ItemStack(Blocks.ICE), 32, 6, Enchantments.FROST_WALKER, 2, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("depthstrider1"), new ItemStack(Blocks.PRISMARINE), 1, 3, Enchantments.DEPTH_STRIDER, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("depthstrider2"), new ItemStack(Blocks.PRISMARINE), 2, 6, Enchantments.DEPTH_STRIDER, 2, 15));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("depthstrider3"), new ItemStack(Blocks.PRISMARINE), 3, 9, Enchantments.DEPTH_STRIDER, 3, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("aqua_affinity"), new ItemStack(Blocks.WATERLILY), 1, 15, Enchantments.AQUA_AFFINITY, 1, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("respiration1"), new ItemStack(Items.GLASS_BOTTLE), 1, 3, Enchantments.RESPIRATION, 1, 7));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("respiration2"), new ItemStack(Items.GLASS_BOTTLE), 2, 6, Enchantments.RESPIRATION, 2, 22));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("respiration3"), new ItemStack(Items.GLASS_BOTTLE), 3, 9, Enchantments.RESPIRATION, 3, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("looting1"), new ItemStack(Items.SKULL, 1, 1), 1, 3, Enchantments.LOOTING, 1, 7));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("looting2"), new ItemStack(Items.SKULL, 1, 1), 2, 6, Enchantments.LOOTING, 2, 22));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("looting3"), new ItemStack(Items.SKULL, 1, 1), 3, 9, Enchantments.LOOTING, 3, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("projectile_prot1"), new ItemStack(Items.ARROW), 16, 3, Enchantments.PROJECTILE_PROTECTION, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("projectile_prot2"), new ItemStack(Items.ARROW), 32, 6, Enchantments.PROJECTILE_PROTECTION, 2, 15));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("projectile_prot3"), new ItemStack(Items.ARROW), 48, 9, Enchantments.PROJECTILE_PROTECTION, 3, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("projectile_prot4"), new ItemStack(Items.ARROW), 64, 12, Enchantments.PROJECTILE_PROTECTION, 4, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("blast_prot1"), new ItemStack(Items.GUNPOWDER), 16, 3, Enchantments.BLAST_PROTECTION, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("blast_prot2"), new ItemStack(Items.GUNPOWDER), 32, 6, Enchantments.BLAST_PROTECTION, 2, 15));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("blast_prot3"), new ItemStack(Items.GUNPOWDER), 48, 9, Enchantments.BLAST_PROTECTION, 3, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("blast_prot4"), new ItemStack(Items.GUNPOWDER), 64, 12, Enchantments.BLAST_PROTECTION, 4, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("falling1"), new ItemStack(Items.FEATHER), 4, 3, Enchantments.FEATHER_FALLING, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("falling2"), new ItemStack(Items.FEATHER), 8, 6, Enchantments.FEATHER_FALLING, 2, 15));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("falling3"), new ItemStack(Items.FEATHER), 12, 9, Enchantments.FEATHER_FALLING, 3, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("falling4"), new ItemStack(Items.FEATHER), 16, 12, Enchantments.FEATHER_FALLING, 4, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("prot1"), new ItemStack(Items.IRON_INGOT), 16, 3, Enchantments.PROTECTION, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("prot2"), new ItemStack(Items.IRON_INGOT), 32, 6, Enchantments.PROTECTION, 2, 15));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("prot3"), new ItemStack(Items.IRON_INGOT), 48, 9, Enchantments.PROTECTION, 3, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("prot4"), new ItemStack(Items.IRON_INGOT), 64, 12, Enchantments.PROTECTION, 4, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("fire_prot1"), new ItemStack(Items.BLAZE_POWDER), 16, 3, Enchantments.FIRE_PROTECTION, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("fire_prot2"), new ItemStack(Items.BLAZE_POWDER), 32, 6, Enchantments.FIRE_PROTECTION, 2, 15));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("fire_prot3"), new ItemStack(Items.BLAZE_POWDER), 48, 9, Enchantments.FIRE_PROTECTION, 3, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("fire_prot4"), new ItemStack(Items.BLAZE_POWDER), 64, 12, Enchantments.FIRE_PROTECTION, 4, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("sweeping1"), new ItemStack(Items.SNOWBALL), 5, 3, Enchantments.SWEEPING, 1, 7));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("sweeping2"), new ItemStack(Items.SNOWBALL), 10, 6, Enchantments.SWEEPING, 2, 22));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("sweeping3"), new ItemStack(Items.SNOWBALL), 16, 9, Enchantments.SWEEPING, 3, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("lure1"), new ItemStack(Items.FISH), 1, 3, Enchantments.LURE, 1, 7));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("lure2"), new ItemStack(Items.FISH), 2, 6, Enchantments.LURE, 2, 22));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("lure3"), new ItemStack(Items.FISH), 3, 9, Enchantments.LURE, 3, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("luck1"), new ItemStack(Items.GOLD_INGOT), 8, 3, Enchantments.LUCK_OF_THE_SEA, 1, 7));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("luck2"), new ItemStack(Items.GOLD_INGOT), 16, 6, Enchantments.LUCK_OF_THE_SEA, 2, 22));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("luck3"), new ItemStack(Items.GOLD_INGOT), 32, 9, Enchantments.LUCK_OF_THE_SEA, 3, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("infinity"), new ItemStack(Items.ENDER_PEARL), 1, 15, Enchantments.INFINITY, 1, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("flame"), new ItemStack(Blocks.MAGMA), 1, 15, Enchantments.FLAME, 1, 15));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("punch1"), new ItemStack(Blocks.CACTUS), 16, 3, Enchantments.PUNCH, 1, 15));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("punch2"), new ItemStack(Blocks.CACTUS), 32, 6, Enchantments.PUNCH, 2, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("fortune1"), new ItemStack(Items.EMERALD), 1, 3, Enchantments.FORTUNE, 1, 7));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("fortune2"), new ItemStack(Items.EMERALD), 2, 6, Enchantments.FORTUNE, 2, 22));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("fortune3"), new ItemStack(Items.EMERALD), 3, 9, Enchantments.FORTUNE, 3, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("power1"), new ItemStack(Items.FLINT), 12, 3, Enchantments.POWER, 1, 5));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("power2"), new ItemStack(Items.FLINT), 24, 6, Enchantments.POWER, 2, 10));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("power3"), new ItemStack(Items.FLINT), 36, 9, Enchantments.POWER, 3, 18));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("power4"), new ItemStack(Items.FLINT), 48, 12, Enchantments.POWER, 4, 23));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("power5"), new ItemStack(Items.FLINT), 64, 15, Enchantments.POWER, 5, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("unbreaking1"), new ItemStack(Blocks.OBSIDIAN), 1, 3, Enchantments.UNBREAKING, 1, 7));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("unbreaking2"), new ItemStack(Blocks.OBSIDIAN), 2, 6, Enchantments.UNBREAKING, 2, 22));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("unbreaking3"), new ItemStack(Blocks.OBSIDIAN), 3, 9, Enchantments.UNBREAKING, 3, 30));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("mending"), new ItemStack(Blocks.ANVIL), 1, 15, Enchantments.MENDING, 1, 15));
		recipes.add(new RecipeEnchanter(createEnchanterRegistryName("soulbound"), new ItemStack(Items.DIAMOND), 1, 15, ModEnchantments.SOUL_BOUND, 1, 30));
		return recipes;
	}

	public static List<IEnchanterRecipe> getAllEnchanterRecipesForRecipeItem(ItemStack stack) {
		List<IEnchanterRecipe> recipes = Lists.<IEnchanterRecipe>newArrayList();
		for (IEnchanterRecipe recipe : ModRegistries.ENCHANTER_RECIPES.getValues()) {
			if (recipe.getRecipeItem().isItemEqual(stack)) {
				recipes.add(recipe);

			}
		}
		recipes.sort((recipe1, recipe2) -> {
			if (recipe1.getEnchantmentLevel() == recipe2.getEnchantmentLevel()) {
				return 0;
			}
			return recipe1.getEnchantmentLevel() < recipe2.getEnchantmentLevel() ? -1 : 1;
		});
		return recipes;
	}

	public static IEnchanterRecipe getEnchanterRecipeByRecipeItemAndLapis(ItemStack stack, ItemStack lapis) {
		List<IEnchanterRecipe> possibleRecipes = getAllEnchanterRecipesForRecipeItem(stack);
		if (possibleRecipes.size() > 0) {
			IEnchanterRecipe returnRecipe = null;
			for (IEnchanterRecipe recipe : possibleRecipes) {
				if (stack.getCount() >= recipe.getRequiredItemCount() && lapis.getCount() >= recipe.getRequiredLapis()) {
					returnRecipe = recipe;
				}
			}
			return returnRecipe;
		}
		return null;
	}

	public static IEnchanterRecipe getEnchanterRecipeByRecipeItem(ItemStack stack) {
		List<IEnchanterRecipe> possibleRecipes = getAllEnchanterRecipesForRecipeItem(stack);
		if (possibleRecipes.size() > 0) {
			IEnchanterRecipe returnRecipe = null;
			for (IEnchanterRecipe recipe : possibleRecipes) {
				if (stack.getCount() >= recipe.getRequiredItemCount()) {
					returnRecipe = recipe;
				}
			}
			return returnRecipe;
		}
		return null;
	}

	public static List<ItemStack> getEnchanterRecipeItems() {
		if (ENCHANTER_RECIPE_ITEMS.isEmpty()) {
			for (IEnchanterRecipe recipe : ModRegistries.ENCHANTER_RECIPES.getValues()) {
				ENCHANTER_RECIPE_ITEMS.add(recipe.getRecipeItem());
			}
		}
		return ENCHANTER_RECIPE_ITEMS;
	}

	public static List<ItemStack> getEnchanterRecipeEnchantedBooks() {
		if (ENCHANTER_RECIPE_ENCHANTED_BOOKS.isEmpty()) {
			for (IEnchanterRecipe recipe : ModRegistries.ENCHANTER_RECIPES.getValues()) {
				ENCHANTER_RECIPE_ENCHANTED_BOOKS.add(recipe.getEnchantedBook());
			}
		}
		return ENCHANTER_RECIPE_ENCHANTED_BOOKS;
	}

	public static boolean isValidEnchanterRecipeItem(ItemStack stack) {
		List<ItemStack> validItems = getEnchanterRecipeItems();
		for (ItemStack validItem : validItems) {
			if (stack.isItemEqual(validItem)) {
				return true;
			}
		}
		return false;
	}

	private static ResourceLocation createEnchanterRegistryName(String name) {
		return createRegistryName("enchanter_" + name);
	}

	private static ResourceLocation createRegistryName(String name) {
		return new ResourceLocation(ModGlobals.MODID, name);
	}

}
