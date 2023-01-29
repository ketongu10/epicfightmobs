package maninthehouse.epicfight.capabilities.entity.player;

import java.util.UUID;

import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.item.ItemWand;
import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.entity.DataKeys;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.entity.event.PlayerEvent;
import maninthehouse.epicfight.entity.event.PlayerEventListener;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Models;
import maninthehouse.epicfight.gamedata.Skills;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.skill.SkillContainer;
import maninthehouse.epicfight.skill.SkillSlot;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource.DamageType;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource.StunType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public abstract class PlayerData<T extends EntityPlayer> extends LivingData<T> {
	private static final UUID ACTION_EVENT_UUID = UUID.fromString("e6beeac4-77d2-11eb-9439-0242ac130002");
	protected float yaw;
	protected PlayerEventListener eventListeners;
	protected int tickSinceLastAction;
	public SkillContainer[] skills;
	
	public PlayerData() {
		SkillSlot[] slots = SkillSlot.values();
		this.skills = new SkillContainer[SkillSlot.values().length];
		for(SkillSlot slot : slots) {
			this.skills[slot.getIndex()] = new SkillContainer(this, slot.getIndex());
		}

	}
	@Override
	public void onEntityJoinWorld(T entityIn) {
		super.onEntityJoinWorld(entityIn);
		this.eventListeners = new PlayerEventListener(this);
		this.skills[SkillSlot.DODGE.getIndex()].setSkill(Skills.ROLL);
		this.skills[SkillSlot.STEP_BACK.getIndex()].setSkill(Skills.STEP_BACK);
		//this.skills[SkillSlot.WEAPON_GUARD.getIndex()].setSkill(Skills.GUARD);
		if (!this.orgEntity.getDataManager().entries.containsKey(DataKeys.STUN_ARMOR.getId())) {
			this.orgEntity.getDataManager().register(DataKeys.STUN_ARMOR, Float.valueOf(0.0F));
		}
		if (!this.orgEntity.getDataManager().entries.containsKey(DataKeys.STAMINA.getId())) {
			this.orgEntity.getDataManager().register(DataKeys.STAMINA, Float.valueOf(0.0F));
		}
		this.tickSinceLastAction = 40;
		this.eventListeners.addEventListener(PlayerEventListener.EventType.ACTION_EVENT, ACTION_EVENT_UUID, (event) -> {
			this.resetActionTick();
			return false;
		});
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.registerIfAbsent(ModAttributes.MAX_STUN_ARMOR);
		this.registerIfAbsent(ModAttributes.MAX_STAMINA);
		this.registerIfAbsent(ModAttributes.OFFHAND_ATTACK_DAMAGE);
		this.registerIfAbsent(ModAttributes.OFFHAND_ATTACK_SPEED);
	}
	
	@Override
	public void initAnimator(AnimatorClient animatorClient) {
		animatorClient.mixLayer.setJointMask("Root", "Torso");
		animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.BIPED_IDLE);
		animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.BIPED_WALK);
		animatorClient.addLivingAnimation(LivingMotion.RUNNING, Animations.BIPED_RUN);
		animatorClient.addLivingAnimation(LivingMotion.SNEAKING, Animations.BIPED_SNEAK);
		animatorClient.addLivingAnimation(LivingMotion.SWIMMING, Animations.BIPED_SWIM);
		animatorClient.addLivingAnimation(LivingMotion.SLEEP, Animations.BIPED_SLEEPING);
		animatorClient.addLivingAnimation(LivingMotion.CLIMB, Animations.BIPED_CLIMBING);
		animatorClient.addLivingAnimation(LivingMotion.FLOATING, Animations.BIPED_FLOAT);
		animatorClient.addLivingAnimation(LivingMotion.KNEELING, Animations.BIPED_KNEEL);
		animatorClient.addLivingAnimation(LivingMotion.FALL, Animations.BIPED_FALL);
		animatorClient.addLivingAnimation(LivingMotion.MOUNT, Animations.BIPED_MOUNT);
		animatorClient.addLivingAnimation(LivingMotion.FLYING, Animations.BIPED_FLYING);
		animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.BIPED_DEATH);
		animatorClient.addLivingAnimation(LivingMotion.JUMPING, Animations.BIPED_JUMP);
		animatorClient.addLivingMixAnimation(LivingMotion.BLOCKING, Animations.BIPED_BLOCK);
		animatorClient.addLivingMixAnimation(LivingMotion.AIMING, Animations.BIPED_BOW_AIM);
		animatorClient.addLivingMixAnimation(LivingMotion.RELOADING, Animations.BIPED_CROSSBOW_RELOAD);
		animatorClient.addLivingMixAnimation(LivingMotion.SHOTING, Animations.BIPED_BOW_REBOUND);
		animatorClient.addLivingMixAnimation(LivingMotion.SUMMONNING, Animations.SUMMONNING_WAND);
		animatorClient.addLivingMixAnimation(LivingMotion.SPELLCASTING, Animations.MAGIC_DASH_WAND); //re-choose ketongu10
		animatorClient.addLivingMixAnimation(LivingMotion.TACHI, Animations.BIPED_TACHI_HELD);
		animatorClient.setCurrentLivingMotionsToDefault();
	}

	public void changeYaw(float amount) {
		this.yaw = amount;
	}
	
	@Override
	public void updateOnServer() {
		super.updateOnServer();
		this.tickSinceLastAction++;
		
		float stunArmor = this.getStunArmor();
		float maxStunArmor = this.getMaxStunArmor();
		
		if(stunArmor < maxStunArmor && this.tickSinceLastAction > 60) {
			float stunArmorFactor = 1.0F + (stunArmor / maxStunArmor);
			float healthFactor = this.orgEntity.getHealth() / this.orgEntity.getMaxHealth();
			this.setStunArmor(stunArmor + maxStunArmor * 0.01F * healthFactor * stunArmorFactor);
		}
		
		if (maxStunArmor < stunArmor) {
			this.setStunArmor(maxStunArmor);
		}
		float stamina = this.getStamina();
		float maxStamina = this.getMaxStamina();

		if(stamina < maxStamina && this.tickSinceLastAction > 60) {
			float healthFactor = this.orgEntity.getHealth() / this.orgEntity.getMaxHealth();
			this.setStamina(stamina + healthFactor * 1.5F * 70 / (float) getWeight());
		}

		if (maxStamina < stamina) {
			this.setStamina(maxStamina);
		}
	}
	
	@Override
	public void update() {
		if(this.orgEntity.getRidingEntity() == null) {
			for(SkillContainer container : this.skills) {
				if(container != null)
					container.update();
			}
		}
		super.update();
	}
	
	public SkillContainer getSkill(SkillSlot slot) {
		return this.skills[slot.getIndex()];
	}
	
	public SkillContainer getSkill(int slotIndex) {
		return this.skills[slotIndex];
	}
	
	public float getAttackSpeed() {
		return (float) this.getAttributeValue(SharedMonsterAttributes.ATTACK_SPEED);
	}
	
	public PlayerEventListener getEventListener() {
		return this.eventListeners;
	}
	
	@Override
	public boolean hurtBy(LivingAttackEvent event) {
		if(super.hurtBy(event)) {
			this.tickSinceLastAction = 0;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public IExtendedDamageSource getDamageSource(StunType stunType, DamageType damageType, int id) {
		return IExtendedDamageSource.causePlayerDamage(orgEntity, stunType, damageType, id);
	}
	
	@Override
	public StaticAnimation getHitAnimation(StunType stunType) {
		if(orgEntity.getRidingEntity() != null) {
			return Animations.BIPED_HIT_ON_MOUNT;
		} else {
			switch(stunType)
			{
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
	
	@Override
	public <M extends Model> M getEntityModel(Models<M> modelDB) {
		return modelDB.ENTITY_BIPED;

	}

	public float getMaxStamina() {
		IAttributeInstance stamina = this.orgEntity.getEntityAttribute(ModAttributes.MAX_STAMINA);
		return (float) (stamina == null ? 0 : stamina.getAttributeValue());
	}

	public float getStamina() {
		return getMaxStamina() == 0 ? 0 : this.orgEntity.getDataManager().get(DataKeys.STAMINA).floatValue();
	}

	public void setStamina(float value) {
		float f1 = Math.max(Math.min(value, this.getMaxStamina()), 0);
		this.orgEntity.getDataManager().set(DataKeys.STAMINA, f1);
	}

	public void resetActionTick() {
		this.tickSinceLastAction = 0;
	}
}