package p455w0rd.morphtweaks.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import p455w0rd.morphtweaks.blocks.tiles.TileEnchanter;

/**
 * @author p455w0rd
 *
 */
public class SlotEnchanter extends Slot {

	TileEnchanter enchanter;
	EntityPlayer player;

	public SlotEnchanter(@Nonnull TileEnchanter enchanter, EntityPlayer player, int index, int xPosition, int yPosition) {
		super(enchanter.getInventory(), index, xPosition, yPosition);
		this.enchanter = enchanter;
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return inventory.isItemValidForSlot(getSlotIndex(), stack);
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
		if (getSlotIndex() == 3 && enchanter.getCurrentRecipe() != null) {
			enchanter.getCurrentRecipe().craftItem(enchanter.getInventory(), player);
		}
		return stack;
	}

}
