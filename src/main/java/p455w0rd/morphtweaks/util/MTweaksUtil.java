package p455w0rd.morphtweaks.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
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
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import p455w0rd.morphtweaks.init.ModBlocks;
import p455w0rd.morphtweaks.init.ModItems;
import p455w0rd.morphtweaks.init.ModConfig.Options;
import twilightforest.TFTeleporter;

/**
 * @author p455w0rd
 *
 */
public class MTweaksUtil {

	public static List<Block> NATURE_BLOCKS = Lists.<Block>newArrayList();
	public static List<Block> PLACEABLE_NATURE_BLOCKS = Lists.<Block>newArrayList();
	public static Map<ResourceLocation, EntityEntry> CAPTURABLE_MOBS = Maps.<ResourceLocation, EntityEntry>newHashMap();

	public static Block getFluidBlock() {
		Block fluidBlock = Block.getBlockFromName(Options.FLUID_NAME);
		if (fluidBlock == null || !(fluidBlock instanceof IFluidBlock)) { //just use default rather than crash
			Options.FLUID_NAME = "minecraft:water";
			fluidBlock = Block.getBlockFromName(Options.FLUID_NAME);
		}
		return fluidBlock;
	}

	/*
		public static boolean isGoodPortalPool(World world, BlockPos pos) {
			return isGoodPortalPool(world, pos, getFluidBlock());
		}
	
		public static boolean isGoodPortalPool(World world, BlockPos pos, Block block) {
			return isGoodPortalPoolStrict(world, pos, block) || isGoodPortalPoolStrict(world, pos.north(), block) || isGoodPortalPoolStrict(world, pos.south(), block) || isGoodPortalPoolStrict(world, pos.west(), block) || isGoodPortalPoolStrict(world, pos.east(), block) || isGoodPortalPoolStrict(world, pos.west().north(), block) || isGoodPortalPoolStrict(world, pos.west().south(), block) || isGoodPortalPoolStrict(world, pos.east().north(), block) || isGoodPortalPoolStrict(world, pos.east().south(), block);
		}
	*/
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

	/*
		public static boolean isGoodPortalPoolStrict(World world, BlockPos pos, Block block) {
			// 4 squares of water
			return world.getBlockState(pos).getBlock() == block && world.getBlockState(pos.east()).getBlock() == block && world.getBlockState(pos.east().south()).getBlock() == block && world.getBlockState(pos.south()).getBlock() == block
	
					&& isFullFluidBlock(block, world, pos) && isFullFluidBlock(block, world, pos.east()) && isFullFluidBlock(block, world, pos.east().south()) && isFullFluidBlock(block, world, pos.south())
	
					// grass in the 12 squares surrounding
					&& isGrassOrDirt(world, pos.west().north()) && isGrassOrDirt(world, pos.west()) && isGrassOrDirt(world, pos.west().south()) && isGrassOrDirt(world, pos.west().south(2))
	
					&& isGrassOrDirt(world, pos.north()) && isGrassOrDirt(world, pos.east().north())
	
					&& isGrassOrDirt(world, pos.south(2)) && isGrassOrDirt(world, pos.east().south(2))
	
					&& isGrassOrDirt(world, pos.east(2).north()) && isGrassOrDirt(world, pos.east(2)) && isGrassOrDirt(world, pos.east(2).south()) && isGrassOrDirt(world, pos.east(2).south(2))
	
					// solid underneath
					&& world.getBlockState(pos.down()).getMaterial().isSolid() && world.getBlockState(pos.down().east()).getMaterial().isSolid() && world.getBlockState(pos.down().east().south()).getMaterial().isSolid() && world.getBlockState(pos.down().south()).getMaterial().isSolid()
	
					// 12 nature blocks above the grass?
					&& isNatureBlock(world, pos.up().west().north()) && isNatureBlock(world, pos.up().west()) && isNatureBlock(world, pos.up().west().south()) && isNatureBlock(world, pos.up().west().south(2))
	
					&& isNatureBlock(world, pos.up().north()) && isNatureBlock(world, pos.up().east().north())
	
					&& isNatureBlock(world, pos.up().south(2)) && isNatureBlock(world, pos.up().south(2).east())
	
					&& isNatureBlock(world, pos.up().east(2).north()) && isNatureBlock(world, pos.up().east(2)) && isNatureBlock(world, pos.up().east(2).south()) && isNatureBlock(world, pos.up().east(2).south(2));
		}
	*/
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

	/*
		public static boolean tryToCreatePortal(World world, BlockPos pos) {
			if (isGoodPortalPool(world, pos)) {
				world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), true));
				//world.spawnEntity(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), true));
				createPortal(world, pos);
				return true;
			}
			else {
				return false;
			}
		}
		*/
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

