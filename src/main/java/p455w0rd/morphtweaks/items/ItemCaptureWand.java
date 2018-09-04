package p455w0rd.morphtweaks.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.morphtweaks.api.IModelHolder;
import p455w0rd.morphtweaks.init.ModConfig.Options;
import p455w0rd.morphtweaks.init.ModCreativeTab;
import p455w0rd.morphtweaks.init.ModGlobals;
import p455w0rd.morphtweaks.util.MTweaksUtil;

/**
 * @author p455w0rd
 *
 */
public class ItemCaptureWand extends Item implements IModelHolder {

	private static final String NAME = "capture_wand";

	public ItemCaptureWand() {
		setRegistryName(ModGlobals.MODID + ":" + NAME);
		setUnlocalizedName(NAME);
		addPropertyOverride(new ResourceLocation("hasmob"), (stack, world, entity) -> MTweaksUtil.doesWandHaveMobStored(stack) ? 1 : 0);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab == ModCreativeTab.TAB || tab == CreativeTabs.SEARCH) {
			items.add(new ItemStack(this));
			items.addAll(ModCreativeTab.getWandList());
		}
	}

	@Override
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(new ResourceLocation(ModGlobals.MODID, NAME), "inventory"));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand == EnumHand.MAIN_HAND && player != null) {
			ItemStack wand = player.getHeldItemMainhand();
			if (world.isRemote) {
				player.swingArm(hand);
			}
			if (!world.isRemote) {
				MTweaksUtil.spawnMobFromWand(wand, world, facing == EnumFacing.UP ? pos.up(1) : pos.offset(facing), player.capabilities.isCreativeMode);
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		ResourceLocation mob = MTweaksUtil.getCapturedMobInWand(stack);
		if (mob == null) {
			String mobType = Options.DISABLE_CAPTURING_HOSTILE_MOBS ? I18n.translateToLocal("tooltip.a_friendly") : I18n.translateToLocal("tooltip.any");
			tooltip.add(I18n.translateToLocal("tooltip.right_click") + " " + mobType + " " + I18n.translateToLocal("tooltip.mob_to_capture"));
		}
		else {
			tooltip.add(I18n.translateToLocal("tooltip.captured_mob") + ": " + MTweaksUtil.getMobName(stack));
			if (MTweaksUtil.isVillager(MTweaksUtil.getCapturedMobInWand(stack))) {
				String profession = MTweaksUtil.getCapturedVillagerProfession(stack);
				if (!profession.isEmpty()) {
					tooltip.add(profession);
				}
			}
		}
	}

}
