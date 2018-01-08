package p455w0rd.morphtweaks.integration.jei;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import p455w0rd.morphtweaks.api.IEnchanterRecipe;

/**
 * @author p455w0rd
 *
 */
public class EnchanterRecipeWrapper implements IRecipeWrapper {

	private final IEnchanterRecipe recipe;
	@Nonnull
	private final List<List<ItemStack>> inputs = Lists.newArrayList();
	@Nonnull
	private final List<List<ItemStack>> outputs = Lists.newArrayList();

	public EnchanterRecipeWrapper(IEnchanterRecipe recipe) {
		this.recipe = recipe;
		inputs.add(Lists.newArrayList(new ItemStack(Items.WRITABLE_BOOK)));
		inputs.add(Lists.newArrayList(recipe.getRecipeItem()));
		inputs.add(Lists.newArrayList(recipe.getLapis()));
		outputs.add(Lists.newArrayList(recipe.getEnchantedBook()));
	}

	@Override
	public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5, 0.5, 0.5);
		mc.fontRenderer.drawString("XP Levels: " + recipe.getRequiredExperience(), 230, 40, 0xFF00FF00, true);
		GlStateManager.popMatrix();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutputs(ItemStack.class, getOutputs());
	}

	@Nonnull
	public List<ItemStack> getOutputs() {
		return Lists.<ItemStack>newArrayList(recipe.getEnchantedBook());
	}

	public static class EnchanterRecipeWrapperFactory implements IRecipeWrapperFactory<IEnchanterRecipe> {

		@Override
		public IRecipeWrapper getRecipeWrapper(IEnchanterRecipe recipe) {
			return new EnchanterRecipeWrapper(recipe);
		}

	}

}
