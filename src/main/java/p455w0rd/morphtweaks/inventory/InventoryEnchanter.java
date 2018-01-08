package p455w0rd.morphtweaks.inventory;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import p455w0rd.morphtweaks.blocks.tiles.TileEnchanter;
import p455w0rd.morphtweaks.init.ModRecipes;

/**
 * @author p455w0rd
 *
 */
public class InventoryEnchanter implements ISidedInventory {

	NonNullList<ItemStack> inventoryList = NonNullList.withSize(4, ItemStack.EMPTY);
	TileEnchanter enchanter;

	public InventoryEnchanter(@Nonnull TileEnchanter enchanterIn) {
		enchanter = enchanterIn;
	}

	@Override
	public int getSizeInventory() {
		return inventoryList.size();
	}

	@Override
	public boolean isEmpty() {
		return inventoryList.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventoryList.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(inventoryList, index, count);
		if (!itemstack.isEmpty()) {
			enchanter.markDirty();
		}
		return itemstack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = ItemStackHelper.getAndRemove(inventoryList, index);
		enchanter.markDirty();
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		inventoryList.set(index, stack);
		enchanter.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 0 && stack.getItem() == Items.WRITABLE_BOOK) {
			return true;
		}
		else if (index == 1 && ModRecipes.isValidEnchanterRecipeItem(stack)) {
			return true;
		}
		else if (index == 2 && stack.getItem() == Items.DYE && stack.getItemDamage() == 4) {
			return true;
		}
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		inventoryList.clear();
	}

	@Override
	public String getName() {
		return "advanced_enchanter";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == null) {
			return new int[] {
					0,
					1,
					2,
					3
			};
		}
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

}
