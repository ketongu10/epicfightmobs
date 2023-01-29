package maninthehouse.epicfight.client.model.compatibility.WitcherEquipment;

import electroblob.wizardry.client.model.ModelWizardArmour;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWitcherHat extends ModelBiped {

    ModelRenderer hatTop;
    ModelRenderer hatRim;

    
    public ModelWitcherHat(int type) {
        super(0.0F, 0.0F, 64, 64);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.6f);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + 0, 0.0F);
        if (type == 1) {
            hatTop = new ModelRenderer(this, 0, 31);
            hatTop.addBox(-4F, -13F, -4F, 8, 5, 8);//y was -14
            hatTop.setRotationPoint(super.bipedHead.rotationPointX, super.bipedHead.rotationPointY, super.bipedHead.rotationPointZ);
            hatTop.setTextureSize(128, 64);
            hatTop.mirror = true;

            hatRim = new ModelRenderer(this, 0, 35);
            hatRim.addBox(-6F, -8F, -6F, 12, 1, 12);//y was -9
            hatRim.setRotationPoint(super.bipedHead.rotationPointX, super.bipedHead.rotationPointY, super.bipedHead.rotationPointZ);
            hatRim.setTextureSize(128, 64);
            hatRim.mirror = true;
        } else if (type == 2) {
            hatTop = new ModelRenderer(this, 0, 31);
            hatTop.addBox(-4F, -11F, -4F, 8, 3, 8);
            hatTop.setRotationPoint(super.bipedHead.rotationPointX, super.bipedHead.rotationPointY, super.bipedHead.rotationPointZ);
            hatTop.setTextureSize(128, 64);
            hatTop.mirror = true;

            hatRim = new ModelRenderer(this, 0, 31);
            hatRim.addBox(-8F, -8F, -8F, 16, 1, 16);
            hatRim.setRotationPoint(super.bipedHead.rotationPointX, super.bipedHead.rotationPointY, super.bipedHead.rotationPointZ);
            hatRim.setTextureSize(128, 64);
            hatRim.mirror = true;
        }

        bipedHead.addChild(hatTop);
        bipedHead.addChild(hatRim);

    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        //hatTop.render(scale);
        //hatRim.render(scale);
        //setRotationAngles(limbSwing,limbSwingAmount, ageInTicks,  netHeadYaw,  headPitch,  scale, entityIn);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z){
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6, Entity e) {
        super.setRotationAngles(f1, f2, f3, f4, f5, f6, e);
        /**hatRim.rotateAngleX = super.bipedHead.rotateAngleX/2;
        hatRim.rotateAngleY = super.bipedHead.rotateAngleY/2;
        hatRim.rotateAngleZ = super.bipedHead.rotateAngleZ/2;
        hatTop.rotateAngleX = super.bipedHead.rotateAngleX/2;
        hatTop.rotateAngleY = super.bipedHead.rotateAngleY/2;
        hatTop.rotateAngleZ = super.bipedHead.rotateAngleZ/2;**/
    }
    @Override
    public void setVisible(boolean invisible) {
        super.setVisible(false);
        hatRim.showModel = true;
        hatTop.showModel = true;
    }
}
