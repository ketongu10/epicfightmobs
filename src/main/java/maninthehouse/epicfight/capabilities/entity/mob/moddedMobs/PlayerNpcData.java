package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import io.netty.buffer.ByteBuf;
import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.entity.DataKeys;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.entity.mob.Faction;
import maninthehouse.epicfight.capabilities.entity.mob.MobAttackPatterns;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.entity.ai.EntityAIArcher;
import maninthehouse.epicfight.entity.ai.EntityAIAttackPattern;
import maninthehouse.epicfight.entity.ai.EntityAIChase;
import maninthehouse.epicfight.entity.ai.EntityAIParry;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Models;
import maninthehouse.epicfight.gamedata.Sounds;
import maninthehouse.epicfight.item.SpearItem;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.network.server.STCMobInitialSetting;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.shadowmage.ancientwarfare.npc.entity.NpcPlayerOwned;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFaction;
import net.shadowmage.ancientwarfare.npc.item.ItemExtendedReachWeapon;
import net.shadowmage.ancientwarfare.npc.item.ItemShield;

public class PlayerNpcData<T extends NpcPlayerOwned> extends BipedMobData<T> implements ISkillfulParry, IRangedAttackMobCapability {

    private int tickSinceBlock=0;

    public PlayerNpcData() {
        super(Faction.NATURAL);
    }
    @Override
    public void onEntityJoinWorld(T entityIn) {
        super.onEntityJoinWorld(entityIn);
        this.orgEntity.getDataManager().register(DataKeys.STUN_ARMOR, Float.valueOf(7.0F));
        this.orgEntity.getDataManager().register(DataKeys.PARRYING, Boolean.valueOf(false));
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
        if (heldItemString.equals("ancientwarfarenpc:wooden_halberd")
                || heldItemString.equals("ancientwarfarenpc:stone_halberd")
                || heldItemString.equals("ancientwarfarenpc:iron_halberd")
                || heldItemString.equals("ancientwarfarenpc:gold_halberd")
                || heldItemString.equals("ancientwarfarenpc:diamond_halberd")
                || heldItemString.equals("ancientwarfarenpc:death_scythe")
                || heldItemString.equals("ancientwarfarenpc:scythe")
                || heldItem instanceof SpearItem
        ) {//Twohanded halberd/**&& heldItem instanceof ItemExtendedReachWeapon**/
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SPEAR2)); //BIPED_ARMED_ONEHAND //BIPED_ARMED_SPEAR2
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
        }
        else if (heldItem instanceof ItemSword && !(heldItem instanceof ItemExtendedReachWeapon) && !(offhand instanceof ItemSword)) {//One sword
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SWORD));
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
        }
        else if (heldItem instanceof ItemSword && !(heldItem instanceof ItemExtendedReachWeapon) && (offhand instanceof ItemSword)) {//Two swords
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SWORD2));
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
        }
        else if ((heldItem instanceof ItemAxe || heldItemString.equals("ancientwarfarenpc:wooden_cleaver")
                || heldItemString.equals("ancientwarfarenpc:stone_cleaver")
                || heldItemString.equals("ancientwarfarenpc:iron_cleaver")
                || heldItemString.equals("ancientwarfarenpc:gold_cleaver")
                || heldItemString.equals("ancientwarfarenpc:diamond_cleaver"))) {
            if (offhand instanceof ItemShield || offhand instanceof net.minecraft.item.ItemShield) {
                orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_AXE)); //BIPED_ARMED_ONEHAND
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
            }
            else {
                orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.VINDICATOR_PATTERN)); //BIPED_ARMED_ONEHAND
                orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
            }
        }
        else if (heldItemString.equals("ancientwarfarenpc:giant_club")) {
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 1.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_AXE)); //BIPED_ARMED_ONEHAND
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
        }
        else {/** Other**/
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.BIPED_ARMED_SPEAR)); //BIPED_ARMED_SPEAR
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, speedIn, false));
        }
        if (!(offhand instanceof ItemShield) && !(offhand instanceof net.minecraft.item.ItemShield))
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
    protected void initAttributes() {
        super.initAttributes();
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(1.0F);
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.MAX_STUN_ARMOR).setBaseValue(7.0F);
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.OFFHAND_ATTACK_DAMAGE).setBaseValue(this.orgEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
    }


    @Override
    public <M extends Model> M getEntityModel(Models<M> modelDB) {
        if (this.orgEntity.isFemale()) {
            return modelDB.ENTITY_BIPED_SLIM_ARM;
        }
        return modelDB.ENTITY_BIPED;
    }


}