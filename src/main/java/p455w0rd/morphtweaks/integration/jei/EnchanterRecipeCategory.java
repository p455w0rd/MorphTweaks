package p455w0rd.morphtweaks.integration.jei;

import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import p455w0rd.morphtweaks.api.IEnchanterRecipe;
import p455w0rd.morphtweaks.init.ModGlobals;
import p455w0rd.morphtweaks.init.ModRegistries;
import p455w0rd.morphtweaks.integration.JEI;
import p455w0rd.morphtweaks.util.TextUtils;

/**
 * @author p455w0rd
 *
 */
public class EnchanterRecipeCategory implements IRecipeCategory<EnchanterRecipeWrapper> {

	private final IDrawable background;
	private final String title;

	public EnchanterRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(new ResourceLocation(ModGlobals.MODID, "textures/gui/enchanter.png"), 12, 20, 179, 26);
		title = I18n.translateToLocal("tile.enchanter.name");
	}

	@Override
	public String getUid() {
		return JEI.UID.ENCHANTER;
	}

	@Override
	public String getTitle() {
		return TextUtils.rainbow(title);
	}

	@Override
	public String getModName() {
		return ModGlobals.NAME;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, EnchanterRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(0, true, 6, 4);
		guiItemStacks.init(1, true, 67, 4);
		guiItemStacks.init(2, true, 89, 4);
		guiItemStacks.init(3, false, 154, 4);
		guiItemStacks.set(ingredients);
	}

	public static List<EnchanterRecipeWrapper> getRecipes() {
		List<EnchanterRecipeWrapper> recipes = Lists.newArrayList();
		for (IEnchanterRecipe recipe : ModRegistries.ENCHANTER_RECIPES.getValues()) {
			recipes.add(new EnchanterRecipeWrapper(recipe));
		}
		return recipes;
	}

}
