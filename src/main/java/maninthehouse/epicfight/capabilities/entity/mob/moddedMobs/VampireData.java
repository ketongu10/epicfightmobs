package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import com.windanesz.ancientspellcraft.registry.ASItems;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.entity.hunter.EntityAdvancedHunter;
import de.teamlapen.vampirism.entity.minions.vampire.EntityVampireMinionBase;
import de.teamlapen.vampirism.entity.vampire.EntityAdvancedVampire;
import de.teamlapen.vampirism.entity.vampire.EntityVampireBaron;
import de.teamlapen.vampirism.entity.vampire.EntityVampireBase;
import de.teamlapen.vampirism.items.VampirismVampireSword;
import electroblob.wizardry.registry.WizardryItems;
import io.netty.buffer.ByteBuf;
import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.entity.DataKeys;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.entity.mob.Faction;
import maninthehouse.epicfight.capabilities.entity.mob.MobAttackPatterns;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.entity.ai.EntityAIArcher;
import maninthehouse.epicfight.entity.ai.EntityAIAttackPattern;
import maninthehouse.epicfight.entity.ai.EntityAIChase;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Models;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.network.server.STCMobInitialSetting;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.Mod;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFaction;
import net.shadowmage.ancientwarfare.npc.item.ItemExtendedReachWeapon;
import net.shadowmage.ancientwarfare.npc.item.ItemShield;

import static maninthehouse.epicfight.main.LoaderConstants.EBWIZARDRY;

public class VampireData<T extends EntityVampireBase> extends BipedMobData<T> {
    public VampireData() {
        super(Faction.UNDEAD);
    }

    @Override
    public void onEntityJoinWorld(T entityIn) {
        super.onEntityJoinWorld(entityIn);
        this.orgEntity.getDataManager().register(DataKeys.STUN_ARMOR, Float.valueOf(2.0F*this.orgEntity.getRenderSizeModifier()));//only for hitbox
        if (this.orgEntity instanceof EntityVampireBaron) {
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(de.teamlapen.vampirism.core.ModItems.vampire_cloak));
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.heart_striker));
        } else if (this.orgEntity instanceof EntityAdvancedVampire) {
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.heart_seeker));
        } else if (this.orgEntity instanceof EntityVampireMinionBase) {
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Blocks.REDSTONE_TORCH));
            if (EBWIZARDRY) {
                this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(WizardryItems.warlock_hood_necromancy));
                this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(WizardryItems.warlock_robe_necromancy));
                this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(WizardryItems.warlock_leggings_necromancy));
                this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(WizardryItems.warlock_boots_necromancy));
            }
            notifyToReset((0.5F)*0.6F, 0.5F*1.8F); //only for hitbox
            this.resetSize();
        }



    }


    @Override
    protected void initAnimator(AnimatorClient animatorClient) {
        super.initAnimator(animatorClient);
        if (this.orgEntity instanceof EntityVampireMinionBase) {
            animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.ILLAGER_IDLE);
            animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.ILLAGER_WALK);
        } else {
            animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.BIPED_IDLE);
            animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.BIPED_WALK);
        }
        animatorClient.addLivingAnimation(LivingMotion.FALL, Animations.BIPED_FALL);
        animatorClient.addLivingAnimation(LivingMotion.MOUNT, Animations.BIPED_MOUNT);
        animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.BIPED_DEATH);
        animatorClient.setCurrentLivingMotionsToDefault();
    }


    @Override
    public STCMobInitialSetting sendInitialInformationToClient() {
        STCMobInitialSetting packet = new STCMobInitialSetting(this.orgEntity.getEntityId());
        ByteBuf buf = packet.getBuffer();
        buf.writeBoolean(this.orgEntity.canPickUpLoot());
        return packet;
    }

    @Override
    public void postInit() {
        if (!this.isRemote() ) {
            super.resetCombatAI();
            Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();

            if (heldItem instanceof ItemBow && this.orgEntity instanceof IRangedAttackMob) {
                this.setAIAsRange();
            } else if (this.orgEntity.getRidingEntity() != null
                    && this.orgEntity.getRidingEntity() instanceof EntityMob) {
                this.setAIAsMounted(this.orgEntity.getRidingEntity());
            } else if (isArmed()) {
                this.setAIAsArmed();
            } else {
                this.setAIAsUnarmed();
            }
        }
    }
    @Override
    public boolean isArmed() {
        Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();
        return super.isArmed() || heldItem == ModItems.heart_seeker ;
    }




    @Override
    public void updateMotion() {
        super.commonCreatureUpdateMotion();

    }


    @Override
    public void setAIAsArmed() {
        if (this.orgEntity.getHeldItemMainhand().getItem() instanceof VampirismVampireSword) {
            orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_KATANA)); //BIPED_ARMED_ONEHAND //BIPED_ARMED_SPEAR2
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.2D, true));
        } else {
            orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SWORD)); //BIPED_ARMED_ONEHAND //BIPED_ARMED_SPEAR2
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.2D, true));
        }


    }


    @Override
    public void setAIAsMounted(Entity ridingEntity) {
        Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();
        if ((heldItem instanceof ItemSword && !(heldItem instanceof ItemExtendedReachWeapon))|| heldItem instanceof ItemAxe ){
            orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.BIPED_MOUNT_SWORD));
        } else {
            orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.BIPED_ARMED_SPEAR));
        }
        if (ridingEntity instanceof AbstractHorse) {
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.4D, false));
        }

    }
    @Override
    public void setAIAsRange() {
        int cooldown = this.orgEntity.world.getDifficulty() != EnumDifficulty.HARD ? 40 : 20;
        orgEntity.tasks.addTask(1, new EntityAIArcher(this, this.orgEntity, 1.0D, cooldown, 15.0F));
    }

    @Override
    public void setAIAsUnarmed() {
        orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_CLAWED)); //BIPED_ARMED_ONEHAND //BIPED_ARMED_SPEAR2
        orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.3D, true));
    }
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.registerIfAbsent(ModAttributes.MAX_STUN_ARMOR);
        this.registerIfAbsent(ModAttributes.OFFHAND_ATTACK_DAMAGE);
    }
    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(1.0F);
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.MAX_STUN_ARMOR).setBaseValue(2.0F*this.orgEntity.getRenderSizeModifier());
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.OFFHAND_ATTACK_DAMAGE).setBaseValue(this.orgEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
    }



    @Override
    public <M extends Model> M getEntityModel(Models<M> modelDB) {
        return modelDB.ENTITY_BIPED_64_32_TEX;
    }


    /**
     *костыль
     */
    @Override
    public StaticAnimation getHitAnimation(IExtendedDamageSource.StunType stunType) {
            if (orgEntity.getRidingEntity() != null) {
                return Animations.BIPED_HIT_ON_MOUNT;
            } else {
                switch (stunType) {
                    case LONG:
                        return Animations.BIPED_HIT_LONG;
                    case SHORT:
                        return Animations.BIPED_HIT_SHORT;
                    case HOLD:
                        return Animations.BIPED_HIT_SHORT;
                    default:
                        return null;
                }
            }


    }


}
