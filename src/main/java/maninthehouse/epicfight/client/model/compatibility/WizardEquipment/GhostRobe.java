package maninthehouse.epicfight.client.model.compatibility.WizardEquipment;

import com.windanesz.ancientspellcraft.entity.living.EntityEvilClassWizard;
import electroblob.wizardry.entity.living.EntityEvilWizard;
import electroblob.wizardry.entity.living.EntityWizard;
import electroblob.wizardry.item.ItemWizardArmour;
import maninthehouse.epicfight.main.EpicFightMod;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GhostRobe extends ItemArmor {

    private ResourceLocation TEXTURE = new ResourceLocation(EpicFightMod.MODID+":textures/models/armor/ghost_robe.png");

    public GhostRobe(ArmorMaterial material, int renderIndex, EntityEquipmentSlot armourType){
        super(material, renderIndex, armourType);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armourSlot, net.minecraft.client.model.ModelBiped original){
        return new ModelRobeArmor(0.75F, false);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
        return TEXTURE.toString();

    }



}
