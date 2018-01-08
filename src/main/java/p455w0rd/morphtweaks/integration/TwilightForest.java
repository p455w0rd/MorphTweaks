package p455w0rd.morphtweaks.integration;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import p455w0rd.morphtweaks.init.ModLogger;
import p455w0rd.morphtweaks.util.MTweaksUtil;
import p455w0rd.morphtweaks.util.MorphTFTeleporter;
import twilightforest.TFConfig;
import twilightforest.advancements.TFAdvancements;

/**
 * @author p455w0rd
 *
 */
public class TwilightForest {

	public static String TF_PORTAL_DISABLED_STRING = "";
	public static String TF_PORTAL_DISABLED_STRING_2 = "";

	public static void postInit() {
		TFConfig.disablePortalCreation = true;
	}

	public static void checkForPortalCreation(EntityPlayer player, World world, float rangeToCheck) {
		if (world.provider.getDimension() == 0 || world.provider.getDimension() == TFConfig.dimension.dimensionID || TFConfig.allowPortalsInOtherDimensions) {
			Item item = Item.REGISTRY.getObject(new ResourceLocation(TFConfig.portalCreationItem));
			int metadata = TFConfig.portalCreationMeta;
			if (item == null) {
				item = Items.DIAMOND;
				metadata = -1;
			}
			ItemStack itemStack = new ItemStack(item, 1, metadata);
			final List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, player.getEntityBoundingBox().grow(rangeToCheck, rangeToCheck, rangeToCheck));

			for (final EntityItem entityItem : itemList) {
				if (item == entityItem.getItem().getItem() && (metadata == -1 || entityItem.getItem().getMetadata() == metadata)) {
					if (world.isMaterialInBB(entityItem.getEntityBoundingBox(), Material.WATER) && MTweaksUtil.getFluidBlock() != Blocks.WATER) {
						TF_PORTAL_DISABLED_STRING = TextFormatting.RED + "" + TextFormatting.BOLD + "Default TF Portal Creation disabled";
						Block fluidBlock = MTweaksUtil.getFluidBlock();
						ItemStack blockStack = new ItemStack(fluidBlock);
						String blockName = blockStack.getDisplayName();
						if (blockName.trim().equals("Air")) {
							if (fluidBlock instanceof IFluidBlock) {
								blockName = I18n.translateToLocal(((IFluidBlock) fluidBlock).getFluid().getUnlocalizedName());
							}
						}
						TF_PORTAL_DISABLED_STRING_2 = TextFormatting.RED + "" + TextFormatting.BOLD + "You must throw a " + itemStack.getDisplayName() + " into a 2x2 pool of " + blockName;

					}
					else if (world.getBlockState(new BlockPos(entityItem)).getBlock() == MTweaksUtil.getFluidBlock()) {

						Random rand = new Random();
						for (int k = 0; k < 2; k++) {
							double d = rand.nextGaussian() * 0.02D;
							double d1 = rand.nextGaussian() * 0.02D;
							double d2 = rand.nextGaussian() * 0.02D;

							world.spawnParticle(EnumParticleTypes.SPELL, entityItem.posX, entityItem.posY + 0.2, entityItem.posZ, d, d1, d2);
						}

						if (MTweaksUtil.tryToCreatePortal(world, new BlockPos(entityItem), entityItem)) {
							TFAdvancements.MADE_TF_PORTAL.trigger((EntityPlayerMP) player);
							return;
						}
					}
					return;
				}
			}
			if (!TF_PORTAL_DISABLED_STRING.isEmpty()) {
				TF_PORTAL_DISABLED_STRING = "";
				TF_PORTAL_DISABLED_STRING_2 = "";
			}
		}
	}

	public static void sendToTwilightForest(EntityPlayerMP playerMP) {
		if (playerMP.timeUntilPortal > 0) {
			// do not switch dimensions if the player has any time on this thinger
			playerMP.timeUntilPortal = 10;
		}
		else {

			// send to twilight
			if (playerMP.dimension != TFConfig.dimension.dimensionID) {
				if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(playerMP, TFConfig.dimension.dimensionID)) {
					return;
				}

				//PlayerHelper.grantAdvancement(playerMP, new ResourceLocation(TwilightForestMod.ID, "twilight_portal"));
				ModLogger.debug("Player touched the portal block.  Sending the player to dimension " + TFConfig.dimension.dimensionID);

				playerMP.mcServer.getPlayerList().transferPlayerToDimension(playerMP, TFConfig.dimension.dimensionID, MorphTFTeleporter.getTeleporterForDim(playerMP.mcServer, TFConfig.dimension.dimensionID));

				// set respawn point for TF dimension to near the arrival portal
				playerMP.setSpawnChunk(new BlockPos(playerMP), true, TFConfig.dimension.dimensionID);
			}
			else {
				if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(playerMP, 0)) {
					return;
				}

				playerMP.mcServer.getPlayerList().transferPlayerToDimension(playerMP, 0, MorphTFTeleporter.getTeleporterForDim(playerMP.mcServer, 0));
			}
		}
	}

	public static ResourceLocation getPortalCreationItem() {
		return new ResourceLocation(TFConfig.portalCreationItem);
	}

	public static int getTFDimensionID() {
		return TFConfig.dimension.dimensionID;
	}

}
