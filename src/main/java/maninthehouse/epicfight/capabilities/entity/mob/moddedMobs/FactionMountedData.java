package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import electroblob.wizardry.entity.living.EntityWizard;
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
import maninthehouse.epicfight.item.SpearItem;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.network.server.STCMobInitialSetting;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.math.Vec3f;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.Loader;
import net.shadowmage.ancientwarfare.npc.ai.NpcAIAttack;
import net.shadowmage.ancientwarfare.npc.ai.NpcAIAttackMeleeLongRange;
import net.shadowmage.ancientwarfare.npc.ai.NpcAIBlockWithShield;
import net.shadowmage.ancientwarfare.npc.ai.faction.NpcAIFactionRideHorse;
import net.shadowmage.ancientwarfare.npc.entity.NpcBase;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFaction;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFactionMounted;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFactionSoldier;
import net.shadowmage.ancientwarfare.npc.item.ItemExtendedReachWeapon;
import net.shadowmage.ancientwarfare.npc.item.ItemShield;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FactionMountedData<T extends NpcFactionMounted> extends BipedMobData<T> {


    public FactionMountedData() {
        super(Faction.NATURAL);
    }

        @Override
        public void onEntityJoinWorld(T entityIn) {
            super.onEntityJoinWorld(entityIn);
            notifyToReset((this.orgEntity.getWidthModifier())*0.6F, this.orgEntity.getRenderSizeModifier()*1.8F);
            this.resetSize();
            this.orgEntity.getDataManager().register(DataKeys.STUN_ARMOR, Float.valueOf(10.0F));
        }


        @Override
        protected void initAnimator(AnimatorClient animatorClient) {
            super.initAnimator(animatorClient);
            animatorClient.addLivingAnimation(LivingMotion.FALL, Animations.BIPED_FALL);
            animatorClient.addLivingAnimation(LivingMotion.MOUNT, Animations.BIPED_MOUNT);
            animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.BIPED_DEATH);
            animatorClient.addLivingMixAnimation(LivingMotion.BLOCKING, Animations.BIPED_BLOCK);
            animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.BIPED_IDLE);
            animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.BIPED_WALK);
            //}
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
            if (!this.isRemote()) {
                super.resetCombatAI();
                //resetRidingAI();
                Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();

                if (heldItem instanceof ItemBow && this.orgEntity instanceof IRangedAttackMob) {
                    this.setAIAsRange();
                } else if (this.orgEntity.getRidingEntity() != null) {
                    this.setAIAsMounted(this.orgEntity.getRidingEntity());
                } else if (isArmed()) {
                    this.setAIAsArmed();
                } else {
                    this.setAIAsUnarmed();
                }
            }
        }

    

    @Override
        public void updateMotion() {
            ItemStack offHandItemStack = this.getOriginalEntity().getHeldItem(EnumHand.OFF_HAND);
            if (((this.getOriginalEntity())).getActiveHand()== EnumHand.OFF_HAND && !offHandItemStack.isEmpty() && offHandItemStack.getItemUseAction() == EnumAction.BLOCK) {
                currentMixMotion = LivingMotion.BLOCKING;
            } else {
                super.commonCreatureUpdateMotion();
            }
        }


        /**
         * Halberd;
         * Sword;
         * 2 Swords;
         * Axe or cleaver;
         * Axe and shield;
         * Other
         */
        @Override
        public void setAIAsArmed() {
            Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();
            String heldItemString = heldItem.getRegistryName().toString();
            Item offhand = this.orgEntity.getHeldItemOffhand().getItem();
            float speedModifier = this.orgEntity.getRenderSizeModifier();
            if (heldItemString.equals("ancientwarfarenpc:wooden_halberd")
                    || heldItemString.equals("ancientwarfarenpc:stone_halberd")
                    || heldItemString.equals("ancientwarfarenpc:iron_halberd")
                    || heldItemString.equals("ancientwarfarenpc:gold_halberd")
                    || heldItemString.equals("ancientwarfarenpc:diamond_halberd")
                    || heldItemString.equals("ancientwarfarenpc:death_scythe")
                    || heldItemString.equals("ancientwarfarenpc:scythe")
            ) {//Twohanded halberd/**&& heldItem instanceof ItemExtendedReachWeapon**/
                orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SPEAR2)); //BIPED_ARMED_ONEHAND //BIPED_ARMED_SPEAR2
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.4D * speedModifier, false));
            }
            else if (heldItem instanceof ItemSword && !(heldItem instanceof ItemExtendedReachWeapon) && !(offhand instanceof ItemSword)) {//One sword
                //orgEntity.tasks.addTask(1, new NpcAIBlockWithShield(this.orgEntity));
                orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SWORD));
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.4D * speedModifier, false));
            }
            else if (heldItem instanceof ItemSword && !(heldItem instanceof ItemExtendedReachWeapon) && (offhand instanceof ItemSword)) {//Two swords
                //orgEntity.tasks.addTask(1, new NpcAIBlockWithShield(this.orgEntity));
                orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SWORD2));
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.4D * speedModifier, false));
            }
            else if ((heldItem instanceof ItemAxe || heldItemString.equals("ancientwarfarenpc:wooden_cleaver")
                    || heldItemString.equals("ancientwarfarenpc:stone_cleaver")
                    || heldItemString.equals("ancientwarfarenpc:iron_cleaver")
                    || heldItemString.equals("ancientwarfarenpc:gold_cleaver")
                    || heldItemString.equals("ancientwarfarenpc:diamond_cleaver"))) {
                if (offhand instanceof ItemShield) {
                    orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_AXE)); //BIPED_ARMED_ONEHAND
                    orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.4D * speedModifier, false));
                }
                else {
                    orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.VINDICATOR_PATTERN)); //BIPED_ARMED_ONEHAND
                    orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.4D * speedModifier, false));
                }
            }
            else if (heldItemString.equals("ancientwarfarenpc:giant_club")) {
                orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 1.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_AXE)); //BIPED_ARMED_ONEHAND
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.4D * speedModifier, false));
            }
            else {/** Other**/
                //orgEntity.tasks.addTask(1, new NpcAIBlockWithShield(this.orgEntity));
                orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.BIPED_ARMED_SPEAR)); //BIPED_ARMED_SPEAR
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.4D * speedModifier, false));
            }
        }


        @Override
        public void setAIAsMounted(Entity ridingEntity) {
            //orgEntity.tasks.addTask(0, horseAI);
            Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();
            if ((heldItem instanceof ItemSword && !(heldItem instanceof ItemExtendedReachWeapon))|| heldItem instanceof ItemAxe ){
                orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.BIPED_MOUNT_SWORD));
            } else {
                orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.BIPED_ARMED_SPEAR));
            }
            if (ridingEntity instanceof AbstractHorse) {
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.5D, false));
            }

        }
        @Override
        public void setAIAsRange() {
            int cooldown = this.orgEntity.world.getDifficulty() != EnumDifficulty.HARD ? 40 : 20;
            orgEntity.tasks.addTask(1, new EntityAIArcher(this, this.orgEntity, 1.0D, cooldown, 15.0F));
        }

        @Override
        public void setAIAsUnarmed() {
            Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();
            String heldItemString = heldItem.getRegistryName().toString();
            if (orgEntity.getWidthModifier() >= 2.0F) {
                orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.GIANT_PATTERN));
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.2D, false));
            }
            else {
                orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.BIPED_UNARMED));
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.4D, false));}
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
            float modifier = this.orgEntity.getWidthModifier();
            this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(1.0F + (modifier-1) * 5.0F);
            this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.MAX_STUN_ARMOR).setBaseValue(100.0F);
            this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.OFFHAND_ATTACK_DAMAGE).setBaseValue(this.orgEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        }



        @Override
        public <M extends Model> M getEntityModel(Models<M> modelDB) {
            if (this.orgEntity.getSkinSettings().isAlexModel()) {
                return modelDB.ENTITY_BIPED_SLIM_ARM;
            }
            if (this.orgEntity.getFaction().equals("orc")) {
                return modelDB.ENTITY_ORC;
            }
            return modelDB.ENTITY_BIPED;
        }
    @Override
    public VisibleMatrix4f getModelMatrix(float partialTicks) {
        float h = orgEntity.height/1.8F;
        float w = orgEntity.width/0.6F;
        if (this.orgEntity.getFaction().equals("orc")) {
            h*=1.1;
        }
        VisibleMatrix4f mat = super.getModelMatrix(partialTicks);
        return VisibleMatrix4f.scale(new Vec3f(w, h, w), mat, mat);
    }


        /**
         *костыль
         */
        @Override
        public StaticAnimation getHitAnimation(IExtendedDamageSource.StunType stunType) {
            if(this.orgEntity.getNpcType() != "trader") {
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
            else {return null;}
        }
    }
