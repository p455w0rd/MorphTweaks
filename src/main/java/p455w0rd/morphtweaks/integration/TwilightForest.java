package p455w0rd.morphtweaks.integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import p455w0rd.morphtweaks.init.ModBlocks;
import p455w0rd.morphtweaks.init.ModConfig.Options;
import p455w0rd.morphtweaks.init.ModLogger;
import p455w0rd.morphtweaks.util.ArrayGen;
import p455w0rd.morphtweaks.util.MTweaksUtil;
import p455w0rd.morphtweaks.util.MorphTFTeleporter;
import twilightforest.TFConfig;
import twilightforest.TFTeleporter;
import twilightforest.advancements.TFAdvancements;

/**
 * @author p455w0rd
 *
 */
public class TwilightForest {

	public static String TF_PORTAL_DISABLED_STRING = "";
	public static String TF_PORTAL_DISABLED_STRING_2 = "";
	public static List<Block> NATURE_BLOCKS = Lists.<Block>newArrayList();
	public static List<Block> PLACEABLE_NATURE_BLOCKS = Lists.<Block>newArrayList();
	public static ArrayGen PORTAL_RECIPE = null;

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
					if (world.isMaterialInBB(entityItem.getEntityBoundingBox(), Material.WATER) && getFluidBlock() != Blocks.WATER) {
						TF_PORTAL_DISABLED_STRING = TextFormatting.RED + "" + TextFormatting.BOLD + "Default TF Portal Creation disabled";
						Block fluidBlock = getFluidBlock();
						ItemStack blockStack = new ItemStack(fluidBlock);
						String blockName = blockStack.getDisplayName();
						if (blockName.trim().equals("Air")) {
							if (fluidBlock instanceof IFluidBlock) {
								blockName = I18n.translateToLocal(((IFluidBlock) fluidBlock).getFluid().getUnlocalizedName());
							}
						}
						TF_PORTAL_DISABLED_STRING_2 = TextFormatting.RED + "" + TextFormatting.BOLD + "You must throw a " + itemStack.getDisplayName() + " into a 2x2 pool of " + blockName;

					}
					else if (world.getBlockState(new BlockPos(entityItem)).getBlock() == getFluidBlock()) {

						Random rand = new Random();
						for (int k = 0; k < 2; k++) {
							double d = rand.nextGaussian() * 0.02D;
							double d1 = rand.nextGaussian() * 0.02D;
							double d2 = rand.nextGaussian() * 0.02D;

							world.spawnParticle(EnumParticleTypes.SPELL, entityItem.posX, entityItem.posY + 0.2, entityItem.posZ, d, d1, d2);
						}

						if (tryToCreatePortal(world, new BlockPos(entityItem), entityItem)) {
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

	public static boolean isNatureBlock(World world, BlockPos pos) {
		return isNatureBlock(world.getBlockState(pos));
	}

	private static boolean isNatureBlock(IBlockState state) {
		Material mat = state.getMaterial();
		return mat == Material.PLANTS || mat == Material.VINE || mat == Material.LEAVES;
	}

	public static List<Block> getNatureBlocks() {
		if (NATURE_BLOCKS.isEmpty()) {
			for (Block block : ForgeRegistries.BLOCKS.getValues()) {
				if (block != null && isNatureBlock(block.getDefaultState())) {
					NATURE_BLOCKS.add(block);
				}
			}
		}
		return NATURE_BLOCKS;
	}

	public static List<Block> getPlaceableNatureBlocks(World world) {
		if (PLACEABLE_NATURE_BLOCKS.isEmpty()) {
			for (Block block : getNatureBlocks()) {
				if (block.getRegistryName().getResourceDomain().equals("minecraft") && block instanceof IPlantable) {
					IPlantable plant = (IPlantable) block;
					if (plant.getPlantType(world, new BlockPos(0, 0, 0)) == EnumPlantType.Plains) {
						PLACEABLE_NATURE_BLOCKS.add(block);
					}
				}
			}
		}
		return PLACEABLE_NATURE_BLOCKS;
	}

	public static Block getRandomNatureBlock(World world) {
		return getPlaceableNatureBlocks(world).get(world.rand.nextInt(getPlaceableNatureBlocks(world).size()));
	}

	public static boolean isGrassOrDirt(World world, BlockPos pos) {
		return isGrassOrDirt(world.getBlockState(pos));
	}

	public static boolean isGrassOrDirt(IBlockState state) {
		return state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND;
	}

	public static void createPortal(World world, BlockPos pos) {
		if (world.getBlockState(pos.west()).getBlock() == getFluidBlock()) {
			pos = pos.west();
		}
		if (world.getBlockState(pos.north()).getBlock() == getFluidBlock()) {
			pos = pos.north();
		}

		world.setBlockState(pos, ModBlocks.NEW_TF_PORTAL.getDefaultState(), 2);
		world.setBlockState(pos.east(), ModBlocks.NEW_TF_PORTAL.getDefaultState(), 2);
		world.setBlockState(pos.east().south(), ModBlocks.NEW_TF_PORTAL.getDefaultState(), 2);
		world.setBlockState(pos.south(), ModBlocks.NEW_TF_PORTAL.getDefaultState(), 2);
	}

	public static boolean tryToCreatePortal(World world, BlockPos pos, EntityItem activationItem) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == getFluidBlock()) {
			HashMap<BlockPos, Boolean> blocksChecked = new HashMap<>();
			blocksChecked.put(pos, true);

			PassableNumber number = new PassableNumber(64);

			if (recursivelyValidatePortal(world, pos, blocksChecked, number) && number.getNumber() > 3) {
				activationItem.getItem().shrink(1);
				world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), true));

