package p455w0rd.morphtweaks.init;

import java.util.Arrays;

import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.EnumHelper;

/**
 * @author p455w0rd
 *
 */
public class ModRarities {

	private static final NonNullList<Enum<?>> RARITY_LIST = NonNullList.<Enum<?>>create();

	public static final Rarity EPIC = EnumHelper.addEnum(Rarity.class, ModGlobals.MODID + ":epic", new Class<?>[] {
			Integer.class
	}, 0);

	public static NonNullList<Enum<?>> getList() {
		if (RARITY_LIST.isEmpty()) {
			RARITY_LIST.addAll(Arrays.asList(EPIC));
		}
		return RARITY_LIST;
	}

}