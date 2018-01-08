package p455w0rd.morphtweaks.client.gui;

import java.io.IOException;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import p455w0rd.morphtweaks.container.ContainerEnchanter;
import p455w0rd.morphtweaks.init.ModGlobals;
import p455w0rd.morphtweaks.init.ModIntegration.Mods;
import p455w0rd.morphtweaks.integration.JEI;
import p455w0rd.morphtweaks.util.MTweaksUtil;
import p455w0rd.morphtweaks.util.TextUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiEnchanter extends GuiContainerBase {

	public GuiEnchanter(Container inventorySlotsIn) {
		super(inventorySlotsIn);
		xSize = 201;
		ySize = 146;
		guiLeft = ((width - xSize) / 2);
		guiTop = ((height - ySize) / 2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(new ResourceLocation(ModGlobals.MODID, "textures/gui/enchanter.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int color = EnumDyeColor.byDyeDamage(TextUtils.RAINBOW_COLORS.get(TextUtils.getOffset(250)).getColorIndex()).getColorValue();
		setTooltipColors(0xFF000000 | color, 0x99000000 | color);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		mc.fontRenderer.drawString(TextUtils.rainbow(I18n.translateToLocal("tile.enchanter.name")), 48, 6, 0x99A74CFF, true);
		for (int i = 0; i < 4; i++) {
			int offset = i + 1;
			if (offset >= TextUtils.RAINBOW_COLORS.size()) {
				offset = offset - i;
			}
			drawSlotBorder(inventorySlots.inventorySlots.get(i).xPos, inventorySlots.inventorySlots.get(i).yPos, offset);
		}
		if (inventorySlots instanceof ContainerEnchanter) {
			ContainerEnchanter container = (ContainerEnchanter) inventorySlots;
			if (container.getEnchanter().getCurrentRecipe() != null) {
				int requiredLevels = container.getEnchanter().getCurrentRecipe().getRequiredExperience();
				int playerLevels = MTweaksUtil.getExperienceLevels(mc.player);
				int levelColor = playerLevels >= requiredLevels ? 0xFF00FF00 : 0xFFFF0000;
				mc.fontRenderer.drawString("XP Levels: " + requiredLevels, 64, 44, levelColor, true);
			}
		}
		GlStateManager.enableBlend();
		GlStateManager.enableLighting();

	}

	public void drawItem(ItemStack item, int x, int y) {
		RenderHelper.enableGUIStandardItemLighting();
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item, x, y);
		RenderHelper.disableStandardItemLighting();
	}

	private void drawSlotBorder(int x, int y, int index) {
		int offset = TextUtils.getOffset(250) + index;
		if (offset >= TextUtils.RAINBOW_COLORS.size()) {
			offset = 0;
		}
		int color = EnumDyeColor.byDyeDamage(TextUtils.getColor(index + 1, index).getColorIndex()).getColorValue();
		drawGradientRect(x - 1, y - 1, x + 16, y, 0xFF000000 | color, 0xFF000000 | color);
		drawGradientRect(x - 1, y - 1, x, y + 17, 0xFF000000 | color, 0xFF000000 | color);
		drawGradientRect(x + 16, y - 1, x + 17, y + 17, 0xFF000000 | color, 0xFF000000 | color);
		drawGradientRect(x - 1, y + 16, x + 17, y + 17, 0xFF000000 | color, 0xFF000000 | color);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0 && isPointInRegion(134, 25, 23, 15, mouseX, mouseY) && Mods.JEI.isLoaded()) {
			JEI.showRecipes(Arrays.<String>asList(new String[] {
					JEI.UID.ENCHANTER
			}));
		}
		else {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (Mods.JEI.isLoaded()) {
			if (isPointInRegion(134, 25, 23, 15, mouseX, mouseY)) {
				int color = EnumDyeColor.byDyeDamage(TextUtils.getColor(0, 0).getColorIndex()).getColorValue();
				drawJEIClickOverlay(134, 25);
				drawToolTipWithBorderColor(Arrays.<String>asList(new String[] {
						"Show Recipes"
				}), mouseX, mouseY, 0xFF000000 | color, 0x99000000 | color);
			}
		}
	}

	private void drawJEIClickOverlay(int posX, int posY) {
		int x = posX + guiLeft;
		int y = posY + guiTop;
		int width = 23;
		int height = 15;
		int red = 255;
		int green = 255;
		int blue = 255;
		int alpha = 80;
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		Tessellator tessellator1 = Tessellator.getInstance();
		BufferBuilder renderer = tessellator1.getBuffer();

		renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x + 0, y + 0, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + 0, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + width, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + width, y + 0, 0.0D).color(red, green, blue, alpha).endVertex();
		Tessellator.getInstance().draw();

		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
	}

}
