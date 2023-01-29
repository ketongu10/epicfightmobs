package maninthehouse.epicfight.client.renderer.item;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ancientspellcraft.registry.ASItems;
import maninthehouse.epicfight.capabilities.ModCapabilities;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.item.ArmorCapability;
import maninthehouse.epicfight.client.ClientEngine;
import maninthehouse.epicfight.client.model.ClientModel;
import maninthehouse.epicfight.client.model.ClientModels;
import maninthehouse.epicfight.client.model.compatibility.WitcherEquipment.ItemWitcherBackpack;
import maninthehouse.epicfight.client.model.compatibility.WitcherEquipment.ModelWitcherArmor;
import maninthehouse.epicfight.client.model.custom.CustomModelBakery;
import maninthehouse.epicfight.client.renderer.layer.WearableItemLayer;
import maninthehouse.epicfight.item.ModItems;
import maninthehouse.epicfight.main.EpicFightMod;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.utils.math.MathUtils;
import maninthehouse.epicfight.utils.math.Vec3f;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import static maninthehouse.epicfight.main.LoaderConstants.ANCIENT_SPELLCRAFT;

public class RenderWitcherBackpack extends RenderItemBase {
    private ItemStack human = new ItemStack(Items.IRON_SWORD);
    private ItemStack wizard = ANCIENT_SPELLCRAFT ? new ItemStack(ASItems.devoritium_sword) : new ItemStack(Items.IRON_SWORD);
    public final ModelWitcherArmor model = new ModelWitcherArmor();


    @Override
    public void renderItemOnHead(ItemStack stack, LivingData<?> itemHolder, float partialTicks) {
        //super.renderItemOnHead(stack, itemHolder, partialTicks);
        switch (((ItemWitcherBackpack)stack.getItem()).display) {
            case 0:
                renderHuman(stack, itemHolder, partialTicks);
                renderWizard(stack, itemHolder, partialTicks);
            case 1:
                renderWizard(stack, itemHolder, partialTicks);
            case 2:
                renderHuman(stack, itemHolder, partialTicks);
            default:
                renderWizard(stack, itemHolder, partialTicks);
        }
        /**EntityLivingBase entity = itemHolder.getOriginalEntity();
        VisibleMatrix4f modelMatrix = new VisibleMatrix4f();
        //VisibleMatrix4f.scale(new Vec3f(-0.9F, -0.9F, 0.9F), modelMatrix, modelMatrix);
        VisibleMatrix4f.translate(new Vec3f(0F, 0.0F, 0.0F), modelMatrix, modelMatrix);
        VisibleMatrix4f.mul(itemHolder.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature().findJointById(8).getAnimatedTransform(), modelMatrix, modelMatrix);
        GlStateManager.multMatrix(modelMatrix.toFloatBuffer());


        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(stack.getItem().getArmorTexture(stack, entity, EntityEquipmentSlot.CHEST, "1")));


        float f = MathUtils.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        float f1 = MathUtils.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        float f2 = f1 - f;
        float f7 = itemHolder.getPitch(partialTicks);

        this.model.isChild = entity.isChild();
        this.model.setRotationAngles(entity.limbSwing, entity.limbSwingAmount, entity.ticksExisted, f2, f7, 0.0625F, entity);
        this.model.render(entity, entity.limbSwing, entity.limbSwingAmount, entity.ticksExisted, f2, f7, 0.0625F);**/
    }


    private void renderHuman(ItemStack stack, LivingData<?> itemHolder, float partialTicks) {
        GlStateManager.pushMatrix();
        VisibleMatrix4f modelMatrix = new VisibleMatrix4f(this.correctionMatrix);
        VisibleMatrix4f.rotate((float) Math.toRadians(90), new Vec3f(0, 0, 1), modelMatrix, modelMatrix);
        VisibleMatrix4f.rotate((float) Math.toRadians(-55), new Vec3f(1, 0, 0), modelMatrix, modelMatrix);
        VisibleMatrix4f.translate(new Vec3f(-0.25F, -0.6F, -0.1F), modelMatrix, modelMatrix);
        VisibleMatrix4f.mul(itemHolder.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature().findJointById(8).getAnimatedTransform(), modelMatrix, modelMatrix);
        GlStateManager.multMatrix(modelMatrix.toFloatBuffer());
        Minecraft.getMinecraft().getItemRenderer().renderItem(itemHolder.getOriginalEntity(), this.human, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        GlStateManager.popMatrix();
    }
    private void renderWizard(ItemStack stack, LivingData<?> itemHolder, float partialTicks) {
        GlStateManager.pushMatrix();
        VisibleMatrix4f modelMatrix = new VisibleMatrix4f(this.correctionMatrix);
        VisibleMatrix4f.rotate((float) Math.toRadians(90), new Vec3f(0, 0, 1), modelMatrix, modelMatrix);
        VisibleMatrix4f.rotate((float) Math.toRadians(-55), new Vec3f(1, 0, 0), modelMatrix, modelMatrix);
        VisibleMatrix4f.translate(new Vec3f(-0.25F, -0.6F, 0.1F), modelMatrix, modelMatrix);
        VisibleMatrix4f.mul(itemHolder.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature().findJointById(8).getAnimatedTransform(), modelMatrix, modelMatrix);
        GlStateManager.multMatrix(modelMatrix.toFloatBuffer());
        Minecraft.getMinecraft().getItemRenderer().renderItem(itemHolder.getOriginalEntity(), this.wizard, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        GlStateManager.popMatrix();
    }
}
