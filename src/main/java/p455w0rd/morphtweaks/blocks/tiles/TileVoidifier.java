package p455w0rd.morphtweaks.blocks.tiles;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author p455w0rd
 *
 */
public class TileVoidifier extends TileEntity {

	public IFluidHandler invFluid = new IFluidHandler() {

		@Override
		public IFluidTankProperties[] getTankProperties() {
			return new IFluidTankProperties[] {
					new FluidTankProperties(null, Integer.MAX_VALUE, true, false),
					new FluidTankProperties(null, Integer.MAX_VALUE, true, false),
					new FluidTankProperties(null, Integer.MAX_VALUE, true, false),
					new FluidTankProperties(null, Integer.MAX_VALUE, true, false),
					new FluidTankProperties(null, Integer.MAX_VALUE, true, false),
					new FluidTankProperties(null, Integer.MAX_VALUE, true, false),
					new FluidTankProperties(null, Integer.MAX_VALUE, true, false)
			};
		}

		@Override
		public int fill(FluidStack fluid, boolean doFill) {
			return fluid != null ? fluid.amount : 0;
		}

		@Override
		@Nullable
		public FluidStack drain(FluidStack fluid, boolean doDrain) {
			return null;
		}

		@Override
		@Nullable
		public FluidStack drain(int maxDrain, boolean doDrain) {
			return null;
		}
	};

	public ItemStackHandler invItem = new ItemStackHandler(27) {

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return ItemStack.EMPTY;
		}
	};

	public EnergyStorage storageFE = new EnergyStorage(Integer.MAX_VALUE) {

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return maxReceive;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return 0;
		}

		@Override
		public int getEnergyStored() {
			return 0;
		}

		@Override
		public int getMaxEnergyStored() {
			return capacity;
		}

		@Override
		public boolean canExtract() {
			return false;
		}

		@Override
		public boolean canReceive() {
			return true;
		}
	};

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(invItem);
		}
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(invFluid);
		}
		else if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(storageFE);
		}
		return super.getCapability(capability, facing);
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

}
