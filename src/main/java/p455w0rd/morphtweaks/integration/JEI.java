package p455w0rd.morphtweaks.integration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.transfer.RecipeTransferHandlerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import p455w0rd.morphtweaks.container.SlotEnchanter;
import p455w0rd.morphtweaks.init.ModBlocks;
import p455w0rd.morphtweaks.init.ModGlobals;
import p455w0rd.morphtweaks.init.ModIntegration.Mods;
import p455w0rd.morphtweaks.integration.jei.EnchanterRecipeCategory;

/**
 * @author p455w0rd
 *
 */
@JEIPlugin
public class JEI implements IModPlugin {

	public static IIngredientBlacklist blacklist;
	private static RecipesGui recipesGui;

	@Override
	public void register(@Nonnull IModRegistry registry) {
		blacklist = registry.getJeiHelpers().getIngredientBlacklist();
		registry.addRecipes(EnchanterRecipeCategory.getRecipes(), UID.ENCHANTER);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.ENCHANTER), UID.ENCHANTER);
		//registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerEnchanter.class, UID.ENCHANTER, 0, 3, 4, 36);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime runtime) {
		recipesGui = (RecipesGui) runtime.getRecipesGui();
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry registry) {
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new EnchanterRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	public static void blacklistItem(ItemStack stack) {
		if (Mods.JEI.isLoaded() && blacklist != null && !isItemBlacklisted(stack)) {
			blacklist.addIngredientToBlacklist(stack);
		}
	}

	public static boolean isItemBlacklisted(ItemStack stack) {
		if (Mods.JEI.isLoaded()) {
			return blacklist.isIngredientBlacklisted(stack);
		}
		return false;
	}

	public static void whitelistItem(ItemStack stack) {
		if (Mods.JEI.isLoaded() && isItemBlacklisted(stack)) {
			blacklist.removeIngredientFromBlacklist(stack);
		}
	}

	public static void handleItemBlacklisting(ItemStack stack, boolean shouldBlacklist) {
		if (shouldBlacklist) {
			if (!isItemBlacklisted(stack)) {
				blacklistItem(stack);
			}
			return;
		}
		if (isItemBlacklisted(stack)) {
			whitelistItem(stack);
		}
	}

	public static void showRecipes(List<String> categoryUIDs) {
		recipesGui.showCategories(categoryUIDs);
	}

	public static class UID {
		public static final String ENCHANTER = ModGlobals.MODID + ".enchanter";
	}

	public class RecipeTransferHandler<T extends Container> implements IRecipeTransferHandler<T> {

		private final Class<T> containerClass;

		RecipeTransferHandler(Class<T> containerClass) {
			this.containerClass = containerClass;
		}

		@Override
		public Class<T> getContainerClass() {
			return containerClass;
		}

		@Nullable
		@Override
		public IRecipeTransferError transferRecipe(T container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer, boolean doTransfer) {

			if (!doTransfer) {
				return null;
			}

			Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients = recipeLayout.getItemStacks().getGuiIngredients();

			final NBTTagCompound recipe = new NBTTagCompound();

			int slotIndex = 0;
			for (Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> ingredientEntry : ingredients.entrySet()) {
				IGuiIngredient<ItemStack> ingredient = ingredientEntry.getValue();
				if (!ingredient.isInput()) {
					continue;
				}

				for (final Slot slot : container.inventorySlots) {
					if (slot instanceof SlotEnchanter) {
						if (slot.getSlotIndex() == slotIndex) {
							final NBTTagList tags = new NBTTagList();
							final List<ItemStack> list = new LinkedList<ItemStack>();
							final ItemStack displayed = ingredient.getDisplayedIngredient();
							if (displayed != null && !displayed.isEmpty()) {
								list.add(displayed);
							}
							for (ItemStack stack : ingredient.getAllIngredients()) {
								list.add(stack);
							}
							for (final ItemStack is : list) {
								final NBTTagCompound tag = new NBTTagCompound();
								is.writeToNBT(tag);
								tags.appendTag(tag);
							}
							recipe.setTag("#" + slot.getSlotIndex(), tags);
							break;
						}
					}
				}
				slotIndex++;
			}
			IRecipeTransferError error = new RecipeTransferHandlerHelper().createUserErrorForSlots("test", Lists.newArrayList(1));
			error.showError(Minecraft.getMinecraft(), Mouse.getX(), Mouse.getY(), recipeLayout, 7, 40);
			return error;
		}
	}

}