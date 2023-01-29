package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import chumbanotz.mutantbeasts.entity.mutant.MutantEndermanEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantSnowGolemEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantZombieEntity;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemSilverArmor;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.windanesz.ancientspellcraft.registry.ASItems;
import de.teamlapen.vampirism.api.items.IVampirismCrossbow;
import de.teamlapen.vampirism.core.ModEntities;
import de.teamlapen.vampirism.entity.hunter.EntityAdvancedHunter;
import de.teamlapen.vampirism.entity.hunter.EntityBasicHunter;
import de.teamlapen.vampirism.entity.hunter.EntityHunterBase;
import de.teamlapen.vampirism.entity.hunter.EntityHunterTrainer;
import de.teamlapen.vampirism.entity.vampire.EntityVampireBase;
import de.teamlapen.vampirism.items.ItemDoubleCrossbow;
import de.teamlapen.vampirism.items.ItemHunterAxe;
import de.teamlapen.vampirism.items.VampirismItem;
import electroblob.wizardry.entity.living.EntityEvilWizard;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.registry.WizardryItems;
import io.netty.buffer.ByteBuf;
import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.ActionAnimation;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.ModCapabilities;
import maninthehouse.epicfight.capabilities.entity.DataKeys;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.capabilities.entity.MobData;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.entity.mob.Faction;
import maninthehouse.epicfight.capabilities.entity.mob.MobAttackPatterns;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.client.model.compatibility.WitcherEquipment.ItemWitcherBackpack;
import maninthehouse.epicfight.entity.ai.*;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.entity.event.HitEvent;
import maninthehouse.epicfight.entity.event.PlayerEventListener;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Models;
import maninthehouse.epicfight.gamedata.Sounds;
import maninthehouse.epicfight.item.ModItems;
import maninthehouse.epicfight.item.SpearItem;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.network.ModNetworkManager;
import maninthehouse.epicfight.network.server.STCMobInitialSetting;
import maninthehouse.epicfight.network.server.STCPlayAnimationTarget;
import maninthehouse.epicfight.utils.game.Formulars;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Loader;
import net.shadowmage.ancientwarfare.npc.ai.NpcAIAttack;
import net.shadowmage.ancientwarfare.npc.compat.ebwizardry.EBWizardryCompat;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFaction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static maninthehouse.epicfight.main.LoaderConstants.ANCIENT_SPELLCRAFT;
import static maninthehouse.epicfight.main.LoaderConstants.EBWIZARDRY;

public class HunterData<T extends EntityHunterBase> extends BipedMobData<T> implements IRangedAttackMobCapability, ISkillfulParry {

    private int angerLevel;
    private int tickSinceBlock = 0;
    private int tickNoTarget = 0;
    private int randomSoundDelay;
    private UUID angerTargetUUID;
    private ItemStack weaponHuman;
    private ItemStack weaponWizard;


