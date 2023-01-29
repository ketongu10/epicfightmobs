package maninthehouse.epicfight.client.model.compatibility.WizardEquipment;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelBeard extends ModelBiped {
    ModelRenderer beard;

    public ModelBeard() {


        super(0, 0, 64,64);

        this.bipedHead = new ModelRenderer(this, 0, 32);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.6f);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 32);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.6f);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);

        beard = new ModelRenderer(this, 32, 0);
        beard.addBox(0F, 0F, 0F, 8, 16, 0);
        beard.setRotationPoint(-4F, 0F, -4F);
        beard.setTextureSize(64, 64);
        beard.mirror = true;
        setRotation(beard, 0F, 0F, 0F);
        this.bipedHead.addChild(beard);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z){
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