				for (Map.Entry<BlockPos, Boolean> checkedPos : blocksChecked.entrySet()) {
					if (checkedPos.getValue()) {
						world.setBlockState(checkedPos.getKey(), ModBlocks.NEW_TF_PORTAL.getDefaultState(), 2);
					}
				}
				return true;
			}
		}
		return false;
	}

	private static boolean recursivelyValidatePortal(World world, BlockPos pos, HashMap<BlockPos, Boolean> blocksChecked, PassableNumber fluidLimit) {
		boolean isPoolProbablyEnclosed = true;

		fluidLimit.decrement();
		if (fluidLimit.getNumber() < 0) {
			return false;
		}

		for (int i = 0; i < EnumFacing.HORIZONTALS.length && fluidLimit.getNumber() >= 0; i++) {
			BlockPos positionCheck = pos.offset(EnumFacing.HORIZONTALS[i]);

			if (!blocksChecked.containsKey(positionCheck)) {
				IBlockState state = world.getBlockState(positionCheck);

				if (state == getFluidBlock().getDefaultState() && world.getBlockState(positionCheck.down()).getMaterial().isSolid()) {
					blocksChecked.put(positionCheck, true);

					isPoolProbablyEnclosed = isPoolProbablyEnclosed && recursivelyValidatePortal(world, positionCheck, blocksChecked, fluidLimit);
				}
				else if (isGrassOrDirt(state) && isNatureBlock(world.getBlockState(positionCheck.up()))) {
					blocksChecked.put(positionCheck, false);
				}
				else {
					return false;
				}
			}
		}

		return isPoolProbablyEnclosed;
	}

	public static void changeDimension(Entity toTeleport, int dimensionIn) {
		if (!toTeleport.world.isRemote && !toTeleport.isDead) {
			if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(toTeleport, dimensionIn)) {
				return;
			}
			toTeleport.world.profiler.startSection("changeDimension");
			MinecraftServer minecraftserver = toTeleport.getServer();
			int i = toTeleport.dimension;
			WorldServer worldserver = minecraftserver.getWorld(i);
			WorldServer worldserver1 = minecraftserver.getWorld(dimensionIn);
			toTeleport.dimension = dimensionIn;

			if (i == 1 && dimensionIn == 1) {
				worldserver1 = minecraftserver.getWorld(0);
				toTeleport.dimension = 0;
			}

			toTeleport.world.removeEntity(toTeleport);
			toTeleport.isDead = false;
			toTeleport.world.profiler.startSection("reposition");
			BlockPos blockpos;

			if (dimensionIn == 1) {
				blockpos = worldserver1.getSpawnCoordinate();
			}
			else {
				double d0 = toTeleport.posX;
				double d1 = toTeleport.posZ;
				//double d2 = 8.0D;

				// Tf - remove 8x scaling for nether
				d0 = MathHelper.clamp(d0, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
				d1 = MathHelper.clamp(d1, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);

				d0 = MathHelper.clamp((int) d0, -29999872, 29999872);
				d1 = MathHelper.clamp((int) d1, -29999872, 29999872);
				float f = toTeleport.rotationYaw;
				toTeleport.setLocationAndAngles(d0, toTeleport.posY, d1, 90.0F, 0.0F);
				Teleporter teleporter = TFTeleporter.getTeleporterForDim(minecraftserver, dimensionIn); // TF - custom teleporter
				teleporter.placeInExistingPortal(toTeleport, f);
				blockpos = new BlockPos(toTeleport);
			}

			worldserver.updateEntityWithOptionalForce(toTeleport, false);
			toTeleport.world.profiler.endStartSection("reloading");
			Entity entity = EntityList.newEntity(toTeleport.getClass(), worldserver1);

			if (entity != null) {
				entity.copyDataFromOld(toTeleport);

				if (i == 1 && dimensionIn == 1) {
					BlockPos blockpos1 = worldserver1.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
					entity.moveToBlockPosAndAngles(blockpos1, entity.rotationYaw, entity.rotationPitch);
				}
				else {
					// TF - inline moveToBlockPosAndAngles without +0.5 offsets, since teleporter already took care of it
					entity.setLocationAndAngles(blockpos.getX(), blockpos.getY(), blockpos.getZ(), entity.rotationYaw, entity.rotationPitch);
				}

				boolean flag = entity.forceSpawn;
				entity.forceSpawn = true;
				worldserver1.spawnEntity(entity);
				entity.forceSpawn = flag;
				worldserver1.updateEntityWithOptionalForce(entity, false);
			}

			toTeleport.isDead = true;
			toTeleport.world.profiler.endSection();
			worldserver.resetUpdateEntityTick();
			worldserver1.resetUpdateEntityTick();
			toTeleport.world.profiler.endSection();
		}
	}

	public static void placePortal(World world, BlockPos pos, EnumFacing blockFace, EnumFacing playerFacing) {
		if (PORTAL_RECIPE == null) {
			PORTAL_RECIPE = new ArrayGen(new Block[][][] {
				//@formatter:off
				new Block[][] {
					new Block[0], // bottom layer
					new Block[] { null, Blocks.DIRT, Blocks.DIRT, null },
					new Block[] { null, Blocks.DIRT, Blocks.DIRT, null },
					new Block[0]
				},
				new Block[][] {
					new Block[] { Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, Blocks.GRASS }, // middle layer
					new Block[] { Blocks.GRASS, ModBlocks.NEW_TF_PORTAL, ModBlocks.NEW_TF_PORTAL, Blocks.GRASS },
					new Block[] { Blocks.GRASS, ModBlocks.NEW_TF_PORTAL, ModBlocks.NEW_TF_PORTAL, Blocks.GRASS },
					new Block[] { Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, Blocks.GRASS }
				},
				new Block[][] {
					new Block[] { getRandomNatureBlock(world), getRandomNatureBlock(world), getRandomNatureBlock(world), getRandomNatureBlock(world) }, //top layer
					new Block[] { getRandomNatureBlock(world), Blocks.AIR, Blocks.AIR, getRandomNatureBlock(world) },
					new Block[] { getRandomNatureBlock(world), Blocks.AIR, Blocks.AIR, getRandomNatureBlock(world) },
					new Block[] { getRandomNatureBlock(world), getRandomNatureBlock(world), getRandomNatureBlock(world), getRandomNatureBlock(world) }
				}
				//@formatter:on
			});
		}
		//
		PORTAL_RECIPE.placeStateArray(pos.down(2), world, playerFacing);
	}

	public static boolean isFullFluidBlock(Block block, World world, BlockPos pos) {
		if (block instanceof BlockLiquid) {
			return world.getBlockState(pos).getValue(BlockLiquid.LEVEL).intValue() == 0;
		}
		if (block instanceof BlockFluidBase) {
			return world.getBlockState(pos).getValue(BlockFluidBase.LEVEL).intValue() == 0;
		}
		if (block instanceof IFluidBlock) {
			return ((IFluidBlock) block).getFilledPercentage(world, pos) == 1.0F;
		}

		return false;
	}

	public static Block getFluidBlock() {
		Block fluidBlock = Block.getBlockFromName(Options.FLUID_NAME);
		if (fluidBlock == null || !(fluidBlock instanceof IFluidBlock)) { //just use default rather than crash
			Options.FLUID_NAME = "minecraft:water";
			fluidBlock = Block.getBlockFromName(Options.FLUID_NAME);
		}
		return fluidBlock;
	}

	private static class PassableNumber {
		private int number;

		PassableNumber(int number) {
			this.number = number;
		}

		int getNumber() {
			return number;
		}

		void decrement() {
			number = number - 1;
		}

		void setNumber(int number) {
			this.number = number;
		}
	}

	public static void renderDisabledText() {
		if (!TF_PORTAL_DISABLED_STRING.isEmpty()) {
			MTweaksUtil.renderHighlightText(76, TF_PORTAL_DISABLED_STRING);
			MTweaksUtil.renderHighlightText(68, TF_PORTAL_DISABLED_STRING_2);
		}
	}

}
