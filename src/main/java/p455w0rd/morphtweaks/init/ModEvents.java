package p455w0rd.morphtweaks.init;

import java.util.Iterator;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.world.BlockEvent.CreateFluidSourceEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import p455w0rd.morphtweaks.api.IEnchanterRecipe;
import p455w0rd.morphtweaks.enchantments.EnchantmentSoulBound;
import p455w0rd.morphtweaks.init.ModConfig.Options;
import p455w0rd.morphtweaks.init.ModIntegration.Mods;
import p455w0rd.morphtweaks.integration.Baubles;
import p455w0rd.morphtweaks.integration.TFAndTinkers;
import p455w0rd.morphtweaks.integration.TwilightForest;
import p455w0rd.morphtweaks.util.MTweaksUtil;
import p455w0rd.morphtweaks.util.TextUtils;

/**
 * @author p455w0rd
 *
 */
public class ModEvents {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new ModEvents());
	}

	@SubscribeEvent
	public void onRegistryRegister(RegistryEvent.NewRegistry event) {
		ModRegistries.buildRegistries();
	}

	@SubscribeEvent
	public void onItemRegistryReady(RegistryEvent.Register<Item> event) {
		for (Item item : ModItems.getList()) {
			event.getRegistry().register(item);
		}
	}

	@SubscribeEvent
	public void onBlockRegistryReady(RegistryEvent.Register<Block> event) {
		for (Block block : ModBlocks.getList()) {
			event.getRegistry().register(block);
			if (block == ModBlocks.VOIDIFIER || block == ModBlocks.ENCHANTER) {
				ForgeRegistries.ITEMS.register(new ItemBlock(block) {

					@Override
					public String getItemStackDisplayName(ItemStack stack) {
						String name = I18n.translateToLocal(getUnlocalizedNameInefficiently(stack) + ".name").trim();
						return block == ModBlocks.ENCHANTER && FMLCommonHandler.instance().getSide().isClient() ? TextUtils.rainbow(name) : name;
					}

				}.setRegistryName(block.getRegistryName()));
			}
		}
	}

	@SubscribeEvent
	public void onBiomeRegistryReady(RegistryEvent.Register<Biome> event) {
		if (!Options.DISABLE_VANILLA_BIOME_REPLACEMENT) {
			for (Biome biome : ModBiomes.getList()) {
				event.getRegistry().register(biome);
			}
		}
	}

	@SubscribeEvent
	public void onEnchanterRecipeRegistryReady(RegistryEvent.Register<IEnchanterRecipe> event) {
		for (IEnchanterRecipe recipe : ModRecipes.getEnchanterRecipes()) {
			event.getRegistry().register(recipe);
		}
	}

	@SubscribeEvent
	public void onEnchantmentRegistryReady(RegistryEvent.Register<Enchantment> event) {
		for (Enchantment enchantment : ModEnchantments.getList()) {
			event.getRegistry().register(enchantment);
		}
	}

	@SubscribeEvent
	public void onModelRegistryReady(ModelRegistryEvent event) {
		ModBlocks.registerModels();
		ModItems.registerModels();
		if (Options.ENABLE_ENDERIUM_BLOCKS && Mods.TINKERS.isLoaded() && Mods.THERMALFOUNDATION.isLoaded()) {
			TFAndTinkers.registerEnderiumModel();
		}
	}

	@SubscribeEvent
	public void onCreateFluidSource(CreateFluidSourceEvent event) {
		if (Options.DISABLE_INFINITE_WATER) {
			Block block = event.getState().getBlock();
			if (block == Blocks.FLOWING_WATER) {
				event.setResult(Result.DENY);
			}
		}
	}

	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		if (Mods.TWILIGHTFOREST.isLoaded()) {
			EntityPlayer player = event.player;
			World world = player.world;
			if (!world.isRemote && event.phase == TickEvent.Phase.END && player.ticksExisted % 20 == 0) {
				TwilightForest.checkForPortalCreation(player, world, 6.0F);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		ModGlobals.incClientTicks();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPostRenderOverlay(RenderGameOverlayEvent.Post e) {
		if (e.getType() == ElementType.SUBTITLES && Mods.TWILIGHTFOREST.isLoaded()) {
			TwilightForest.renderDisabledText();
		}
	}

	@SubscribeEvent
	public void onEntityInteract(EntityInteract event) {
		if (event.getEntityPlayer() != null && event.getEntityPlayer().getEntityWorld() != null) {
			EntityPlayer player = event.getEntityPlayer();
			Entity target = event.getTarget();
			if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == ModItems.CAPTURE_WAND && target instanceof EntityCreature) {
				if (Options.DISABLE_CAPTURING_HOSTILE_MOBS && target instanceof EntityMob) {
					return;
				}
				MTweaksUtil.storeMobInWand(player.getHeldItemMainhand(), player.getEntityWorld(), (EntityLivingBase) target, !player.capabilities.isCreativeMode);
			}
		}
	}

	/**
	 * Taken from EnderIO's Soul Bound enchant-ported to 1.12.2 at least so we have it until they get EIO on 1.12
	 */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDropsEvent event) {
		if (event.getEntityPlayer() == null || event.getEntityPlayer() instanceof FakePlayer || event.isCanceled()) {
			return;
		}
		if (event.getEntityPlayer().world.getGameRules().getBoolean("keepInventory")) {
			return;
		}
		EntityPlayer player = event.getEntityPlayer();
		if (Mods.BAUBLES.isLoaded()) {
			IItemHandlerModifiable baubles = Baubles.getBaubles(player);
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					ItemStack item = baubles.getStackInSlot(i);
					if (!item.isEmpty()) {
						if (EnchantmentSoulBound.isSoulBound(item)) {
							if (!Baubles.addToBaublesInventory(player, item)) {
								if (addToPlayerInventory(player, item)) {
									baubles.setStackInSlot(i, ItemStack.EMPTY);
									event.getDrops().remove(item);
								}
							}
						}
					}
				}
			}
		}
		Iterator<EntityItem> iterator = event.getDrops().iterator();
		while (iterator.hasNext()) {
			ItemStack item = iterator.next().getItem();
			if (EnchantmentSoulBound.isSoulBound(item)) {
				if (addToPlayerInventory(player, item)) {
					iterator.remove();
				}
			}
		}

	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerDeathPost(PlayerDropsEvent event) {
		if (event.getEntityPlayer() == null || event.getEntityPlayer() instanceof FakePlayer || event.isCanceled()) {
			return;
		}
		if (event.getEntityPlayer().world.getGameRules().getBoolean("keepInventory")) {
			return;
		}

		EntityPlayer player = event.getEntityPlayer();
		if (Mods.BAUBLES.isLoaded()) {
			IItemHandlerModifiable baubles = Baubles.getBaubles(player);
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					ItemStack item = baubles.getStackInSlot(i);
					if (!item.isEmpty()) {
						if (EnchantmentSoulBound.isSoulBound(item)) {
							if (!Baubles.addToBaublesInventory(player, item)) {
								if (addToPlayerInventory(player, item)) {
									baubles.setStackInSlot(i, ItemStack.EMPTY);
									event.getDrops().remove(item);
								}
							}
						}
					}
				}
			}
		}
		Iterator<EntityItem> iterator = event.getDrops().iterator();
		while (iterator.hasNext()) {
			ItemStack item = iterator.next().getItem();
			if (EnchantmentSoulBound.isSoulBound(item)) {
				if (addToPlayerInventory(event.getEntityPlayer(), item)) {
					iterator.remove();
				}
			}
		}

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerClone(PlayerEvent.Clone event) {
		if (!event.isWasDeath() || event.isCanceled()) {
			return;
		}
		if (event.getOriginal() == null || event.getEntityPlayer() == null || event.getEntityPlayer() instanceof FakePlayer) {
			return;
		}
		EntityPlayer player = event.getEntityPlayer();
		if (player.world.getGameRules().getBoolean("keepInventory")) {
			return;
		}
		if (event.getOriginal() == player || event.getOriginal().inventory == player.inventory || (event.getOriginal().inventory.armorInventory == player.inventory.armorInventory && event.getOriginal().inventory.mainInventory == player.inventory.mainInventory)) {
			return;
		}
		if (Mods.BAUBLES.isLoaded()) {
			IItemHandlerModifiable baubles = Baubles.getBaubles(event.getOriginal());
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					ItemStack item = baubles.getStackInSlot(i);
					if (!item.isEmpty()) {
						if (EnchantmentSoulBound.isSoulBound(item)) {
							if (!Baubles.addToBaublesInventory(player, item)) {
								if (addToPlayerInventory(player, item)) {
									baubles.setStackInSlot(i, ItemStack.EMPTY);
								}
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < event.getOriginal().inventory.armorInventory.size(); i++) {
			ItemStack item = event.getOriginal().inventory.armorInventory.get(i);
			if (EnchantmentSoulBound.isSoulBound(item)) {
				if (addToPlayerInventory(event.getEntityPlayer(), item)) {
					event.getOriginal().inventory.armorInventory.set(i, ItemStack.EMPTY);
				}
			}
		}
		for (int i = 0; i < event.getOriginal().inventory.mainInventory.size(); i++) {
			ItemStack item = event.getOriginal().inventory.mainInventory.get(i);
			if (EnchantmentSoulBound.isSoulBound(item)) {
				if (addToPlayerInventory(event.getEntityPlayer(), item)) {
					event.getOriginal().inventory.mainInventory.set(i, ItemStack.EMPTY);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerClonePost(PlayerEvent.Clone event) {
		if (!event.isWasDeath() || event.isCanceled()) {
			return;
		}
		if (event.getOriginal() == null || event.getEntityPlayer() == null || event.getEntityPlayer() instanceof FakePlayer) {
			return;
		}
		EntityPlayer player = event.getEntityPlayer();
		if (player.world.getGameRules().getBoolean("keepInventory")) {
			return;
		}
		if (event.getOriginal() == player || event.getOriginal().inventory == player.inventory || (event.getOriginal().inventory.armorInventory == player.inventory.armorInventory && event.getOriginal().inventory.mainInventory == player.inventory.mainInventory)) {
			return;
		}

		if (Mods.BAUBLES.isLoaded()) {
			IItemHandlerModifiable baubles = Baubles.getBaubles(event.getOriginal());
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					ItemStack item = baubles.getStackInSlot(i);
					if (!item.isEmpty()) {
						if (EnchantmentSoulBound.isSoulBound(item)) {
							if (!Baubles.addToBaublesInventory(player, item)) {
								if (addToPlayerInventory(player, item)) {
									baubles.setStackInSlot(i, ItemStack.EMPTY);
								}
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < event.getOriginal().inventory.armorInventory.size(); i++) {
			ItemStack item = event.getOriginal().inventory.armorInventory.get(i);
			if (EnchantmentSoulBound.isSoulBound(item)) {
				if (addToPlayerInventory(event.getEntityPlayer(), item) || tryToSpawnItemInWorld(event.getOriginal(), item)) {
					event.getOriginal().inventory.armorInventory.set(i, ItemStack.EMPTY);
				}
			}
		}
		for (int i = 0; i < event.getOriginal().inventory.mainInventory.size(); i++) {
			ItemStack item = event.getOriginal().inventory.mainInventory.get(i);
			if (EnchantmentSoulBound.isSoulBound(item)) {
				if (addToPlayerInventory(event.getEntityPlayer(), item) || tryToSpawnItemInWorld(event.getOriginal(), item)) {
					event.getOriginal().inventory.mainInventory.set(i, ItemStack.EMPTY);
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void handleTooltip(ItemTooltipEvent event) {
		if (EnchantmentSoulBound.isSoulBound(event.getItemStack()) || EnchantmentSoulBound.isSoulBoundEnchantedBook(event.getItemStack())) {
			for (int i = 0; i < event.getToolTip().size(); i++) {
				if (event.getToolTip().get(i).contains(I18n.translateToLocal(ModEnchantments.SOUL_BOUND.getName()))) {
					String line = event.getToolTip().get(i);
					event.getToolTip().set(i, TextFormatting.DARK_PURPLE.toString() + TextFormatting.ITALIC + " - " + line);
					i++;
				}
			}
		}
	}

	private boolean tryToSpawnItemInWorld(EntityPlayer entityPlayer, @Nonnull ItemStack item) {
		if (entityPlayer != null) {
			EntityItem entityitem = new EntityItem(entityPlayer.world, entityPlayer.posX, entityPlayer.posY + 0.5, entityPlayer.posZ, item);
			entityitem.setPickupDelay(40);
			entityitem.lifespan *= 5;
			entityitem.motionX = 0;
			entityitem.motionZ = 0;
			entityPlayer.world.spawnEntity(entityitem);
			return true;
		}
		return false;
	}

	private boolean addToPlayerInventory(EntityPlayer entityPlayer, ItemStack item) {
		if (item == null || entityPlayer == null) {
			return false;
		}
		if (item.getItem() instanceof ItemArmor) {
			ItemArmor arm = (ItemArmor) item.getItem();
			int index = arm.armorType.getIndex();
			if (entityPlayer.inventory.armorInventory.get(index).isEmpty()) {
				entityPlayer.inventory.armorInventory.set(index, item);
				return true;
			}
		}

		InventoryPlayer inv = entityPlayer.inventory;
		for (int i = 0; i < inv.mainInventory.size(); i++) {
			if (inv.mainInventory.get(i).isEmpty()) {
				inv.mainInventory.set(i, item.copy());
				return true;
			}
		}
		return false;
	}

}
