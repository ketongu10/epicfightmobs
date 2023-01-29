package maninthehouse.epicfight.client.model.compatibility.WitcherEquipment;

import de.teamlapen.vampirism.client.model.ModelBipedCloaked;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ModelCape extends ModelBase {
    protected ModelRenderer bipedCloak;

    public ModelCape (int x, int y) {
        super();
        this.textureHeight = 64;
        this.bipedCloak = new ModelRenderer(this, x, y);//0. 32
        this.bipedCloak.addBox(-7.0F, 0.0F, 0.4F, 14, 20, 1);
        this.bipedCloak.setRotationPoint(0.0F, 0.0F, 2.0F);
        setRotation(bipedCloak, (float) Math.toRadians(-15.0F), 0f, 0f);
    }
    private void setRotation(ModelRenderer model, float x, float y, float z){
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableCull();

        if (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).isChild()) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 15.5F, -0.1F);
            this.bipedCloak.render(scale);
            GlStateManager.popMatrix();
        } else {
            this.bipedCloak.render(scale);
        }
    }
}
