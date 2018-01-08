package p455w0rd.morphtweaks.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author p455w0rd
 *
 */
public class ParticleEnchanter extends Particle {
	private final float oSize;
	private final double coordX;
	private final double coordY;
	private final double coordZ;

	double x = 0;
	double y = 0;
	double z = 0;
	double degree = 0;
	boolean upOrDown = true;
	int ticks = 0;

	public ParticleEnchanter(World worldIn, BlockPos posIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
		super(worldIn, posIn.getX(), posIn.getY(), posIn.getZ(), xSpeedIn, ySpeedIn, zSpeedIn);
		motionX = xSpeedIn;
		motionY = ySpeedIn;
		motionZ = zSpeedIn;
		coordX = posIn.getX() + 0.5d;
		coordY = posIn.getY();
		coordZ = posIn.getZ() + 0.5d;
		prevPosX = coordX + xSpeedIn;
		prevPosY = coordY + ySpeedIn;
		prevPosZ = coordZ + zSpeedIn;
		posX = prevPosX;
		posY = prevPosY;
		posZ = prevPosZ;
		float red = rand.nextFloat() + 0.6F;
		float green = rand.nextFloat() + 0.6f;
		float blue = rand.nextFloat() + 0.6f;
		particleScale = rand.nextFloat() * 0.5F + 0.2F;
		oSize = particleScale;
		particleRed = red;
		particleGreen = green;
		particleBlue = blue;
		particleAlpha = 1.0F;
		particleMaxAge = (int) (Math.random() * 10.0D) + 30;
		setParticleTextureIndex((int) (Math.random() * 26.0D + 1.0D + 224.0D));
	}

	@Override
	public void move(double x, double y, double z) {
		setBoundingBox(getBoundingBox().offset(x, y, z));
		resetPositionToBB();
	}

	@Override
	public int getBrightnessForRender(float p_189214_1_) {
		int i = super.getBrightnessForRender(p_189214_1_);

		float f = (float) particleAge / (float) particleMaxAge;
		f = f * f;
		f = f * f;
		int j = i & 255;
		int k = i >> 16 & 255;
		k = k + (int) (f * 15.0F * 16.0F);

		if (k > 240) {
			k = 240;
		}

		return j | k << 16;
	}

	@Override
	public void onUpdate() {
		//ticks++;
		//if (ticks < 3) {
		//	return;
		//}
		//BlockPos pos = getPosForParticle();
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		float f = (float) particleAge / (float) particleMaxAge;
		f = 1.0F - f;
		float f1 = 1.0F - f;
		f1 = f1 * f1;
		f1 = f1 * f1;
		posX = coordX + motionX * f;
		posY = coordY + motionY * f - f1 * 1.2F;
		posZ = coordZ + motionZ * f;

		if (particleAge++ >= particleMaxAge) {
			setExpired();
		}

		//posX = pos.getX() + 1.0D;
		//posY = pos.getY();
		//posZ = pos.getZ() + 1.0D;

		//if (degree >= 360) {
		//setExpired();
		//}
		//else {
		//	ticks = 0;
		//}
	}

	public BlockPos getPosForParticle() {
		int height = 2;
		double yIncrement = 0.01;

		double radius = 1.5;

		BlockPos pos = new BlockPos(coordX, coordY, coordZ);

		x = Math.cos(degree) * radius;
		z = Math.sin(degree) * radius;

		BlockPos newPos = new BlockPos((float) (pos.getX() + x), (float) (pos.getY() + y), (float) (pos.getZ() + z));
		degree++;
		if (upOrDown) {
			if (y >= height) {
				upOrDown = false;
				y -= yIncrement;
			}
			else {
				y += yIncrement;
			}
		}
		else {
			if (y <= 0) {
				upOrDown = true;
				y += yIncrement;
			}
			else {
				y -= yIncrement;
			}
		}
		if (degree >= 360) {
			//degree = 0;
		}
		return newPos;
	}

	public static class Enchanter implements IParticleFactory {
		@Override
		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
			return new ParticleEnchanter(worldIn, new BlockPos(xCoordIn, yCoordIn, zCoordIn), xSpeedIn, ySpeedIn, zSpeedIn);
		}
	}
}