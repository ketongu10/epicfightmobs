package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import com.windanesz.ancientspellcraft.entity.living.EntityEvilClassWizard;
import com.windanesz.ancientspellcraft.registry.ASItems;
import electroblob.wizardry.client.model.ModelWizard;
import electroblob.wizardry.client.model.WizardryModels;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.entity.living.EntityEvilWizard;
import electroblob.wizardry.entity.living.EntityWizard;
import electroblob.wizardry.registry.WizardryBlocks;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import io.netty.buffer.ByteBuf;
import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.capabilities.entity.DataKeys;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.entity.mob.Faction;
import maninthehouse.epicfight.capabilities.entity.mob.MobAttackPatterns;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.config.EntityConfigurationCapability;
import maninthehouse.epicfight.entity.ai.EntityAIAttackPattern;
import maninthehouse.epicfight.entity.ai.EntityAIChase;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Models;
import maninthehouse.epicfight.item.ModItems;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.network.server.STCMobInitialSetting;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;

import static maninthehouse.epicfight.main.LoaderConstants.EBWIZARDRY;

public class WizardData<T extends EntityCreature> extends BipedMobData<T> {
    int isCast;
    float impact = 4.0F;

    public WizardData() {
        super(Faction.NATURAL);
        isCast = 0;
    }

    public WizardData(float impact) {
        this();
        this.impact = impact;
    }

    @Override
    public void onEntityJoinWorld(T entityIn) {
        super.onEntityJoinWorld(entityIn);
        this.orgEntity.getDataManager().register(DataKeys.STUN_ARMOR, Float.valueOf((impact)));

    }

    @Override
    protected void initAnimator(AnimatorClient animatorClient) {
        super.initAnimator(animatorClient);
        animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.BIPED_IDLE);
        animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.BIPED_WALK);
        animatorClient.addLivingAnimation(LivingMotion.FALL, Animations.BIPED_FALL);
        animatorClient.addLivingAnimation(LivingMotion.MOUNT, Animations.BIPED_MOUNT);
        animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.BIPED_DEATH);
        animatorClient.addLivingAnimation(LivingMotion.SUMMONNING, Animations.SWORD_DASH);
        animatorClient.addLivingMixAnimation(LivingMotion.SPELLCASTING, Animations.MAGIC_DASH_WAND);
        animatorClient.setCurrentLivingMotionsToDefault();
    }

    @Override
    public void updateMotion() {
        super.commonCreatureUpdateMotion();
        if (isCast>0) {
            currentMixMotion = LivingMotion.SPELLCASTING;
            isCast--;
        } else {currentMixMotion = LivingMotion.NONE;}
    }



    @Override
    public STCMobInitialSetting sendInitialInformationToClient() {
        STCMobInitialSetting packet = new STCMobInitialSetting(this.orgEntity.getEntityId());
        ByteBuf buf = packet.getBuffer();
        buf.writeBoolean(this.orgEntity.canPickUpLoot());
        return packet;
    }


    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.registerIfAbsent(ModAttributes.MAX_STUN_ARMOR);
        //this.registerIfAbsent(ModAttributes.OFFHAND_ATTACK_DAMAGE);
    }
    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(1.0F);
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.MAX_STUN_ARMOR).setBaseValue(100.0F);
        //this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.OFFHAND_ATTACK_DAMAGE).setBaseValue(this.orgEntity.getAttributeMap().getAttributeInstance(1.0D).getAttributeValue());
    }


    @Override
    public <M extends Model> M getEntityModel(Models<M> modelDB) {
        return modelDB.ENTITY_BIPED_64_32_TEX;
    }

    @Override
    public void postInit() {
        if (this.orgEntity.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
            setAIAsBattleWizard();
        }
    }

    private void setAIAsBattleWizard() {
        orgEntity.tasks.addTask(3, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SWORD));
        orgEntity.tasks.addTask(3, new EntityAIChase(this, this.orgEntity, 1.0D, false));
    }

    @Override
    public void onEntityCast(Spell spell) {
        if (spell.isContinuous) {
            setCast(60);
        } else if (spell.getType() == SpellType.MINION) {
            playAnimationSynchronize(Animations.EVOKER_CAST_SPELL, 0); //ТАК РАБОТАЕТ!!!SWORD_DASH
        } else {
            playAnimationSynchronize(Animations.MAGIC_SWING, 0); //ТАК РАБОТАЕТ!!!
        }

    }

    public void setCast(int a)
    {
        this.isCast = a;
    }

    @Override
    public void setAIAsArmed() {

    }





}