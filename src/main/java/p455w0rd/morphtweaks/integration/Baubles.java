package p455w0rd.morphtweaks.integration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.container.SlotBauble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author p455w0rd
 *
 */
public class Baubles {

	public static IBaublesItemHandler getBaubles(EntityPlayer player) {
		if (player.hasCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null)) {
			return player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
		}
		return null;
	}

	public static void setBaublesItemStack(EntityPlayer player, int slot, ItemStack stack) {
		if (player.hasCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null)) {
			IBaublesItemHandler baubles = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
			baubles.setStackInSlot(slot, stack);
		}
	}

	public static boolean isBauble(ItemStack stack) {
		return stack.getItem() instanceof IBauble || stack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
	}

	public static boolean isBaubleSlot(Slot slot) {
		return slot instanceof SlotBauble;
	}

	public static BaubleType getBaubleType(ItemStack stack) {
		if (isBauble(stack)) {
			if (stack.getItem() instanceof IBauble) {
				return ((IBauble) stack.getItem()).getBaubleType(stack);
			}
			if (stack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
				return stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).getBaubleType(stack);
			}
		}
		return null;
	}

	public static boolean addToBaublesInventory(EntityPlayer player, ItemStack stack) {
		if (isBauble(stack)) {
			List<Integer> validSlots = Arrays.stream(getBaubleType(stack).getValidSlots()).boxed().collect(Collectors.toList());
			IBaublesItemHandler inv = getBaubles(player);
			for (int i = 0; i < getBaubles(player).getSlots(); i++) {
				if (validSlots.contains(i)) {
					getBaubles(player).setStackInSlot(i, stack);
					return true;
				}
			}
		}
		return false;
	}

}