	public static ArrayGen PORTAL_RECIPE = null;

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

	private static final String CAPTURED_MOB_TAG = "CapturedMob";
	private static final String CAPTURED_MOB_DATA_TAG = "CapturedMobData";

	public static ResourceLocation getCapturedMobInWand(ItemStack wand) {
		if (wand.getItem() == ModItems.CAPTURE_WAND && wand.hasTagCompound()) {
			NBTTagCompound wandNBT = wand.getTagCompound();
			if (wandNBT.hasKey(CAPTURED_MOB_TAG) && !wandNBT.getString(CAPTURED_MOB_TAG).isEmpty()) {
				ResourceLocation mob = new ResourceLocation(wandNBT.getString(CAPTURED_MOB_TAG));
				if (ForgeRegistries.ENTITIES.containsKey(mob)) {
					return mob;
				}
			}
		}
		return null;
	}

	public static String getMobName(ItemStack wand) {
		if (wand.getItem() == ModItems.CAPTURE_WAND && wand.hasTagCompound() && wand.getTagCompound().hasKey(CAPTURED_MOB_TAG)) {
			for (EntityEntry entry : getCapurableMobs().values()) {
				ResourceLocation resLoc = new ResourceLocation(wand.getTagCompound().getString(CAPTURED_MOB_TAG));
				if (entry.getRegistryName().equals(resLoc)) {
					return I18n.translateToLocal("entity." + entry.getName() + ".name");
				}
			}
		}
		return "";
	}

	public static boolean isVillager(ResourceLocation resLoc) {
		return resLoc.toString().equals("minecraft:villager");
	}

