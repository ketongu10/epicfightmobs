package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.custom;

import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;
import io.netty.buffer.ByteBuf;
import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.capabilities.entity.CapabilityEntity;
import maninthehouse.epicfight.capabilities.entity.DataKeys;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.entity.mob.Faction;
import maninthehouse.epicfight.capabilities.entity.mob.MobAttackPatterns;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.config.EntityConfigurationCapability;
import maninthehouse.epicfight.entity.ai.EntityAIArcher;
import maninthehouse.epicfight.entity.ai.EntityAIAttackPattern;
import maninthehouse.epicfight.entity.ai.EntityAIChase;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Models;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.network.ModNetworkManager;
import maninthehouse.epicfight.network.client.CTSReqSpawnInfo;
import maninthehouse.epicfight.network.server.STCMobInitialSetting;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import maninthehouse.epicfight.utils.math.Vec3f;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.EnumDifficulty;

public class CustomMobData<T extends EntityCreature> extends BipedMobData<T> implements IRangedAttackMobCapability {

    private float impact = 0;
    private float stun_armor = 0;
    private boolean oldTex = false;
    private EntityConfigurationCapability.EntityType type;

    public CustomMobData(float impact, Float stunArmor, EntityConfigurationCapability.EntityType type, Boolean oldTex) {
        super(Faction.NATURAL);
        this.impact = impact;
        if (stunArmor != null) {this.stun_armor = stunArmor;}
        if (oldTex != null) {this.oldTex = oldTex;}
        this.type = type;
    }
    @Override
    public void onEntityJoinWorld(T entityIn) {
        super.onEntityJoinWorld(entityIn);
        this.orgEntity.getDataManager().register(DataKeys.STUN_ARMOR, Float.valueOf(stun_armor));
        notifyToReset(orgEntity.width, orgEntity.height); //only for hitbox
        this.resetSize();
    }
    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(impact);
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.MAX_STUN_ARMOR).setBaseValue(stun_armor);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.registerIfAbsent(ModAttributes.MAX_STUN_ARMOR);
        this.registerIfAbsent(ModAttributes.OFFHAND_ATTACK_DAMAGE);

    }
    @Override
    protected void initAnimator(AnimatorClient animatorClient) {
        super.initAnimator(animatorClient);
        switch (this.type) {
            case ZOMBIE:
                animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.ZOMBIE_IDLE);
                animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.ZOMBIE_WALK);
                break;
            case SKELETON:
                if (orgEntity.getClass().isInstance(IRangedAttackMob.class)) {
                    animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.BIPED_IDLE);
                    animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.BIPED_WALK);
                    break;
                } else {
                    animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.WITHER_SKELETON_IDLE);
                    animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.WITHER_SKELETON_WALK);
                    break;
                }
            case ILLAGER:
                animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.ILLAGER_IDLE);
                animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.ILLAGER_WALK);
                break;
        }
        animatorClient.addLivingAnimation(LivingMotion.FALL, Animations.BIPED_FALL);
        animatorClient.addLivingAnimation(LivingMotion.MOUNT, Animations.BIPED_MOUNT);
        animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.BIPED_DEATH);
        animatorClient.addLivingMixAnimation(LivingMotion.BLOCKING, Animations.BIPED_BLOCK);
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
            Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();

            if (heldItem instanceof ItemBow && this.orgEntity instanceof IRangedAttackMob) {
                this.setAIAsRange();
            } else if (this.orgEntity.getRidingEntity() != null) {
                this.setAIAsMounted(this.orgEntity.getRidingEntity());
            } else if (isArmed()) {
                if (type == EntityConfigurationCapability.EntityType.SKELETON) {
                    this.setAIAsWitherSkeleton();
                } else if (type== EntityConfigurationCapability.EntityType.ILLAGER) {
                    this.setAIAsVindicator();
                } else {
                    this.setAIAsArmed();
                }
            } else {
                this.setAIAsZombie();
            }

            if (!this.orgEntity.canPickUpLoot()) {
                this.orgEntity.setCanPickUpLoot(!isArmed());
            }
        } else {
            ModNetworkManager.sendToServer(new CTSReqSpawnInfo(this.orgEntity.getEntityId()));
        }
    }

    @Override
    public IndirectDamageSourceExtended getRangedDamageSource(Entity damageCarrier) {
        IndirectDamageSourceExtended source = new IndirectDamageSourceExtended("arrow", this.orgEntity, damageCarrier, IExtendedDamageSource.StunType.SHORT);
        source.setImpact(1.0F);

        return source;
    }

    @Override
    public void updateMotion() {
        super.commonCreatureUpdateMotion();
        ItemStack offHandItemStack = this.getOriginalEntity().getHeldItem(EnumHand.OFF_HAND);
        if (((this.getOriginalEntity())).getActiveHand()== EnumHand.OFF_HAND && !offHandItemStack.isEmpty() && offHandItemStack.getItemUseAction() == EnumAction.BLOCK) {
            currentMixMotion = LivingMotion.BLOCKING;
        } else {
            currentMixMotion = LivingMotion.NONE;
        }
    }

    public void setAIAsWitherSkeleton() {
        orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.WITHER_SKELETON_PATTERN));//0
        orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.2D, true, Animations.WITHER_SKELETON_CHASE, Animations.WITHER_SKELETON_WALK));//1
    }
    public void setAIAsZombie() {
        orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_ONEHAND));
        orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.0D, false));
    }

    public void setAIAsVindicator() {
        orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 3.0D, true, MobAttackPatterns.VINDICATOR_PATTERN));
        orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.0D, false));
    }

    public void setAIAsMounted(Entity ridingEntity) {
        if (isArmed()) {
            if (isArmed()) {
                orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_MOUNT_SWORD));

                if (ridingEntity instanceof AbstractHorse) {
                    orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.0D, false));
                }
            }
        }
    }

    public void setAIAsRange() {
        int cooldown = this.orgEntity.world.getDifficulty() != EnumDifficulty.HARD ? 40 : 20;
        orgEntity.tasks.addTask(1, new EntityAIArcher(this, this.orgEntity, 1.0D, cooldown, 15.0F));
    }
    @Override
    public <M extends Model> M getEntityModel(Models<M> modelDB) {
        switch (this.type) {
            case ILLAGER:
                return modelDB.ENTITY_ILLAGER;
            case ZOMBIE:
                return oldTex ? modelDB.ENTITY_BIPED_64_32_TEX : modelDB.ENTITY_BIPED;
            case SKELETON:
                return modelDB.ENTITY_SKELETON;
        }
        return modelDB.ENTITY_BIPED;
    }

    @Override
    public VisibleMatrix4f getModelMatrix(float partialTicks) {
        float h = orgEntity.height/1.8F;
        float w = orgEntity.width/0.6F;
        VisibleMatrix4f mat = super.getModelMatrix(partialTicks);
        return VisibleMatrix4f.scale(new Vec3f(w, h, w), mat, mat);
    }



}
