package maninthehouse.epicfight.client.renderer.entity;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.EntityEvilWizard;
import electroblob.wizardry.entity.living.EntityWizard;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.client.ClientEngine;
import maninthehouse.epicfight.client.events.engine.RenderEngine;
import maninthehouse.epicfight.client.model.ClientModels;
import maninthehouse.epicfight.client.model.compatibility.WizardEquipment.ItemBeard;
import maninthehouse.epicfight.client.renderer.item.RenderElytra;
import maninthehouse.epicfight.client.renderer.layer.BeardLayer;
import maninthehouse.epicfight.client.renderer.layer.EyeLayer;
import maninthehouse.epicfight.client.renderer.layer.WearableItemLayer;
import maninthehouse.epicfight.item.ModItems;
import maninthehouse.epicfight.main.EpicFightMod;
import maninthehouse.epicfight.main.proxy.ClientProxy;
import maninthehouse.epicfight.utils.math.Vec3f;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.shadowmage.ancientwarfare.npc.entity.NpcBase;

public class WizardRenderer<E extends EntityLivingBase, T extends LivingData<E>> extends BipedRenderer<E, T> {

    private final ResourceLocation[] TEXTURES = new ResourceLocation[6];
    private boolean isEvil;

    public WizardRenderer(boolean evil) {
        super();
        this.layers.add(new BeardLayer<>(EntityEquipmentSlot.HEAD));
        isEvil = evil;
        if (!evil) {
            for(int i = 0; i < 6; i++){
                TEXTURES[i] = new ResourceLocation(EpicFightMod.MODID, "textures/entity/wizard/wizard_" + i + ".png");
            }
        } else {
            for(int i = 0; i < 6; i++){
                TEXTURES[i] = new ResourceLocation(EpicFightMod.MODID, "textures/entity/evil_wizard/evil_wizard_" + i + ".png");
            }
        }

    }

    @Override
    protected ResourceLocation getEntityTexture(E entityIn) {
        return isEvil ? TEXTURES[((EntityEvilWizard)entityIn).textureIndex] : TEXTURES[((EntityWizard)entityIn).textureIndex];
    }



}