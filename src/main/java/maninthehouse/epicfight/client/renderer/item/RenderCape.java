package maninthehouse.epicfight.client.renderer.item;

import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.client.model.ClientModels;
import maninthehouse.epicfight.client.model.compatibility.WitcherEquipment.ModelCape;
import maninthehouse.epicfight.main.EpicFightMod;
import maninthehouse.epicfight.utils.math.MathUtils;
import maninthehouse.epicfight.utils.math.Vec3f;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderCape extends RenderItemBase {

    private ResourceLocation TEXTURE;
    private final ModelCape model;

    public RenderCape(int x, int y, ResourceLocation tex) {
        super();
        model = new ModelCape(x, y);
        TEXTURE = tex;
    }


    @Override
    public void renderItemOnHead(ItemStack stack, LivingData<?> itemHolder, float partialTicks) {
        EntityLivingBase entity = itemHolder.getOriginalEntity();
        VisibleMatrix4f modelMatrix = new VisibleMatrix4f();
        VisibleMatrix4f.translate(new Vec3f(0F, -0.8F, 0.3F), modelMatrix, modelMatrix);
        VisibleMatrix4f.mul(itemHolder.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature().findJointById(8).getAnimatedTransform(), modelMatrix, modelMatrix);
        GlStateManager.multMatrix(modelMatrix.toFloatBuffer());


        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);


        float f = MathUtils.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        float f1 = MathUtils.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        float f2 = f1 - f;
        float f7 = itemHolder.getPitch(partialTicks);

        this.model.isChild = entity.isChild();
        this.model.setRotationAngles(entity.limbSwing, entity.limbSwingAmount, entity.ticksExisted, f2, f7, 0.0625F, entity);
        this.model.render(entity, entity.limbSwing, entity.limbSwingAmount, entity.ticksExisted, f2, f7, 0.0625F);
    }
}
