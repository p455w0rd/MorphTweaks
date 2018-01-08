package p455w0rd.morphtweaks.container;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author p455w0rd
 *
 */
public class ContainerVoidifier extends Container {

	IItemHandler playerInventory;
	IItemHandler trashInventory;
	IFluidHandler fluidInventory;

	public ContainerVoidifier(IItemHandler playerInv, IItemHandler trashInv, IFluidHandler fluidInv, EntityPlayer player) {
		playerInventory = playerInv;
		trashInventory = trashInv;
		fluidInventory = fluidInv;

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new SlotItemHandler(playerInventory, i, i * 20 + (9 + i), 90 + (3 - 1) + (3 * 20 + 6)));
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new SlotItemHandler(playerInventory, j + i * 9 + 9, j * 20 + (9 + j), 149 + (3 - 1) + i - (6 - 3) * 20 + i * 20));
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new SlotItemHandler(trashInventory, j + i * 9, j * 20 + (9 + j), 19 + i + i * 20));
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot clickSlot = inventorySlots.get(index);
		if (clickSlot.getHasStack()) {
			if (!isTrashSlot(index)) {
				if (getNextAvailableSlot(inventorySlots) == -1) {
					if (!moveStackWithinInventory(clickSlot.getStack(), index, inventorySlots)) {
						return ItemStack.EMPTY;
					}
					return clickSlot.getStack();
				}
				clickSlot.putStack(ItemStack.EMPTY);
				playerIn.inventory.markDirty();
			}
			else {
				if (moveStackToInventory(clickSlot.getStack(), inventorySlots)) {
					clickSlot.putStack(ItemStack.EMPTY);
				}
			}
		}
		return ItemStack.EMPTY;
	}

	private boolean isTrashSlot(int index) {
		return index >= 36;
	}

	private int getNextAvailableSlot(List<Slot> inventorySlots) {
		for (int i = 36; i <= 63; i++) {
			Slot s = inventorySlots.get(i);
			if ((s != null) && (s.getStack().isEmpty())) {
				return i;
			}
		}
		return -1;
	}

	private boolean moveStackToInventory(ItemStack itemStackIn, List<Slot> inventorySlots) {
		for (int i = 0; i <= 36; i++) {
			Slot possiblyOpenSlot = inventorySlots.get(i);
			if (!possiblyOpenSlot.getHasStack()) {
				possiblyOpenSlot.putStack(itemStackIn);
				return true;
			}
		}
		return false;
	}

	private boolean moveStackWithinInventory(ItemStack itemStackIn, int index, List<Slot> inventorySlots) {
		if (isInHotbar(index)) {
			for (int i = 9; i <= 36; i++) {
				Slot possiblyOpenSlot = inventorySlots.get(i);
				if (!possiblyOpenSlot.getHasStack()) {
					possiblyOpenSlot.putStack(itemStackIn);
					inventorySlots.get(index).putStack(ItemStack.EMPTY);
					return true;
				}
			}
		}
		else if (isInInventory(index)) {
			for (int i = 0; i <= 8; i++) {
				Slot possiblyOpenSlot = inventorySlots.get(i);
				if (!possiblyOpenSlot.getHasStack()) {
					possiblyOpenSlot.putStack(itemStackIn);
					inventorySlots.get(index).putStack(ItemStack.EMPTY);
					return true;
				}
			}
		}
		return false;
	}

	private boolean isInHotbar(int index) {
		return (index >= 0) && (index <= 8);
	}

	private boolean isInInventory(int index) {
		return (index >= 9) && (index <= 36);
	}

}
