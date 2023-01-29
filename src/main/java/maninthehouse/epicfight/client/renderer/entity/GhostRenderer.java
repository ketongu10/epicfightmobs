package maninthehouse.epicfight.client.renderer.entity;

import electroblob.wizardry.entity.living.EntitySpectralGolem;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.shadowmage.ancientwarfare.npc.entity.NpcBase;
import org.lwjgl.opengl.GL11;

public class GhostRenderer <E extends EntityLivingBase, T extends LivingData<E>> extends BipedRenderer<E, T> {

    public final ResourceLocation textureLocation;

    public GhostRenderer(String texturePath) {
        textureLocation = new ResourceLocation(texturePath);
    }

    @Override
    protected ResourceLocation getEntityTexture(E entityIn) {
        return textureLocation;
    }

    @Override
    public void render(E entityIn, T entitydata, RenderLivingBase<E> renderer, double x, double y, double z, float partialTicks) {

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1, 1, 1, 0.5F);

        super.render(entityIn, entitydata, renderer, x, y, z, partialTicks);
    }
}
