package p455w0rd.morphtweaks.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import p455w0rd.morphtweaks.blocks.tiles.TileEnchanter;
import p455w0rd.morphtweaks.init.ModRecipes;

/**
 * @author p455w0rd
 *
 */
public class ContainerEnchanter extends Container {

	private TileEnchanter enchanter;

	Slot bookSlot;
	Slot itemSlot;
	Slot lapisSlot;
	Slot outputSlot;

	public ContainerEnchanter(EntityPlayer player, TileEnchanter tile) {
		enchanter = tile;
		addSlotToContainer(bookSlot = new SlotEnchanter(tile, player, 0, 19, 25));
		addSlotToContainer(itemSlot = new SlotEnchanter(tile, player, 1, 80, 25));
		addSlotToContainer(lapisSlot = new SlotEnchanter(tile, player, 2, 102, 25));
		addSlotToContainer(outputSlot = new SlotEnchanter(tile, player, 3, 167, 25));
		bindPlayerInventory(player.inventory, 9, 55);
	}

	public TileEnchanter getEnchanter() {
		return enchanter;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	protected void bindPlayerInventory(final InventoryPlayer inventoryPlayer, final int offsetX, final int offsetY) {
		IItemHandler ih = new PlayerInvWrapper(inventoryPlayer);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new SlotItemHandler(ih, j + i * 9 + 9, j * 20 + offsetX + j, offsetY + i * 20 + i));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new SlotItemHandler(ih, i, i * 20 + offsetX + i, offsetY + 67));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		Slot clickSlot = inventorySlots.get(index);
		ItemStack returnStack = ItemStack.EMPTY;
		if (clickSlot.getHasStack()) {
			ItemStack slotStack = clickSlot.getStack();
			if (isEnchanterSlot(clickSlot)) {
				if (!mergeItemStack(slotStack, 4, 40, true)) {
					returnStack = slotStack.copy();
				}
				else {
					if (index == 3) {
						enchanter.getCurrentRecipe().craftItem(enchanter.getInventory(), player);
					}
				}
			}
			else {
				if (slotStack.getItem() == Items.WRITABLE_BOOK && !inventorySlots.get(0).getHasStack()) {
					if (!tryMerge(clickSlot, inventorySlots.get(0))) {
						returnStack = slotStack.copy();
					}
				}
				else if (ModRecipes.isValidEnchanterRecipeItem(slotStack) && slotStack.getCount() <= inventorySlots.get(1).getSlotStackLimit() && slotStack.getCount() <= slotStack.getMaxStackSize()) {
					if (!tryMerge(clickSlot, inventorySlots.get(1))) {
						returnStack = slotStack.copy();
					}
				}
				else if (slotStack.getItem() == Items.DYE && slotStack.getItemDamage() == 4 && slotStack.getCount() <= inventorySlots.get(2).getSlotStackLimit() && slotStack.getCount() <= slotStack.getMaxStackSize()) {
					if (!tryMerge(clickSlot, inventorySlots.get(2))) {
						returnStack = slotStack.copy();
					}
				}
			}
		}
		return returnStack;
	}

	private boolean tryMerge(Slot slotIn, Slot destSlot) {
		if (slotIn != null && destSlot != null && slotIn.getHasStack()) {
			if (!destSlot.getHasStack()) {
				destSlot.putStack(slotIn.getStack().copy());
				slotIn.putStack(ItemStack.EMPTY);
				return true;
			}
			if (ItemStack.areItemsEqualIgnoreDurability(slotIn.getStack().copy(), slotIn.getStack().copy())) {
				int destAvailable = destSlot.getStack().getMaxStackSize() - destSlot.getStack().getCount();
				if (destAvailable > 0) { //there is room
					int sizeIn = slotIn.getStack().getCount();
					if (sizeIn <= destAvailable) {
						destSlot.getStack().grow(sizeIn);
						slotIn.putStack(ItemStack.EMPTY);
						return true;
					}
					slotIn.getStack().shrink(destAvailable);
					destSlot.getStack().grow(destAvailable);
					return true;
				}
			}
		}
		return false;
	}

	public IInventory getEnchanterInventory() {
		return enchanter != null ? enchanter.getInventory() : null;
	}

	private boolean isOutputSlot(Slot slot) {
		return slot.getSlotIndex() == 3;
	}

	private boolean isEnchanterSlot(Slot slot) {
		return slot instanceof SlotEnchanter;
	}

}
