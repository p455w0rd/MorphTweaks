package p455w0rd.morphtweaks.client.render;

import java.util.Random;

import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.model.IModelState;
import p455w0rd.morphtweaks.blocks.tiles.TileEnchanter;
import p455w0rd.morphtweaks.init.ModGlobals;
import p455w0rd.morphtweaks.util.RenderModel;

/**
 * @author p455w0rd
 *
 */
public class EnchanterRenderer extends TileEntitySpecialRenderer<TileEnchanter> implements IItemRenderer {

	private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation(ModGlobals.MODID, "textures/blocks/enchanter_book.png");
	private final ModelBook modelBook = new ModelBook();
	private int currentGlobalTicks;

	@Override
	public void render(TileEnchanter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y + 0.75F, (float) z + 0.5F);
		float f = te.tickCount + partialTicks;
		GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F, 0.0F);
		float f1;

		for (f1 = te.bookRotation - te.bookRotationPrev; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) {
			;
		}

		while (f1 < -(float) Math.PI) {
			f1 += ((float) Math.PI * 2F);
		}

		float f2 = te.bookRotationPrev + f1 * partialTicks;
		GlStateManager.rotate(-f2 * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
		bindTexture(TEXTURE_BOOK);
		float f3 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.25F;
		float f4 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.75F;
		f3 = (f3 - MathHelper.fastFloor(f3)) * 1.6F - 0.3F;
		f4 = (f4 - MathHelper.fastFloor(f4)) * 1.6F - 0.3F;

		if (f3 < 0.0F) {
			f3 = 0.0F;
		}

		if (f4 < 0.0F) {
			f4 = 0.0F;
		}

		if (f3 > 1.0F) {
			f3 = 1.0F;
		}

		if (f4 > 1.0F) {
			f4 = 1.0F;
		}

		float f5 = te.bookSpreadPrev + (te.bookSpread - te.bookSpreadPrev) * partialTicks;
		GlStateManager.enableCull();
		modelBook.render((Entity) null, f, f3, f4, f5, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	private void getCurrentTicks() {
		if (currentGlobalTicks != ModGlobals.getClientTicks()) {
			currentGlobalTicks = ModGlobals.getClientTicks();
			update();
		}
	}

	public int tickCount;
	public float pageFlip;
	public float pageFlipPrev;
	public float flipT;
	public float flipA;
	public float bookSpread;
	public float bookSpreadPrev;
	public float bookRotation;
	public float bookRotationPrev;
	public float tRot;
	private static final Random rand = new Random();

	@Override
	public void renderItem(ItemStack stack, TransformType transformType) {
		getCurrentTicks();
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5F, 0.75F, 0.5F);
		float f = ModGlobals.getClientTicks() + partialTicks;
		GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F, 0.0F);
		float f1;

		for (f1 = bookRotation - bookRotationPrev; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) {
			;
		}

		while (f1 < -(float) Math.PI) {
			f1 += ((float) Math.PI * 2F);
		}

		float f2 = bookRotationPrev + f1 * partialTicks;
		GlStateManager.rotate(-f2 * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
		if (transformType == TransformType.FIRST_PERSON_RIGHT_HAND) {
			GlStateManager.translate(0.15F, 0.0F, 0.0F);
			GlStateManager.rotate(-40F, 0.0F, 0.0F, 1.0F);
		}
		else if (transformType == TransformType.FIRST_PERSON_LEFT_HAND) {
			GlStateManager.translate(0.15F, 0.0F, 0.0F);
			GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-40F, 0.0F, 0.0F, 1.0F);
		}
		else if (transformType == TransformType.THIRD_PERSON_LEFT_HAND || transformType == TransformType.THIRD_PERSON_RIGHT_HAND) {
			GlStateManager.rotate(-20F, 0.0F, 0.0F, 1.0F);
		}
		else if (transformType == TransformType.FIXED || transformType == TransformType.GROUND) {
			GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_BOOK);
		float f3 = pageFlipPrev + (pageFlip - pageFlipPrev) * partialTicks + 0.25F;
		float f4 = pageFlipPrev + (pageFlip - pageFlipPrev) * partialTicks + 0.75F;
		f3 = (f3 - MathHelper.fastFloor(f3)) * 1.6F - 0.3F;
		f4 = (f4 - MathHelper.fastFloor(f4)) * 1.6F - 0.3F;

		if (f3 < 0.0F) {
			f3 = 0.0F;
		}

		if (f4 < 0.0F) {
			f4 = 0.0F;
		}

		if (f3 > 1.0F) {
			f3 = 1.0F;
		}

		if (f4 > 1.0F) {
			f4 = 1.0F;
		}

		float f5 = bookSpreadPrev + (bookSpread - bookSpreadPrev) * partialTicks;
		GlStateManager.enableCull();
		modelBook.render((Entity) null, f, f3, f4, f5, 0.0F, 0.0625F);

		GlStateManager.popMatrix();

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		IBakedModel blockModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Block.getBlockFromItem(stack.getItem()).getDefaultState());
		RenderModel.render(blockModel, stack);

	}

	public void update() {
		bookSpreadPrev = bookSpread;
		bookRotationPrev = bookRotation;

		double d0 = 0.5F;
		double d1 = 0.5F;
		tRot = (float) MathHelper.atan2(d1, d0);
		bookSpread += 0.1F;

		if (bookSpread < 0.5F || rand.nextInt(40) == 0) {
			float f1 = flipT;

			while (true) {
				flipT += rand.nextInt(4) - rand.nextInt(4);

				if (f1 != flipT) {
					break;
				}
			}
		}

		while (bookRotation >= (float) Math.PI) {
			bookRotation -= ((float) Math.PI * 2F);
		}

		while (bookRotation < -(float) Math.PI) {
			bookRotation += ((float) Math.PI * 2F);
		}

		while (tRot >= (float) Math.PI) {
			tRot -= ((float) Math.PI * 2F);
		}

		while (tRot < -(float) Math.PI) {
			tRot += ((float) Math.PI * 2F);
		}

		float f2;

		for (f2 = tRot - bookRotation; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) {
			;
		}

		while (f2 < -(float) Math.PI) {
			f2 += ((float) Math.PI * 2F);
		}

		bookRotation += f2 * 0.4F;
		bookSpread = MathHelper.clamp(bookSpread, 0.0F, 1.0F);
		++tickCount;
		pageFlipPrev = pageFlip;
		float f = (flipT - pageFlip) * 0.4F;
		f = MathHelper.clamp(f, -0.2F, 0.2F);
		flipA += (f - flipA) * 0.9F;
		pageFlip += flipA;

	}

	@Override
	public IModelState getTransforms() {
		return TransformUtils.DEFAULT_BLOCK;
	}

}
