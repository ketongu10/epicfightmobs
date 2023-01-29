package maninthehouse.epicfight.client.renderer.entity;

import de.teamlapen.vampirism.entity.vampire.EntityBasicVampire;
import electroblob.wizardry.entity.living.EntityEvilWizard;
import electroblob.wizardry.entity.living.EntityWizard;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.client.renderer.layer.BeardLayer;
import maninthehouse.epicfight.main.EpicFightMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class VampireRenderer<E extends EntityLivingBase, T extends LivingData<E>> extends BipedRenderer<E, T>{

    private final ResourceLocation[] TEXTURES = new ResourceLocation[17];

    public VampireRenderer() {
        super();
        for(int i = 1; i < 18; i++){
                TEXTURES[i-1] = new ResourceLocation("vampirism:textures/entity/vampire" + i + ".png");
        }
        //TEXTURES[18] = new ResourceLocation("vampirism:textures/entity/advanced_vampire.png");
        //TEXTURES[19] = new ResourceLocation("vampirism:textures/entity/dracula.png");
    }
    @Override
    protected ResourceLocation getEntityTexture(E entityIn) {
        return TEXTURES[entityIn.getEntityId()%17];

    }
}
