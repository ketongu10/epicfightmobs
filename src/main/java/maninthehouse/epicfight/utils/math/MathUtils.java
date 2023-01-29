package maninthehouse.epicfight.utils.math;


import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Quaternion;

public class MathUtils {
	public static VisibleMatrix4f getModelMatrixIntegrated(float prevPosX, float posX, float prevPosY, float posY,
			float prevPosZ, float posZ, float prevPitch, float pitch, float prevYaw, float yaw, float partialTick,
			float scaleX, float scaleY, float scaleZ) {
		VisibleMatrix4f modelMatrix = new VisibleMatrix4f().setIdentity();
		Vec3f entityPosition = new Vec3f(-(prevPosX + (posX - prevPosX) * partialTick),
				((prevPosY + (posY - prevPosY) * partialTick)), -(prevPosZ + (posZ - prevPosZ) * partialTick));
		
		VisibleMatrix4f.translate(entityPosition, modelMatrix, modelMatrix);
		float pitchDegree = interpolateRotation(prevPitch, pitch, partialTick);
		float yawDegree = interpolateRotation(prevYaw, yaw, partialTick);
		VisibleMatrix4f.rotate((float) -Math.toRadians(yawDegree), new Vec3f(0, 1, 0), modelMatrix, modelMatrix);
		VisibleMatrix4f.rotate((float) -Math.toRadians(pitchDegree), new Vec3f(1, 0, 0), modelMatrix, modelMatrix);
		VisibleMatrix4f.scale(new Vec3f(scaleX, scaleY, scaleZ), modelMatrix, modelMatrix);
		
		return modelMatrix;
	}

	public static Vec3d getVectorForRotation(float pitch, float yaw) {
		float f = pitch * ((float) Math.PI / 180F);
		float f1 = -yaw * ((float) Math.PI / 180F);
		float f2 = MathHelper.cos(f1);
		float f3 = MathHelper.sin(f1);
		float f4 = MathHelper.cos(f);
		float f5 = MathHelper.sin(f);

		return new Vec3d((double) (f3 * f4), (double) (-f5), (double) (f2 * f4));
	}

	public static float interpolateRotation(float par1, float par2, float par3) {
		float f = 0;

		for (f = par2 - par1; f < -180.0F; f += 360.0F) {
			;
		}

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return par1 + par3 * f;
	}

	public static float getInterpolatedRotation(float par1, float par2, float par3) {
		float f = 0;

		for (f = par2 - par1; f < -180.0F; f += 360.0F) {
			;
		}

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return par3 * f;
	}

	public static double getAngleBetween(Entity e1, Entity e2) {
		Vec3d a = e1.getLookVec();
		Vec3d b = new Vec3d(e2.posX - e1.posX, e2.posY - e1.posY, e2.posZ - e1.posZ).normalize();
		double cosTheta = (a.x * b.x + a.y * b.y + a.z * b.z);
		return Math.acos(cosTheta);
	}

	public static double lerp(double pct, double start, double end) {
		return start + pct * (end - start);
	}

	public static void translateStack(VisibleMatrix4f mat) {
		Vec3f vector = getTranslationFromMatrix(mat);
		GlStateManager.translate(vector.x, vector.y, vector.z);
	}

	public static void rotateStack(VisibleMatrix4f mat) {
		GlStateManager.rotate(getQuaternionFromMatrix(mat));
	}

	private static Vec3f getTranslationFromMatrix(VisibleMatrix4f mat) {
		return new Vec3f(mat.m30, mat.m31, mat.m32);
	}

	private static Quaternion getQuaternionFromMatrix(VisibleMatrix4f mat) {
		float w, x, y, z;
		float diagonal = mat.m00 + mat.m11 + mat.m22;

		if (diagonal > 0) {
			float w4 = (float) (Math.sqrt(diagonal + 1.0F) * 2.0F);
			w = w4 * 0.25F;
			x = (mat.m21 - mat.m12) / w4;
			y = (mat.m02 - mat.m20) / w4;
			z = (mat.m10 - mat.m01) / w4;
		} else if ((mat.m00 > mat.m11) && (mat.m00 > mat.m22)) {
			float x4 = (float) (Math.sqrt(1.0F + mat.m00 - mat.m11 - mat.m22) * 2F);
			w = (mat.m21 - mat.m12) / x4;
			x = x4 * 0.25F;
			y = (mat.m01 + mat.m10) / x4;
			z = (mat.m02 + mat.m20) / x4;
		} else if (mat.m11 > mat.m22) {
			float y4 = (float) (Math.sqrt(1.0F + mat.m11 - mat.m00 - mat.m22) * 2F);
			w = (mat.m02 - mat.m20) / y4;
			x = (mat.m01 + mat.m10) / y4;
			y = y4 * 0.25F;
			z = (mat.m12 + mat.m21) / y4;
		} else {
			float z4 = (float) (Math.sqrt(1.0F + mat.m22 - mat.m00 - mat.m11) * 2F);
			w = (mat.m10 - mat.m01) / z4;
			x = (mat.m02 + mat.m20) / z4;
			y = (mat.m12 + mat.m21) / z4;
			z = z4 * 0.25F;
		}

		Quaternion quat = new Quaternion(x, y, z, w);
		quat.normalise();
		return quat;
	}
	public static void scaleStack(VisibleMatrix4f mat) {
		Vec3f vector = getScaleFromMatrix(mat);
		GlStateManager.scale(vector.x, vector.y, vector.z);
	}
	private static Vec3f getScaleFromMatrix(VisibleMatrix4f mat) {
		Vec3f a = new Vec3f(mat.m00, mat.m10, mat.m20);
		Vec3f b = new Vec3f(mat.m01, mat.m11, mat.m21);
		Vec3f c = new Vec3f(mat.m02, mat.m12, mat.m22);
		return new Vec3f(a.length(), b.length(), c.length());
	}
}