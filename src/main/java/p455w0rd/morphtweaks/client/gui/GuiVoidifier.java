package p455w0rd.morphtweaks.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import p455w0rd.morphtweaks.init.ModGlobals;

/**
 * @author p455w0rd
 *
 */
public class GuiVoidifier extends GuiContainerBase {

	public GuiVoidifier(Container inventorySlotsIn) {
		super(inventorySlotsIn);
		xSize = 210;
		ySize = 204;
		guiLeft = ((width - xSize) / 2);
		guiTop = ((height - ySize) / 2);
		setTooltipColors(0xFF6E03C8, 0x996E03C8);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(new ResourceLocation(ModGlobals.MODID, "textures/gui/voidifier.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		int fontColor = 16777215;
		int yOffset = 123;

		mc.fontRenderer.drawString(I18n.format("tile.voidifier.name", new Object[0]), 7, 6, 0x99A74CFF, true);

		mc.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 7, ySize - yOffset, fontColor);

		GlStateManager.enableBlend();
		GlStateManager.enableLighting();
	}

}