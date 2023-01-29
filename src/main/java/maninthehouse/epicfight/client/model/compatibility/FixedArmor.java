package maninthehouse.epicfight.client.model.compatibility;

import com.windanesz.ancientspellcraft.item.ItemASWizardArmour;
import com.windanesz.ancientspellcraft.item.ItemAncientHat;
import com.windanesz.ancientspellcraft.registry.ASItems;
import de.teamlapen.vampirism.client.model.ModelHunterHat;
import de.teamlapen.vampirism.core.ModItems;
import electroblob.wizardry.client.model.ModelRobeArmour;
import electroblob.wizardry.client.model.ModelSageArmour;
import electroblob.wizardry.client.model.ModelWizardArmour;
import electroblob.wizardry.item.ItemWizardArmour;
import electroblob.wizardry.registry.WizardryItems;
import maninthehouse.epicfight.capabilities.entity.CapabilityEntity;
import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninthehouse.epicfight.client.model.compatibility.WitcherEquipment.ModelWitcherHat;
import maninthehouse.epicfight.client.model.compatibility.WizardEquipment.ModelRobeArmor;
import maninthehouse.epicfight.client.model.compatibility.WizardEquipment.ModelSageArmor;
import maninthehouse.epicfight.client.model.compatibility.WizardEquipment.ModelWizardArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static maninthehouse.epicfight.main.LoaderConstants.*;

public class FixedArmor {

    public static final Map<ItemWizardArmour.ArmourClass, ModelBiped> FIXED_ARMOUR_BY_TYPE =
            new HashMap<ItemWizardArmour.ArmourClass, ModelBiped>();
    public static final Map<Class<? extends ModelBiped>, ModelBiped> FIXED_ARMOUR_BY_CLASS =
            new HashMap<Class<? extends ModelBiped>, ModelBiped>();
    public static final Map<ResourceLocation, ModelBiped> FIXED_ARMOUR_BY_NAME =
            new HashMap<ResourceLocation, ModelBiped>();
    public static final float delta = 0.75F;
    public static void makeMap(){
        if (EBWIZARDRY) {
            FIXED_ARMOUR_BY_TYPE.put(ItemWizardArmour.ArmourClass.WIZARD, new ModelWizardArmor(delta));
            FIXED_ARMOUR_BY_TYPE.put(ItemWizardArmour.ArmourClass.SAGE, new ModelSageArmor(delta));
            FIXED_ARMOUR_BY_TYPE.put(ItemWizardArmour.ArmourClass.BATTLEMAGE, new ModelRobeArmor(delta, true));
            FIXED_ARMOUR_BY_TYPE.put(ItemWizardArmour.ArmourClass.WARLOCK, new ModelRobeArmor(delta, false));
            if (ANCIENT_SPELLCRAFT) {
                FIXED_ARMOUR_BY_NAME.put(ASItems.wizard_hat_ancient.getRegistryName(), new ModelWizardArmor(delta));
            }
        }


        if (VAMPIRISM) {
            FIXED_ARMOUR_BY_NAME.put(ModItems.hunter_hat0_head.getRegistryName(), new ModelWitcherHat(1));
            FIXED_ARMOUR_BY_NAME.put(ModItems.hunter_hat1_head.getRegistryName(), new ModelWitcherHat(2));
        }

    }

}
