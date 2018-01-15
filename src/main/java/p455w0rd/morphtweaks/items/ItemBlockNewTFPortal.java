package p455w0rd.morphtweaks.items;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import p455w0rd.morphtweaks.integration.TwilightForest;

/**
 * @author p455w0rd
 *
 */
public class ItemBlockNewTFPortal extends ItemBlock {

	public ItemBlockNewTFPortal(Block block) {
		super(block);
		setRegistryName(block.getRegistryName());
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (!block.isReplaceable(worldIn, pos)) {
			pos = pos.offset(facing);
		}

		ItemStack itemstack = player.getHeldItem(hand);

		if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(this.block, pos, false, facing, (Entity) null)) {
			int i = this.getMetadata(itemstack.getMetadata());
			IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);
			iblockstate1 = worldIn.getBlockState(pos);
			SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
			worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			TwilightForest.placePortal(worldIn, pos, facing, player.getAdjustedHorizontalFacing());
			itemstack.shrink(1);

			return EnumActionResult.SUCCESS;
		}
		else {
			return EnumActionResult.FAIL;
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return block.getUnlocalizedName().replace("tile.", "item.");
	}

	@Override
	public String getUnlocalizedName() {
		return block.getUnlocalizedName();
	}

}
