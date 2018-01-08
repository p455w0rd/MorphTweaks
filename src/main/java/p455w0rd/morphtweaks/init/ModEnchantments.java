package p455w0rd.morphtweaks.init;

import java.util.Arrays;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.EnumHelper;
import p455w0rd.morphtweaks.enchantments.EnchantmentSoulBound;

/**
 * @author p455w0rd
 *
 */
public class ModEnchantments {

	private static final NonNullList<Enchantment> ENCHANTMENT_LIST = NonNullList.<Enchantment>create();
	public static final Enchantment SOUL_BOUND = new EnchantmentSoulBound();

	public static NonNullList<Enchantment> getList() {
		if (ENCHANTMENT_LIST.isEmpty()) {
			ENCHANTMENT_LIST.addAll(Arrays.asList(SOUL_BOUND));
		}
		return ENCHANTMENT_LIST;
	}

	public static class EnchantmentTypes {

		public static final EnumEnchantmentType SOUL_BOUND = EnumHelper.addEnchantmentType(ModGlobals.MODID + ":soul_bound", itemIn -> true);

	}

}
