package maninthehouse.epicfight.client.renderer.entity;

import com.windanesz.ancientspellcraft.AncientSpellcraft;
import com.windanesz.ancientspellcraft.entity.living.EntityEvilClassWizard;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.EntityEvilWizard;
import electroblob.wizardry.entity.living.EntityWizard;
import electroblob.wizardry.item.ItemWizardArmour;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.client.renderer.layer.BeardLayer;
import maninthehouse.epicfight.main.EpicFightMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class EntityClassWizardRenderer<E extends EntityLivingBase, T extends LivingData<E>> extends BipedRenderer<E, T> {

    private final ResourceLocation[] TEXTURES = new ResourceLocation[24];

    public EntityClassWizardRenderer() {
        super();
        this.layers.add(new BeardLayer<>(EntityEquipmentSlot.HEAD));
        int totalCount = 0;
        for (int i = 0; i < 4; i++) { // 4 class types
            for (int j = 0; j < 6; j++) { // 6 skin per type
                TEXTURES[totalCount] = new ResourceLocation(EpicFightMod.MODID, "textures/entity/evil_class_wizard/evil_"
                        + ((ItemWizardArmour.ArmourClass.values()[i].name().toLowerCase() + "_" + j) + ".png"));
                totalCount++;
            }
        }

    }

    @Override
    protected ResourceLocation getEntityTexture(E entityIn) {
        int i = ((EntityEvilClassWizard)entityIn).getArmourClass().ordinal();
        int j = i * 6;
        int k = ((EntityEvilClassWizard)entityIn).textureIndex;
        int res = j + k;
        return TEXTURES[(((EntityEvilClassWizard)entityIn).getArmourClass().ordinal() * 6 + ((EntityEvilClassWizard)entityIn).textureIndex)];
    }


}