    public HunterData() {
        super(Faction.NATURAL);

    }
    @Override
    public void onEntityJoinWorld(T entityIn) {
        super.onEntityJoinWorld(entityIn);
        this.orgEntity.getDataManager().register(DataKeys.STUN_ARMOR, Float.valueOf(7.0F));
        this.orgEntity.getDataManager().register(DataKeys.PARRYING, Boolean.valueOf(false));
        if (this.orgEntity instanceof EntityBasicHunter) {
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(de.teamlapen.vampirism.core.ModItems.enhanced_crossbow));
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(de.teamlapen.vampirism.core.ModItems.hunter_hat1_head));
        } else if (this.orgEntity instanceof EntityAdvancedHunter) {
            weaponHuman = new ItemStack(Items.IRON_SWORD);
            weaponWizard = ANCIENT_SPELLCRAFT ? new ItemStack(ASItems.devoritium_sword): new ItemStack(Items.IRON_SWORD);
            weaponHuman.addEnchantment(Enchantments.FIRE_ASPECT,1);
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(ModItems.WITCHER_BACKPACK));
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.HAIR));
        } else {
            weaponHuman = new ItemStack(Items.IRON_AXE);
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponHuman);
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.STICK));
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(ModItems.CAPE));
            this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(de.teamlapen.vampirism.core.ModItems.hunter_hat0_head));
        }
    }



    @Override
    protected void initAnimator(AnimatorClient animatorClient) {
        super.initAnimator(animatorClient);
        animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.BIPED_IDLE);
        animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.BIPED_WALK);
        animatorClient.addLivingAnimation(LivingMotion.FALL, Animations.BIPED_FALL);
        animatorClient.addLivingAnimation(LivingMotion.MOUNT, Animations.BIPED_MOUNT);
        animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.BIPED_DEATH);
        animatorClient.addLivingAnimation(LivingMotion.BLOCKING, Animations.BIPED_BLOCK);

        if (this.orgEntity instanceof EntityBasicHunter) {
            animatorClient.addLivingMixAnimation(LivingMotion.SHOTING, Animations.BIPED_CROSSBOW_AIM);
            animatorClient.addLivingMixAnimation(LivingMotion.WALKING, Animations.BIPED_WALK_CROSSBOW);
            animatorClient.addLivingMixAnimation(LivingMotion.IDLE, Animations.BIPED_IDLE_CROSSBOW);
        } else if (this.orgEntity instanceof EntityAdvancedHunter) {
            animatorClient.addLivingMixAnimation(LivingMotion.BLOCKING, Animations.BIPED_TACHI_HELD);
        } else {
            animatorClient.addLivingMixAnimation(LivingMotion.BLOCKING, Animations.BIPED_WALK_UNSHEATHING);
            animatorClient.addLivingMixAnimation(LivingMotion.SHOTING, Animations.BIPED_BOW_AIM);
            animatorClient.addLivingMixAnimation(LivingMotion.IDLE, Animations.ILLAGER_IDLE);
            animatorClient.addLivingMixAnimation(LivingMotion.WALKING, Animations.ILLAGER_WALK);
        }
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
        if (!this.isRemote() /**&& !this.orgEntity.isAIDisabled()**/) {
            super.resetCombatAI();
            resetTargets();
            Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();

            if (heldItem instanceof IVampirismCrossbow) {
                this.setAIAsRange();
            } else if (this.orgEntity.getRidingEntity() != null
                    && this.orgEntity.getRidingEntity() instanceof EntityMob) {
                this.setAIAsMounted(this.orgEntity.getRidingEntity());
            } else  {
                this.setAIAsArmed();
            }
        }
    }

   @Override
    public void updateMotion() {
       super.commonCreatureUpdateMotion();
        if (isParrying()) {
            currentMixMotion = LivingMotion.BLOCKING;
        } else {
            currentMixMotion = LivingMotion.NONE;
        }
    }

    @Override
    public void update() {
        tickSinceBlock++;
        if (orgEntity instanceof EntityAdvancedHunter) {
            takePropWeapon(1);
        } /**else if (orgEntity instanceof EntityHunterTrainer) {
            takePropWeapon(2);
        }**/
        super.update();
    }
    public void takePropWeapon(int axe) {
        Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();
        EntityLivingBase target = orgEntity.getAttackTarget();
        if (target != null) {
            //tickNoTarget = 0;
            if (axe == 1) {//Advanced
                if (EBWIZARDRY) {
                    if (target instanceof ISpellCaster && heldItem != ASItems.devoritium_sword) {
                        this.playAnimationSynchronize(Animations.BIPED_DIG, 0);
                        this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponWizard);
                        ((ItemWitcherBackpack) this.orgEntity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem()).display = 2;//display silver
                    }
                    if (!(target instanceof ISpellCaster) && heldItem != Items.IRON_SWORD) {
                        this.playAnimationSynchronize(Animations.BIPED_DIG, 0);
                        this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponHuman);
                        ((ItemWitcherBackpack) this.orgEntity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem()).display = 1;//display devoritium
                    }
                } else if (heldItem != Items.IRON_SWORD) {
                    this.playAnimationSynchronize(Animations.BIPED_DIG, 0);
                    this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponHuman);
                    ((ItemWitcherBackpack) this.orgEntity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem()).display = 1;//display devoritium
                }
            }/** else if (axe == 2) {//Trainer
                if (target instanceof ISpellCaster && heldItem != ASItems.devoritium_axe) {
                    this.playAnimationSynchronize(Animations.ZOMBIE_ATTACK1, 0);
                    this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponWizard);
                }
                if (!(target instanceof ISpellCaster) && heldItem == ASItems.devoritium_axe) {
                    this.playAnimationSynchronize(Animations.ZOMBIE_ATTACK1, 0);
                    this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponHuman);
                }
            }**/
        } /**else {
            tickNoTarget++;
            if (tickNoTarget > 100) {//Advanced
                if (heldItem != null) {
                    ((ItemWitcherBackpack) this.orgEntity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem()).display = 0;
                    this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
            }
        }**/

    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(2.5F);
        this.orgEntity.getEntityAttribute(ModAttributes.MAX_STUN_ARMOR).setBaseValue(7.0F);
    }


    @Override
    public void setAIAsArmed() {
        //this.orgEntity.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this.orgEntity, EntityVampireBase.class, true));
        this.orgEntity.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this.orgEntity, EntityMob.class, true));
        this.orgEntity.targetTasks.addTask(1, new HunterData.AIHurtByAggressor(this.orgEntity));
        this.orgEntity.targetTasks.addTask(2, new HunterData.AITargetAggressor(this.orgEntity));
        if (orgEntity instanceof EntityAdvancedHunter) {
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.HUNTER_SWORD));
            orgEntity.tasks.addTask(0, new EntityAIParry(this, this.orgEntity, 0.03F, 0.0D, 3.5D, true, MobAttackPatterns.PARRY_SPECIAL_ATTACK1, MobAttackPatterns.PARRY_SPECIAL_ATTACK2, MobAttackPatterns.PARRY_SPECIAL_ATTACK3));
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.3D, true));//, Animations.LONGSWORD_GUARD, Animations.BIPED_WALK));
        }
        else {
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.HUNTER_AXE));
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.3D, false));
            orgEntity.tasks.addTask(0, new EntityAIParry(this, this.orgEntity, 0.05F, 0.0D, 3.5D, true,  MobAttackPatterns.PARRY_SPECIAL_ATTACK2, MobAttackPatterns.PARRY_SPECIAL_ATTACK3));

        }
    }
    @Override
    public void setAIAsMounted(Entity ridingEntity) {

        //this.orgEntity.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this.orgEntity, EntityVampireBase.class, true));
        this.orgEntity.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this.orgEntity, EntityMob.class, true));
        this.orgEntity.targetTasks.addTask(1, new HunterData.AIHurtByAggressor(this.orgEntity));
        this.orgEntity.targetTasks.addTask(2, new HunterData.AITargetAggressor(this.orgEntity));
        Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();
        if ((heldItem instanceof ItemSword && heldItem instanceof SpearItem)|| heldItem instanceof ItemAxe ){
            orgEntity.tasks.addTask(1, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.BIPED_MOUNT_SWORD));
        } else {
            orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.BIPED_ARMED_SPEAR));
        }
        if (ridingEntity instanceof AbstractHorse) {
            orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.5D, false));
        }

    }

    @Override
    public void setAIAsRange() {
        //this.orgEntity.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this.orgEntity, EntityVampireBase.class, true));
        this.orgEntity.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this.orgEntity, EntityMob.class, true));
        this.orgEntity.targetTasks.addTask(1, new HunterData.AIHurtByAggressor(this.orgEntity));
        this.orgEntity.targetTasks.addTask(2, new HunterData.AITargetAggressor(this.orgEntity));
        int cooldown = this.orgEntity.world.getDifficulty() != EnumDifficulty.HARD ? 40 : 20;
        orgEntity.tasks.addTask(1, new EntityAIArcher(this, this.orgEntity, 1.0D, cooldown, 16.0F)); //hujnya
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
        return !damageSource.isUnblockable() /*&& !damageSource.isProjectile(*/ && !damageSource.isExplosion() && !damageSource.isMagicDamage() && !damageSource.isFireDamage() && isFront(damageSource);
    }

    @Override
    public IndirectDamageSourceExtended getRangedDamageSource(Entity damageCarrier) {
        IndirectDamageSourceExtended source = new IndirectDamageSourceExtended("arrow", this.orgEntity, damageCarrier, IExtendedDamageSource.StunType.SHORT);
        source.setImpact(1.0F);

        return source;
    }
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.registerIfAbsent(ModAttributes.MAX_STUN_ARMOR);
    }
    @Override
    public boolean isArmed() {
        Item heldItem = this.orgEntity.getHeldItemMainhand().getItem();
        return heldItem instanceof ItemSword || heldItem instanceof ItemHunterAxe || heldItem instanceof ItemTool;
    }

    protected void resetTargets() {
        Iterator<EntityAITasks.EntityAITaskEntry> iterator = orgEntity.targetTasks.taskEntries.iterator();
        List<EntityAIBase> removeTargets = new ArrayList<EntityAIBase>();

        while (iterator.hasNext()) {
            EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry = iterator.next();
            EntityAIBase entityAITarget = entityaitasks$entityaitaskentry.action;
            removeTargets.add(entityAITarget);

        }

        for (EntityAIBase target : removeTargets) {
            orgEntity.targetTasks.removeTask(target);
        }
    }

    @Override
    public <M extends Model> M getEntityModel(Models<M> modelDB) {
        /**if (this.orgEntity instanceof EntityHunterTrainer) {
            return modelDB.ENTITY_ILLAGER;
        }**/
        return modelDB.ENTITY_BIPED;
    }


    /**========================FROM ZOMBIE-PIG========================================**/
    static class AIHurtByAggressor extends EntityAIHurtByTarget
    {
        public AIHurtByAggressor(EntityHunterBase entityIn)
        {
            super(entityIn, true);
        }

        protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
        {
            super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);

            if (creatureIn instanceof EntityHunterBase && creatureIn.hasCapability(ModCapabilities.CAPABILITY_ENTITY, null))
            {
                ((HunterData) creatureIn.getCapability(ModCapabilities.CAPABILITY_ENTITY, null)).becomeAngryAt(entityLivingBaseIn);
            }
        }
    }

    static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        public AITargetAggressor(EntityHunterBase hunter)
        {
            super(hunter, EntityPlayer.class, true);
        }

        public boolean shouldExecute()
        {
            return ((HunterData)this.taskOwner.getCapability(ModCapabilities.CAPABILITY_ENTITY, null)).isAngry() && super.shouldExecute();
        }
    }

    public void becomeAngryAt(Entity entity)
    {
        this.angerLevel = 600;
        this.randomSoundDelay = 25;

        if (entity instanceof EntityLivingBase)
        {
            this.setRevengeTarget((EntityLivingBase)entity);
        }
    }

    public void setRevengeTarget(@Nullable EntityLivingBase livingBase)
    {
        super.orgEntity.setRevengeTarget(livingBase);

        if (livingBase != null)
        {
            this.angerTargetUUID = livingBase.getUniqueID();
        }
    }
    public boolean isAngry()
    {
        return this.angerLevel > 0;
    }

    public void setParrying(boolean blocking){
        this.orgEntity.getDataManager().set(DataKeys.PARRYING, blocking);

    }
    public boolean isParrying(){
        return this.orgEntity.getDataManager().get(DataKeys.PARRYING);
    }





}
