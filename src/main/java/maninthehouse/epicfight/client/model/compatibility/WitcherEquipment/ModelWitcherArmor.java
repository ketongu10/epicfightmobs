package maninthehouse.epicfight.client.model.compatibility.WitcherEquipment;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;

public class ModelWitcherArmor extends ModelBiped {

    ModelRenderer holder1;
    ModelRenderer holder2;
    ModelRenderer belt1;
    public ModelWitcherArmor() {
        /**super(0.0F, 0.0F, 64, 64);
        this.bipedBody = new ModelRenderer(this, 0, 0);
        this.bipedBody.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0f);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + 0, 0.0F);

        holder1 = new ModelRenderer(this, 0, 32);
        holder1.addBox(-0.5F, -1.0F, -1.0F, 3, 4, 2, 0);
        holder1.setRotationPoint(-0.5F, 1.0F, 3.0F);
        setRotation(holder1, 0.0F, 0.0F, -0.4363F);

        holder2 = new ModelRenderer(this, 0, 32);
        holder2.addBox(-0.5F, -1.0F, -1.0F, 3, 4, 2, 0);
        holder2.setRotationPoint(-3.5F, 3.0F, 3.0F);
        setRotation(holder2, 0.0F, 0.0F, -0.4363F);
        
        belt1 = new ModelRenderer(this,11, 35);
        belt1.addBox(-1.0F, -1.0F, -0.5F, 2, 1, 1, 0.0F);
        belt1.setRotationPoint(0.1817F, 4.032F, 2.5F);
        setRotation(belt1, 0.0F, 0.0F, -0.7418F);

                


        this.bipedBody.addChild(holder1);
        this.bipedBody.addChild(holder2);
        this.bipedBody.addChild(belt1);***/
    }

    private void setRotation(ModelRenderer model, float x, float y, float z){
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
