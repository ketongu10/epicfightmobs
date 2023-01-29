package maninthehouse.epicfight.client.renderer.entity;

import electroblob.wizardry.client.renderer.RenderSpectralGolem;
import electroblob.wizardry.entity.living.EntitySpectralGolem;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.capabilities.entity.mob.IronGolemData;
import maninthehouse.epicfight.model.Armature;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import static maninthehouse.epicfight.main.LoaderConstants.EBWIZARDRY;

@SideOnly(Side.CLIENT)
public class GolemRenderer<E extends EntityIronGolem, T extends IronGolemData<E>> extends ArmatureRenderer<E, T> {
	public final ResourceLocation GOLEM_TEXTURE;

	public GolemRenderer(String texturePath) {
		GOLEM_TEXTURE = new ResourceLocation(texturePath);}
	
	@Override
	protected void applyRotations(Armature armature, E entityIn, T entitydata, double x, double y, double z, float partialTicks) {
        super.applyRotations(armature, entityIn, entitydata, x, y, z, partialTicks);
        transformJoint(2, armature, entitydata.getHeadMatrix(partialTicks));
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityIronGolem entityIn) {
		return GOLEM_TEXTURE;
	}

	@Override
	public void render(E entityIn, T entitydata, RenderLivingBase<E> renderer, double x, double y, double z, float partialTicks) {
		if (EBWIZARDRY && entityIn instanceof EntitySpectralGolem) {
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1, 1, 1, 0.7F);
		}
		super.render(entityIn, entitydata, renderer, x, y, z, partialTicks);
	}
}