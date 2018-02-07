package p455w0rd.morphtweaks.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

/**
 * @author p455w0rd
 *
 */
public class GuiContainerBase extends GuiContainer {

	int tooltipBorderColors[] = new int[2];

	public GuiContainerBase(Container inventorySlotsIn) {
		super(inventorySlotsIn);
		tooltipBorderColors[0] = 1347420415;
		tooltipBorderColors[1] = 1344798847;
	}

	protected int getTooltipBorderColor(int whichColor) {
		if (whichColor != 1) {
			return tooltipBorderColors[0];
		}
		else {
			return tooltipBorderColors[1];
		}
	}

	protected void setTooltipColors(int color1, int color2) {
		tooltipBorderColors[0] = color1;
		tooltipBorderColors[1] = color2;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent(this))) {
			drawDefaultBackground();
			super.drawScreen(mouseX, mouseY, partialTicks);
			for (int i1 = 0; i1 < inventorySlots.inventorySlots.size(); i1++) {
				Slot slot = inventorySlots.inventorySlots.get(i1);
				if (isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseY) && slot.getHasStack()) {
					renderToolTip(slot.getStack(), mouseX, mouseY);
				}
			}
		}
	}

	@Override
	protected void renderToolTip(ItemStack stack, int x, int y) {
		List<String> list = stack.getTooltip(Minecraft.getMinecraft().player, mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL);

		for (int i = 0; i < list.size(); ++i) {
			if (i == 0) {
				list.set(i, stack.getRarity().rarityColor + list.get(i));
			}
			else {
				list.set(i, TextFormatting.GRAY + list.get(i));
			}
		}
		net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
		drawToolTipWithBorderColor(list, x, y, getTooltipBorderColor(0), getTooltipBorderColor(1));
		net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
	}

	protected void drawToolTipWithBorderColor(List<String> text, int x, int y, int borderColor1, int borderColor2) {
		drawHoveringText(text, x, y, mc.fontRenderer, -267386864, borderColor1, borderColor2);
	}

	private void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font, int backgroundColor, int borderColor1, int borderColor2) {
		if (!textLines.isEmpty()) {
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int i = 0;

			for (String s : textLines) {
				int j = mc.fontRenderer.getStringWidth(s);

				if (j > i) {
					i = j;
				}
			}

			int l1 = x + 15;
			int i2 = y = y - (((textLines.size() - 1) / 2) * 12);
			int k = 8;

			if (textLines.size() > 1) {
				k += 2 + (textLines.size() - 1) * 10;
			}

			if (l1 + i > width) {
				l1 -= 28 + i;
			}

			if (i2 + k + 6 > height) {
				i2 = height - k - 6;
			}

			zLevel = 300.0F;
			itemRender.zLevel = 300.0F;
			drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, backgroundColor, backgroundColor);
			drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, backgroundColor, backgroundColor);
			drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, backgroundColor, backgroundColor);
			drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, backgroundColor, backgroundColor);
			drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, backgroundColor, backgroundColor);
			drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, borderColor1, borderColor2);
			drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, borderColor1, borderColor2);
			drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, borderColor1, borderColor1);
			drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, borderColor2, borderColor2);

			for (int k1 = 0; k1 < textLines.size(); ++k1) {
				String s1 = textLines.get(k1);
				mc.fontRenderer.drawStringWithShadow(s1, l1, i2, -1);

				if (k1 == 0) {
					i2 += 2;
				}

				i2 += 10;
			}

			zLevel = 0.0F;
			itemRender.zLevel = 0.0F;
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();

		}
	}

	@Override
	public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
		float f = (startColor >> 24 & 255) / 255.0F;
		float f1 = (startColor >> 16 & 255) / 255.0F;
		float f2 = (startColor >> 8 & 255) / 255.0F;
		float f3 = (startColor & 255) / 255.0F;
		float f4 = (endColor >> 24 & 255) / 255.0F;
		float f5 = (endColor >> 16 & 255) / 255.0F;
		float f6 = (endColor >> 8 & 255) / 255.0F;
		float f7 = (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos(right, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, bottom, zLevel).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.pos(right, bottom, zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

}
