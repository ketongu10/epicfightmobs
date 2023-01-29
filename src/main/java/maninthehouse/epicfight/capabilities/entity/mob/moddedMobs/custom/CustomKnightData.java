package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.custom;

import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.entity.DataKeys;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.entity.mob.Faction;
import maninthehouse.epicfight.capabilities.entity.mob.MobAttackPatterns;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.ISkillfulParry;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.config.EntityConfigurationCapability;
import maninthehouse.epicfight.entity.ai.EntityAIAttackPattern;
import maninthehouse.epicfight.entity.ai.EntityAIChase;
import maninthehouse.epicfight.entity.ai.EntityAIParry;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Models;
import maninthehouse.epicfight.gamedata.Sounds;
import maninthehouse.epicfight.item.SpearItem;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.shadowmage.ancientwarfare.npc.item.ItemExtendedReachWeapon;

public class CustomKnightData<T extends EntityCreature> extends BipedMobData<T> implements IRangedAttackMobCapability, ISkillfulParry {

    private float impact = 0;
    private float stun_armor = 0;
    private int tickSinceBlock=0;
    private boolean canParry = false;
    private boolean oldTex = false;

    public CustomKnightData(float impact, Float stunArmor, Boolean canParry, Boolean oldTex) {
        super(Faction.NATURAL);
        this.impact = impact;
        if (stunArmor != null) {this.stun_armor = stunArmor;}
        if (canParry != null) {this.canParry = canParry;}
        if (oldTex != null) {this.oldTex = oldTex;}
    }
    @Override
    public void onEntityJoinWorld(T entityIn) {
        super.onEntityJoinWorld(entityIn);
        this.orgEntity.getDataManager().register(DataKeys.STUN_ARMOR, Float.valueOf(stun_armor));
        this.orgEntity.getDataManager().register(DataKeys.PARRYING, Boolean.valueOf(false));
        notifyToReset(orgEntity.width, orgEntity.height); //only for hitbox
        this.resetSize();
    }
    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(impact);
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.MAX_STUN_ARMOR).setBaseValue(stun_armor);
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.OFFHAND_ATTACK_DAMAGE).setBaseValue(this.orgEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
    }

    @Override
    protected void initAnimator(AnimatorClient animatorClient) {
        super.initAnimator(animatorClient);
        animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.BIPED_IDLE);
        animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.BIPED_WALK);
        animatorClient.addLivingAnimation(LivingMotion.FALL, Animations.BIPED_FALL);
        animatorClient.addLivingAnimation(LivingMotion.MOUNT, Animations.BIPED_MOUNT);
        animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.BIPED_DEATH);
        animatorClient.addLivingMixAnimation(LivingMotion.BLOCKING, Animations.BIPED_BLOCK);
        animatorClient.addLivingMixAnimation(LivingMotion.TACHI, Animations.BIPED_TACHI_HELD);
        animatorClient.setCurrentLivingMotionsToDefault();
    }

    @Override
    public void postInit() {
        if (!this.isRemote()) {
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
    public void updateMotion() {
        super.commonCreatureUpdateMotion();
        ItemStack offHandItemStack = this.getOriginalEntity().getHeldItem(EnumHand.OFF_HAND);
        if (((this.getOriginalEntity())).getActiveHand()== EnumHand.OFF_HAND && !offHandItemStack.isEmpty() && offHandItemStack.getItemUseAction() == EnumAction.BLOCK) {
            currentMixMotion = LivingMotion.BLOCKING;
        } else if (isParrying()) {
            currentMixMotion = LivingMotion.TACHI;
        } else {
            currentMixMotion = LivingMotion.NONE;
        }
    }

    @Override
    public void update() {
        tickSinceBlock++;
        super.update();
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
        double speedIn = 1.25D;
        if (heldItem instanceof SpearItem) {//Twohanded halberd/**&& heldItem instanceof ItemExtendedReachWeapon**/
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SPEAR2)); //BIPED_ARMED_ONEHAND //BIPED_ARMED_SPEAR2
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
        }
        else if (heldItem instanceof ItemSword && !(offhand instanceof ItemSword)) {//One sword
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SWORD));
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
        }
        else if (heldItem instanceof ItemSword && (offhand instanceof ItemSword)) {//Two swords
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SWORD2));
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
        }
        else if (heldItem instanceof ItemAxe || heldItem instanceof ItemTool) {
            if (offhand.isShield(orgEntity.getHeldItemOffhand(), orgEntity)) {
                orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_AXE)); //BIPED_ARMED_ONEHAND
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
            }
            else {
                orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.VINDICATOR_PATTERN)); //BIPED_ARMED_ONEHAND
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
            }
        }
        else {/** Other**/
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.BIPED_ARMED_SPEAR)); //BIPED_ARMED_SPEAR
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
        }
        if (!(offhand.isShield(orgEntity.getHeldItemOffhand(), orgEntity)) && canParry)
            orgEntity.tasks.addTask(0, new EntityAIParry(this, this.orgEntity, 0.03F, 0.0D, 3.5D, true, MobAttackPatterns.PARRY_SPECIAL_ATTACK1, MobAttackPatterns.PARRY_SPECIAL_ATTACK2, MobAttackPatterns.PARRY_SPECIAL_ATTACK3));
    }

    @Override
    public boolean hurtBy(LivingAttackEvent event) {
        if (!inaction && isParrying() && tickSinceBlock >= 10 && isBlockableSource(event.getSource())) {
            tickSinceBlock = 0;
            this.playSound(Sounds.CLASH, -0.05F, 0.1F);
            playGuardAnimation();
            return false;
        } else {
            return super.hurtBy(event);
        }
    }

    protected boolean isFront(DamageSource damageSource) {
        boolean isFront = false;
        Vec3d damageLocation = damageSource.getDamageLocation();

        if (damageLocation != null) {
            Vec3d vector3d = this.orgEntity.getLook(1.0F);
            Vec3d vector3d1 = damageLocation.subtractReverse(this.orgEntity.getPositionVector()).normalize();
            vector3d1 = new Vec3d(vector3d1.x, 0.0D, vector3d1.z);
            if (vector3d1.dotProduct(vector3d) < 0.0D) {
                isFront = true;
            }
        }
        return isFront;
    }

    protected void playGuardAnimation() {
        StaticAnimation guard;
        float rnd = this.orgEntity.getRNG().nextFloat();
        if (rnd > 0.5F) {
            guard = Animations.LONGSWORD_GUARD_HIT;
        } else {
            guard = Animations.KATANA_GUARD_HIT;
        }
        this.playAnimationSynchronize(guard, 0);
    }

    public boolean isBlockableSource(DamageSource damageSource) {
        return !damageSource.isUnblockable() && !damageSource.isProjectile() && !damageSource.isExplosion() && !damageSource.isMagicDamage() && !damageSource.isFireDamage() && isFront(damageSource);
    }

    public void setParrying(boolean blocking){
        this.orgEntity.getDataManager().set(DataKeys.PARRYING, blocking);

    }
    public boolean isParrying(){
        return this.orgEntity.getDataManager().get(DataKeys.PARRYING);
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
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.5D, false));
        }

    }
    @Override
    public IndirectDamageSourceExtended getRangedDamageSource(Entity damageCarrier) {
        IndirectDamageSourceExtended source = new IndirectDamageSourceExtended("arrow", this.orgEntity, damageCarrier, IExtendedDamageSource.StunType.SHORT);
        source.setImpact(1.0F);

        return source;
    }

    @Override
    public void setAIAsUnarmed() {
        orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.BIPED_UNARMED));
        orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.4D, false));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.registerIfAbsent(ModAttributes.MAX_STUN_ARMOR);
        this.registerIfAbsent(ModAttributes.OFFHAND_ATTACK_DAMAGE);

    }




    @Override
    public <M extends Model> M getEntityModel(Models<M> modelDB) {

        return oldTex ? modelDB.ENTITY_BIPED_64_32_TEX : modelDB.ENTITY_BIPED;
    }

}
