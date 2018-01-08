package p455w0rd.morphtweaks.enchantments;

import javax.annotation.Nonnull;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import p455w0rd.morphtweaks.init.ModEnchantments;
import p455w0rd.morphtweaks.init.ModGlobals;
import p455w0rd.morphtweaks.init.ModEnchantments.EnchantmentTypes;

/**
 * @author p455w0rd
 *
 */
public class EnchantmentSoulBound extends Enchantment {

	public EnchantmentSoulBound() {
		super(Rarity.VERY_RARE, EnchantmentTypes.SOUL_BOUND, EntityEquipmentSlot.values());
		setName(ModGlobals.MODID + ".soul_bound.name");
		setRegistryName("soul_bound");
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 15;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	public static boolean isSoulBound(@Nonnull ItemStack item) {
		return EnchantmentHelper.getEnchantmentLevel(ModEnchantments.SOUL_BOUND, item) > 0;
	}

	public static boolean isSoulBoundEnchantedBook(@Nonnull ItemStack book) {
		String subNBTKey = "StoredEnchantments";
		if (book.hasTagCompound() && book.getTagCompound().hasKey(subNBTKey, Constants.NBT.TAG_LIST)) {
			NBTTagList subNBTList = book.getTagCompound().getTagList(subNBTKey, Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < subNBTList.tagCount(); i++) {
				NBTTagCompound subNBT = subNBTList.getCompoundTagAt(i);
				if (subNBT != null) {
					int enchantmentID = Enchantment.getEnchantmentID(ModEnchantments.SOUL_BOUND);
					if (subNBT.getShort("id") == enchantmentID) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