	public static String getCapturedVillagerProfession(ItemStack wand) {
		if (wand.getItem() == ModItems.CAPTURE_WAND && wand.hasTagCompound() && wand.getTagCompound().hasKey(CAPTURED_MOB_TAG)) {
			for (EntityEntry entry : getCapurableMobs().values()) {
				ResourceLocation resLoc = new ResourceLocation(wand.getTagCompound().getString(CAPTURED_MOB_TAG));
				if (entry.getRegistryName().equals(resLoc)) {
					if (isVillager(resLoc)) {
						if (wand.getTagCompound().hasKey(CAPTURED_MOB_DATA_TAG)) {
							NBTTagCompound villagerData = wand.getTagCompound().getCompoundTag(CAPTURED_MOB_DATA_TAG);
							VillagerProfession profession = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation(villagerData.getString("ProfessionName")));
							VillagerCareer career = profession.getCareer(villagerData.getInteger("Career"));
							return "- " + I18n.translateToLocal("tooltip.profession") + ": " + I18n.translateToLocal("entity.Villager." + career.getName());
						}
						return "- " + I18n.translateToLocal("tooltip.profession") + ": " + I18n.translateToLocal("entity.Villager.farmer");
					}
				}
			}
		}
		return "";
	}

	public static int getMobEggColor(ResourceLocation resLoc, int index) {
		for (EntityEntry entry : ForgeRegistries.ENTITIES.getValues()) {
			if (entry.getRegistryName().equals(resLoc) && entry.getEgg() != null) {
				return index == 0 ? entry.getEgg().primaryColor : entry.getEgg().secondaryColor;
			}
		}
		return -1;
	}

	public static Map<ResourceLocation, EntityEntry> getCapurableMobs() {
		for (Entry<ResourceLocation, EntityEntry> entry : ForgeRegistries.ENTITIES.getEntries()) {
			if (Options.DISABLE_CAPTURING_HOSTILE_MOBS && EntityMob.class.isAssignableFrom(entry.getValue().getEntityClass())) {
				continue;
			}
			if (EntityCreature.class.isAssignableFrom(entry.getValue().getEntityClass())) {
				CAPTURABLE_MOBS.put(entry.getKey(), entry.getValue());
			}
		}
		return CAPTURABLE_MOBS;
	}

	public static boolean spawnMobFromWand(ItemStack wand, World world, BlockPos pos, boolean isPlayerCreative) {
		if (!wand.isEmpty() && wand.getItem() == ModItems.CAPTURE_WAND && doesWandHaveMobStored(wand)) {
			for (EntityEntry entry : getCapurableMobs().values()) {
				if (entry.getRegistryName().equals(getCapturedMobInWand(wand))) {
					Entity entity = entry.newInstance(world);

					if (wand.getTagCompound().hasKey(CAPTURED_MOB_DATA_TAG)) {
						entity.readFromNBT(wand.getTagCompound().getCompoundTag(CAPTURED_MOB_DATA_TAG));
					}
					entity.setUniqueId(UUID.randomUUID());
					entity.setLocationAndAngles(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, 0.0F, 0.0F);
					world.spawnEntity(entity);
					if (!isPlayerCreative) {
						wand.getTagCompound().setString(CAPTURED_MOB_TAG, "");
						wand.getTagCompound().setTag(CAPTURED_MOB_DATA_TAG, new NBTTagCompound());
					}
					return true;
				}
			}
		}
		return false;
	}

	public static void storeMobInWand(ItemStack wand, Class<? extends Entity> clazz, boolean killMob, boolean ignoreNBT) {
		if (wand.getItem() == ModItems.CAPTURE_WAND && (!doesWandHaveMobStored(wand) || !killMob)) {
			for (EntityEntry entry : getCapurableMobs().values()) {
				if (entry.getEntityClass().equals(clazz)) {
					String mobLoc = entry.getRegistryName().toString();
					if (!wand.hasTagCompound()) {
						wand.setTagCompound(new NBTTagCompound());
					}
					wand.getTagCompound().setString(CAPTURED_MOB_TAG, mobLoc);
				}
			}
		}
	}

	public static boolean storeMobInWand(ItemStack wand, World world, EntityLivingBase mob, boolean killMob) {
		return storeMobInWand(wand, world, mob, killMob, false);
	}

	public static boolean storeMobInWand(ItemStack wand, World world, EntityLivingBase mob, boolean killMob, boolean ignoreNBT) {
		if (world != null && wand.getItem() == ModItems.CAPTURE_WAND && (!doesWandHaveMobStored(wand) || !killMob)) {
			for (EntityEntry entry : getCapurableMobs().values()) {
				if (entry.getEntityClass().equals(mob.getClass())) {
					String mobLoc = entry.getRegistryName().toString();
					if (!wand.hasTagCompound()) {
						wand.setTagCompound(new NBTTagCompound());
					}
					wand.getTagCompound().setString(CAPTURED_MOB_TAG, mobLoc);
					if (!ignoreNBT) {
						wand.getTagCompound().setTag(CAPTURED_MOB_DATA_TAG, mob.writeToNBT(new NBTTagCompound()));
					}
					if (killMob) {
						mob.setDead();
					}
					return true;
				}
			}
		}
		return false;
	}

	public static boolean doesWandHaveMobStored(ItemStack wand) {
		return getCapturedMobInWand(wand) != null;
	}

	public static void renderHighlightText(int yOffset, String text) {
		renderHighlightText(yOffset, text, 1.0F);
	}

	public static void renderHighlightText(int yOffset, String text, float scale) {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaledRes = new ScaledResolution(mc);
		if (mc.playerController == null) {
			return;
		}
		String s = TextFormatting.ITALIC + "" + text;
		int j = scaledRes.getScaledHeight() - yOffset;
		if (!mc.playerController.shouldDrawHUD()) {
			j += 14;
		}
		int k = 255;
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);
		float i = (scaledRes.getScaledWidth() - (mc.fontRenderer.getStringWidth(text) * scale)) / 2;
		mc.fontRenderer.drawString(s, i / scale, j / scale, 16777215 + (k << 24), true);
		GlStateManager.popMatrix();

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

	public static ItemStack getEnchantedBook(Enchantment enchantment, int enchantmentLevel) {
		ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
		Map<Enchantment, Integer> enchantmentMap = Maps.newHashMap();
		enchantmentMap.put(enchantment, enchantmentLevel);
		EnchantmentHelper.setEnchantments(enchantmentMap, enchantedBook);
		return enchantedBook;
	}

	private static NBTTagCompound getPlayerTag(EntityPlayer player) {
		NBTTagCompound playerNBT = new NBTTagCompound();
		player.writeEntityToNBT(playerNBT);
		return playerNBT;
	}

	public static int getTotalExperience(EntityPlayer player) {
		NBTTagCompound playerNBT = getPlayerTag(player);
		if (playerNBT.hasKey("XpTotal")) {
			return playerNBT.getInteger("XpTotal");
		}
		return 0;
	}

	public static int getExperienceLevels(EntityPlayer player) {
		NBTTagCompound playerNBT = getPlayerTag(player);
		if (playerNBT.hasKey("XpLevel")) {
			return playerNBT.getInteger("XpLevel");
		}
		return 0;
	}

	public static void setTotalExperience(EntityPlayer player, int experience) {
		NBTTagCompound playerNBT = getPlayerTag(player);
		playerNBT.setInteger("XpTotal", experience);
		player.readEntityFromNBT(playerNBT);
	}

	public static void setExperienceLevels(EntityPlayer player, int levels) {
		NBTTagCompound playerNBT = getPlayerTag(player);
		playerNBT.setInteger("XpLevel", levels);
		player.readEntityFromNBT(playerNBT);
	}

}
