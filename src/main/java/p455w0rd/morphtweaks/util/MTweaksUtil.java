package p455w0rd.morphtweaks.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import p455w0rd.morphtweaks.init.ModConfig.Options;
import p455w0rd.morphtweaks.init.ModItems;

/**
 * @author p455w0rd
 *
 */
public class MTweaksUtil {

	public static Map<ResourceLocation, EntityEntry> CAPTURABLE_MOBS = Maps.<ResourceLocation, EntityEntry>newHashMap();
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
