package p455w0rd.morphtweaks.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import net.minecraftforge.registries.RegistryBuilder;
import p455w0rd.morphtweaks.api.IEnchanterRecipe;

/**
 * @author p455w0rd
 *
 */
public class ModRegistries {

	public static final ResourceLocation ENCHANTER_RECIPE_REGISTRY_NAME = new ResourceLocation(ModGlobals.MODID, "enchatner_recipes_registry");
	public static IForgeRegistry<IEnchanterRecipe> ENCHANTER_RECIPES;

	public static void buildRegistries() {
		if (ENCHANTER_RECIPES == null) {
			RegistryBuilder<IEnchanterRecipe> builder = new RegistryBuilder<>();
			builder.setName(ENCHANTER_RECIPE_REGISTRY_NAME).disableSaving();
			builder.setType(IEnchanterRecipe.class).allowModification();
			IForgeRegistry<IEnchanterRecipe> registry = builder.create();
			if (!(registry instanceof IForgeRegistryModifiable)) {
				throw new IllegalArgumentException("Forge registry builder didn't build a modifiable registry! Did something in Forge change internally?");
			}
			ENCHANTER_RECIPES = registry;
		}
	}

}
