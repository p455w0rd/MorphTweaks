package p455w0rd.morphtweaks.blocks.tiles;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import p455w0rd.morphtweaks.api.IEnchanterRecipe;
import p455w0rd.morphtweaks.container.ContainerEnchanter;
import p455w0rd.morphtweaks.init.ModRecipes;
import p455w0rd.morphtweaks.inventory.InventoryEnchanter;

/**
 * @author p455w0rd
 *
 */
public class TileEnchanter extends TileEntity implements ITickable {

	private static final String TAG_SLOT = "Slot";
	private static final String TAG_NAME = "Enchanter";
	private static final String TAG_INVENTORY = "Inventory";

	public int tickCount;
	public float pageFlip;
	public float pageFlipPrev;
	public float flipT;
	public float flipA;
	public float bookSpread;
	public float bookSpreadPrev;
	public float bookRotation;
	public float bookRotationPrev;
	public float tRot;
	private static final Random rand = new Random();

	private SidedInvWrapper itemHandler;
	private InventoryEnchanter inventory;

	public TileEnchanter() {
		inventory = new InventoryEnchanter(this);
		itemHandler = new SidedInvWrapper(inventory, null);
	}

	public SidedInvWrapper getItemHandler() {
		return itemHandler;
	}

	public InventoryEnchanter getInventory() {
		return inventory;
	}

	public ItemStack getOutputStack() {
		return inventory.getStackInSlot(3);
	}

	public void setOutputStack(ItemStack stack) {
		inventory.setInventorySlotContents(3, stack);
	}

	public ItemStack getRecipeItem() {
		return inventory.getStackInSlot(1);
	}

	public ItemStack getBookItem() {
		return inventory.getStackInSlot(0);
	}

	public ItemStack getLapisItem() {
		return inventory.getStackInSlot(2);
	}

	public IEnchanterRecipe getCurrentRecipe() {
		IEnchanterRecipe recipe = ModRecipes.getEnchanterRecipeByRecipeItemAndLapis(getRecipeItem(), getLapisItem());
		return recipe != null && recipe.matches(getInventory()) ? recipe : null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler()) : super.getCapability(capability, facing);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList nbtList = new NBTTagList();
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			if (!itemHandler.getStackInSlot(i).isEmpty()) {
				NBTTagCompound slotNBT = new NBTTagCompound();
				slotNBT.setInteger(TAG_SLOT, i);
				itemHandler.getStackInSlot(i).writeToNBT(slotNBT);
				nbtList.appendTag(slotNBT);
			}
		}
		nbt.setTag(TAG_INVENTORY, nbtList);
		compound.setTag(TAG_NAME, nbt);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		NBTTagCompound nbt = compound.getCompoundTag(TAG_NAME);
		NBTTagList tagList = nbt.getTagList(TAG_INVENTORY, 10);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound slotNBT = tagList.getCompoundTagAt(i);
			if (slotNBT != null) {
				itemHandler.setStackInSlot(slotNBT.getInteger(TAG_SLOT), new ItemStack(slotNBT));
			}
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void update() {
		if (getCurrentRecipe() != null) {
			//if (getOutputStack().isEmpty()) {
			if (!getCurrentRecipe().getCraftingResult(getInventory()).isEmpty()) {
				setOutputStack(getCurrentRecipe().getCraftingResult(getInventory()));
				markDirty();
			}
			//}
		}
		else {
			if (!getOutputStack().isEmpty()) {
				setOutputStack(ItemStack.EMPTY);
				markDirty();
			}
		}
		bookSpreadPrev = bookSpread;
		bookRotationPrev = bookRotation;
		EntityPlayer entityplayer = world.getClosestPlayer(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 3.0D, false);

		if (entityplayer != null) {
			double d0 = entityplayer.posX - (pos.getX() + 0.5F);
			double d1 = entityplayer.posZ - (pos.getZ() + 0.5F);
			tRot = (float) MathHelper.atan2(d1, d0);
			bookSpread += 0.1F;

			if (bookSpread < 0.5F || rand.nextInt(40) == 0) {
				float f1 = flipT;

				while (true) {
					flipT += rand.nextInt(4) - rand.nextInt(4);

					if (f1 != flipT) {
						break;
					}
				}
			}
		}
		else {
			tRot += 0.02F;
			bookSpread -= 0.1F;
		}

		while (bookRotation >= (float) Math.PI) {
			bookRotation -= ((float) Math.PI * 2F);
		}

		while (bookRotation < -(float) Math.PI) {
			bookRotation += ((float) Math.PI * 2F);
		}

		while (tRot >= (float) Math.PI) {
			tRot -= ((float) Math.PI * 2F);
		}

		while (tRot < -(float) Math.PI) {
			tRot += ((float) Math.PI * 2F);
		}

		float f2;

		for (f2 = tRot - bookRotation; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) {
			;
		}

		while (f2 < -(float) Math.PI) {
			f2 += ((float) Math.PI * 2F);
		}

		bookRotation += f2 * 0.4F;
		bookSpread = MathHelper.clamp(bookSpread, 0.0F, 1.0F);
		++tickCount;
		pageFlipPrev = pageFlip;
		float f = (flipT - pageFlip) * 0.4F;
		f = MathHelper.clamp(f, -0.2F, 0.2F);
		flipA += (f - flipA) * 0.9F;
		pageFlip += flipA;
	}

	public ContainerEnchanter getContainerForPlayer(EntityPlayer player) {
		return new ContainerEnchanter(player, this);
	}

}
