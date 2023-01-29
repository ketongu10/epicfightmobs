package maninthehouse.epicfight.client.renderer.entity;

import chumbanotz.mutantbeasts.entity.EndersoulCloneEntity;
import electroblob.wizardry.entity.living.EntitySpectralGolem;
import maninthehouse.epicfight.animation.AnimationPlayer;
import maninthehouse.epicfight.animation.Joint;
import maninthehouse.epicfight.animation.types.attack.AttackAnimation;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.capabilities.entity.mob.EndermanData;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.client.model.ClientModel;
import maninthehouse.epicfight.client.model.ClientModels;
import maninthehouse.epicfight.client.renderer.layer.EyeLayer;
import maninthehouse.epicfight.client.renderer.layer.HeldItemLayer;
import maninthehouse.epicfight.client.renderer.layer.WearableItemLayer;
import maninthehouse.epicfight.model.Armature;
import maninthehouse.epicfight.physics.Collider;
import maninthehouse.epicfight.utils.math.Vec3f;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import static maninthehouse.epicfight.main.LoaderConstants.MUTANT_BEASTS;

@SideOnly(Side.CLIENT)
public class EndermanRenderer<E extends EntityMob, T extends EndermanData<E>> extends ArmatureRenderer<E, T> {
	private ResourceLocation ENDERMAN_TEXTURE;
	private static final ResourceLocation ENDERMAN_EYE_TEXTURE = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
	
	public EndermanRenderer(ResourceLocation resourceLocation) {
		ENDERMAN_TEXTURE = resourceLocation;
		this.layers.add(new EyeLayer<>(ENDERMAN_EYE_TEXTURE));
		this.layers.add(new HeldItemLayer<>());
	}
	
	@Override
	protected void applyRotations(Armature armature, E entityIn, T entitydata, double x, double y, double z, float partialTicks) {
		super.applyRotations(armature, entityIn, entitydata, x, y, z, partialTicks);
		this.transformJoint(15, armature, entitydata.getHeadMatrix(partialTicks));
		
		if (entitydata.isRaging()) {
			VisibleMatrix4f head = new VisibleMatrix4f();
			VisibleMatrix4f.translate(new Vec3f(0, 0.25F, 0), head, head);
			transformJoint(16, armature, head);
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityMob entityIn) {
		return ENDERMAN_TEXTURE;
	}

	@Override
	public void render(E entityIn, T entitydata, RenderLivingBase<E> renderer, double x, double y, double z, float partialTicks) {
		if (MUTANT_BEASTS) {
			if (entityIn instanceof EndersoulCloneEntity) {
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(0.9F, 0.3F, 1.0F, 0.7F);
			}
		}
		super.render(entityIn, entitydata, renderer, x, y, z, partialTicks);
	}
}