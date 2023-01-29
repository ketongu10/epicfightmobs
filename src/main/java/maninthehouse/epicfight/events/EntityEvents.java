package maninthehouse.epicfight.events;

import java.util.List;

import com.google.common.collect.Lists;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.windanesz.ancientspellcraft.entity.living.EntityEvilClassWizard;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.spell.Spell;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.ModCapabilities;
import maninthehouse.epicfight.capabilities.entity.CapabilityEntity;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.entity.mob.EndermanData;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.NoAnimationData;
import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninthehouse.epicfight.capabilities.item.CapabilityItem;
import maninthehouse.epicfight.effects.ModEffects;
import maninthehouse.epicfight.entity.event.PlayerEventListener;
import maninthehouse.epicfight.entity.event.TakeDamageEvent;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Sounds;
import maninthehouse.epicfight.main.EpicFightMod;
import maninthehouse.epicfight.network.ModNetworkManager;
import maninthehouse.epicfight.network.client.CTSPlayAnimation;
import maninthehouse.epicfight.network.server.STCPlayAnimation;
import maninthehouse.epicfight.network.server.STCPotion;
import maninthehouse.epicfight.network.server.STCPotion.Action;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource.StunType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.CombatRules;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.shadowmage.ancientwarfare.npc.entity.NpcBase;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFactionSpellcasterWizardry;

import static maninthehouse.epicfight.main.LoaderConstants.ANCIENT_WARFARE;

@Mod.EventBusSubscriber(modid=EpicFightMod.MODID)
public class EntityEvents {
	private static List<CapabilityEntity<?>> unInitializedEntitiesClient = Lists.<CapabilityEntity<?>>newArrayList();
	private static List<CapabilityEntity<?>> unInitializedEntitiesServer = Lists.<CapabilityEntity<?>>newArrayList();


	@SubscribeEvent
	public static void spawnEvent(EntityJoinWorldEvent event) {
		CapabilityEntity entitydata = event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
		if(entitydata != null && event.getEntity().ticksExisted == 0) {
			entitydata.onEntityJoinWorld(event.getEntity());
			if(entitydata.isRemote()) {
				unInitializedEntitiesClient.add(entitydata);
			} else {
				unInitializedEntitiesServer.add(entitydata);
			}
		}
	}
	@SubscribeEvent
	public static void livingAttackEvent(LivingAttackEvent event) {
		CapabilityEntity<?> entitydata = (CapabilityEntity<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
		if (entitydata != null && entitydata instanceof LivingData && event.getEntityLiving().getHealth() > 0.0F) {
			if (!((LivingData)entitydata).hurtBy(event)) {
				event.setCanceled(true);
			}
		}
	}

	
	@SubscribeEvent
	public static void updateEvent(LivingUpdateEvent event) {
		CapabilityEntity<?> entitydata = (CapabilityEntity<?>) event.getEntityLiving().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
		
		if(entitydata != null && entitydata instanceof LivingData && entitydata.getOriginalEntity() != null) {
			entitydata.update();
		}
	}
	
	@SubscribeEvent
	public static void knockBackEvent(LivingKnockBackEvent event) {
		CapabilityEntity<?> cap = event.getEntityLiving().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
		if (cap != null) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void hurtEvent(LivingHurtEvent event) {
		IExtendedDamageSource extSource = null;
		Entity trueSource = event.getSource().getTrueSource();
		
		if(trueSource != null) {
			CapabilityEntity<?> attackerdata = trueSource.getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
			if(event.getSource() instanceof IExtendedDamageSource) {
				extSource = (IExtendedDamageSource) event.getSource();
			} else if(attackerdata != null) {

				if(event.getSource() instanceof EntityDamageSourceIndirect) {
					if(attackerdata instanceof IRangedAttackMobCapability) {
						extSource = ((IRangedAttackMobCapability)attackerdata).getRangedDamageSource(event.getSource().getImmediateSource());
					} else if(event.getSource().damageType.equals("arrow")) {
						extSource = new IndirectDamageSourceExtended("arrow", trueSource, event.getSource().getImmediateSource(), StunType.SHORT);
						extSource.setImpact(1.0F);
					}

				} else if (attackerdata instanceof NoAnimationData) {
					extSource = ((NoAnimationData)attackerdata).getMeleeDamageSource(event.getSource().getImmediateSource());
				}

			}

			/**else if(event.getSource() instanceof EntityDamageSourceIndirect) {
				//CapabilityEntity<?> attackerdata = trueSource.getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
				
				if(attackerdata != null) {
					if(attackerdata instanceof IRangedAttackMobCapability) {
						extSource = ((IRangedAttackMobCapability)attackerdata).getRangedDamageSource(event.getSource().getImmediateSource());
					} else if(event.getSource().damageType.equals("arrow")) {
						extSource = new IndirectDamageSourceExtended("arrow", trueSource, event.getSource().getImmediateSource(), StunType.SHORT);
						extSource.setImpact(1.0F);
					}
				}
			} else if (attackerdata != null) {
				if (attackerdata instanceof NoAnimationData) {
					extSource = ((NoAnimationData)attackerdata).getMeleeDamageSource(event.getSource().getImmediateSource());
				}
			}**/

			if(extSource != null) {
				float totalDamage = event.getAmount();
				float ignoreDamage = event.getAmount() * extSource.getArmorIgnoreRatio();
				float calculatedDamage = ignoreDamage;
				EntityLivingBase hitEntity = event.getEntityLiving();
				
			    if(hitEntity.isPotionActive(MobEffects.RESISTANCE)) {
			    	int i = (hitEntity.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
			        int j = 25 - i;
			        float f = calculatedDamage * (float)j;
			        calculatedDamage = Math.max(f / 25.0F, 0.0F);
			    }
			    
			    if(calculatedDamage > 0.0F) {
			    	int k = EnchantmentHelper.getEnchantmentModifierDamage(hitEntity.getArmorInventoryList(), event.getSource());
			        if(k > 0) {
			        	calculatedDamage = CombatRules.getDamageAfterMagicAbsorb(calculatedDamage, (float)k);
			        }
			    }
			    
			    float absorpAmount = hitEntity.getAbsorptionAmount() - calculatedDamage;
			    hitEntity.setAbsorptionAmount(Math.max(absorpAmount, 0.0F));
		        
		        if(absorpAmount < 0.0F) {
		        	hitEntity.setHealth(hitEntity.getHealth() + absorpAmount);
		        	CapabilityEntity<?> attacker = (CapabilityEntity<?>)trueSource.getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
					if(attacker != null && attacker instanceof LivingData)
						((LivingData)attacker).gatherDamageDealt((IExtendedDamageSource)event.getSource(), calculatedDamage);
		        }
		        
				event.setAmount(totalDamage - ignoreDamage);
				
				if(event.getAmount() + ignoreDamage > 0.0F) {
					CapabilityEntity<?> hitEntityData = (CapabilityEntity<?>) hitEntity.getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
					
					if(hitEntityData != null && hitEntityData instanceof LivingData) {
						StaticAnimation hitAnimation = null;
						float extendStunTime = 0;
						float knockBackAmount = 0;
						float weightReduction = 40.0F / (float) ((LivingData)hitEntityData).getWeight();
						
						float currentStunResistance = ((LivingData)hitEntityData).getStunArmor();
						if(currentStunResistance > 0) {
							float impact = extSource.getImpact();
							((LivingData)hitEntityData).setStunArmor(currentStunResistance - impact);
						}
						
						switch(extSource.getStunType()) {
						case SHORT:
							if(!hitEntity.isPotionActive(ModEffects.STUN_IMMUNITY) && (((LivingData)hitEntityData).getStunArmor() == 0)) {
								int i = EnchantmentHelper.getKnockbackModifier((EntityLivingBase)trueSource);
								float totalStunTime = (float) ((0.25F + extSource.getImpact() * 0.1F + 0.1F * i) * weightReduction);
								totalStunTime *= (1.0F - ((LivingData)hitEntityData).getStunTimeTimeReduction());
								
								if(totalStunTime >= 0.1F) {
									extendStunTime = totalStunTime - 0.1F;
									boolean flag = totalStunTime >= 0.83F;
									StunType stunType = flag ? StunType.LONG : StunType.SHORT;
									extendStunTime = flag ? 0 : extendStunTime;
									hitAnimation = ((LivingData)hitEntityData).getHitAnimation(stunType);
									knockBackAmount = totalStunTime;
								}
							}
							break;
						case LONG:
							hitAnimation = hitEntity.isPotionActive(ModEffects.STUN_IMMUNITY) ? null : ((LivingData)hitEntityData).getHitAnimation(StunType.LONG);
							knockBackAmount = (extSource.getImpact() * 0.25F) * weightReduction;
							break;
						case HOLD:
							hitAnimation = ((LivingData)hitEntityData).getHitAnimation(StunType.SHORT);
							extendStunTime = extSource.getImpact() * 0.1F;
							break;
						}
						
						if(hitAnimation != null) {
							if(!(hitEntity instanceof EntityPlayer)) {
								((LivingData)hitEntityData).lookAttacker(trueSource);
							}
							((LivingData)hitEntityData).setStunTimeReduction();
							((LivingData)hitEntityData).getAnimator().playAnimation(hitAnimation, extendStunTime);
							ModNetworkManager.sendToAllPlayerTrackingThisEntity(new STCPlayAnimation(hitAnimation.getId(), hitEntity.getEntityId(), extendStunTime), hitEntity);
							if(hitEntity instanceof EntityPlayerMP) {
								ModNetworkManager.sendToPlayer(new STCPlayAnimation(hitAnimation.getId(), hitEntity.getEntityId(), extendStunTime), (EntityPlayerMP)hitEntity);
							}
						}

						((LivingData)hitEntityData).knockBackEntity(trueSource, knockBackAmount);
					}
				}
			}
		}
		
		if(event.getEntityLiving().isHandActive() && event.getEntityLiving().getActiveItemStack().getItem() == Items.SHIELD) {
			if(event.getEntityLiving() instanceof EntityPlayer) {
				event.getEntityLiving().world.playSound((EntityPlayer)event.getEntityLiving(), event.getEntityLiving().getPosition(), SoundEvents.ITEM_SHIELD_BLOCK,
						event.getEntityLiving().getSoundCategory(), 1.0F, 0.8F + event.getEntityLiving().getRNG().nextFloat() * 0.4F);
			}
		}
	}
	
	@SubscribeEvent
	public static void damageEvent(LivingDamageEvent event)
	{
		Entity trueSource = event.getSource().getTrueSource();

		if (event.getEntity() instanceof EntityPlayerMP) {
			ServerPlayerData serverplayerdata = (ServerPlayerData) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
			serverplayerdata.getEventListener().activateEvents(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT, new TakeDamageEvent(serverplayerdata, event));
		}

		if(event.getSource() instanceof IExtendedDamageSource)
		{
			if(trueSource != null)
			{
				CapabilityEntity<?> attacker = (CapabilityEntity<?>)trueSource.getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
				if(attacker!=null && attacker instanceof LivingData)
					((LivingData)attacker).gatherDamageDealt((IExtendedDamageSource)event.getSource(), event.getAmount());
			}
		}
	}
	
	/**@SubscribeEvent
	public static void attackEvent(LivingAttackEvent event)
	{
		CapabilityEntity<?> entitydata = (CapabilityEntity<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
		
		if(entitydata != null && entitydata instanceof LivingData && !event.getEntity().world.isRemote && event.getEntityLiving().getHealth() > 0.0F)
		{
			if(!((LivingData)entitydata).attackEntityFrom(event.getSource(), event.getAmount()))
			{
				event.setCanceled(true);
			}
		}
	}**/
	
	@SubscribeEvent
	public static void arrowHitEvent(ProjectileImpactEvent.Arrow event) {
		if (event.getRayTraceResult().entityHit != null && event.getArrow() != null && event.getArrow().shootingEntity != null) {
			if (event.getRayTraceResult().entityHit.equals(event.getArrow().shootingEntity.getRidingEntity())) {
				event.setCanceled(true);
			}
		}
	}


	@SubscribeEvent
	public static void equipChangeEvent(LivingEquipmentChangeEvent event)
	{
		if(event.getFrom().getItem() == event.getTo().getItem()) {
			return;
		}
		
		CapabilityEntity<?> entitycap = (CapabilityEntity<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
		
		if(entitycap != null && entitycap instanceof LivingData && entitycap.getOriginalEntity() != null)
		{
			if(event.getSlot() == EntityEquipmentSlot.MAINHAND)
			{
				CapabilityItem fromCap = ModCapabilities.stackCapabilityGetter(event.getFrom());
				CapabilityItem toCap = ModCapabilities.stackCapabilityGetter(event.getTo());
				((LivingData)entitycap).cancelUsingItem();
				
				if(fromCap != null)
					event.getEntityLiving().getAttributeMap().removeAttributeModifiers(fromCap.getAttributeModifiers(event.getSlot(), (LivingData)entitycap));
				if(toCap != null)
					event.getEntityLiving().getAttributeMap().applyAttributeModifiers(toCap.getAttributeModifiers(event.getSlot(), (LivingData)entitycap));
				
				if(entitycap instanceof ServerPlayerData)
				{
					ServerPlayerData playercap = (ServerPlayerData)entitycap;
					playercap.onHeldItemChange(toCap, event.getTo(), EnumHand.MAIN_HAND);
				}
			}
			else if(event.getSlot() == EntityEquipmentSlot.OFFHAND)
			{
				CapabilityItem fromCap = ModCapabilities.stackCapabilityGetter(event.getFrom());
				CapabilityItem toCap = ModCapabilities.stackCapabilityGetter(event.getTo());
				((LivingData)entitycap).cancelUsingItem();

				if(fromCap != null)
					event.getEntityLiving().getAttributeMap().removeAttributeModifiers(fromCap.getAttributeModifiers(event.getSlot(), (LivingData)entitycap));
				if(toCap != null)
					event.getEntityLiving().getAttributeMap().applyAttributeModifiers(toCap.getAttributeModifiers(event.getSlot(), (LivingData)entitycap));

				
				if(entitycap instanceof ServerPlayerData)
				{
					ServerPlayerData playercap = (ServerPlayerData)entitycap;
					CapabilityItem toCap2 = event.getTo().isEmpty() ? null : ((LivingData)entitycap).getHeldItemCapability(EnumHand.MAIN_HAND);
					playercap.onHeldItemChange(toCap2, event.getTo(), EnumHand.OFF_HAND);
				}
			}
			else if(event.getSlot().getSlotType() == EntityEquipmentSlot.Type.ARMOR)
			{
				CapabilityItem fromCap = ModCapabilities.stackCapabilityGetter(event.getFrom());
				CapabilityItem toCap = ModCapabilities.stackCapabilityGetter(event.getTo());
				
				if(fromCap != null) {
					event.getEntityLiving().getAttributeMap().removeAttributeModifiers(fromCap.getAttributeModifiers(event.getSlot(), (LivingData)entitycap));
				}
				
				if(toCap != null) {
					event.getEntityLiving().getAttributeMap().applyAttributeModifiers(toCap.getAttributeModifiers(event.getSlot(), (LivingData)entitycap));
				}
				
				LivingData<?> entitydata = (LivingData<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
				entitydata.onArmorSlotChanged(fromCap, toCap, event.getSlot());
			}
		}
		if (entitycap instanceof BipedMobData) {
			entitycap.postInit();
		}

	}
	
	@SubscribeEvent
	public static void effectAddEvent(PotionAddedEvent event) {
		if(!event.getEntity().world.isRemote) {
			ModNetworkManager.sendToAll(new STCPotion(event.getPotionEffect().getPotion(), Action.Active, event.getEntity().getEntityId()));
		}
	}
	
	@SubscribeEvent
	public static void effectRemoveEvent(PotionRemoveEvent event) {
		if(!event.getEntity().world.isRemote && event.getPotionEffect() != null) {
			ModNetworkManager.sendToAll(new STCPotion(event.getPotionEffect().getPotion(), Action.Remove, event.getEntity().getEntityId()));
		}
	}
	
	@SubscribeEvent
	public static void effectExpiryEvent(PotionExpiryEvent event) {
		if(!event.getEntity().world.isRemote) {
			ModNetworkManager.sendToAll(new STCPotion(event.getPotionEffect().getPotion(), Action.Remove, event.getEntity().getEntityId()));
		}
	}
	
	@SubscribeEvent
	public static void mountEvent(EntityMountEvent event) {

		if (event.isMounting() && ANCIENT_WARFARE) {
			if (!(event.getEntityMounting() instanceof NpcBase)) {
				CapabilityEntity<?> mountEntity = event.getEntityMounting().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
				if (!event.getWorldObj().isRemote && mountEntity instanceof BipedMobData && mountEntity.getOriginalEntity() != null) {
					if (event.getEntityBeingMounted() instanceof EntityCreature) {
						((BipedMobData<?>) mountEntity).onMount(event.isMounting(), event.getEntityBeingMounted());
					}
				}
			}
		} else {
			CapabilityEntity<?> mountEntity = event.getEntityMounting().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
			if (!event.getWorldObj().isRemote && mountEntity instanceof BipedMobData && mountEntity.getOriginalEntity() != null) {
				if (event.getEntityBeingMounted() instanceof EntityCreature) {
					((BipedMobData<?>) mountEntity).onMount(event.isMounting(), event.getEntityBeingMounted());
				}
			}
		}
	}


	
	@SubscribeEvent
	public static void tpEvent(EnderTeleportEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (event.getEntityLiving() instanceof EntityEnderman) {
			EntityEnderman enderman = (EntityEnderman)entity;
			EndermanData endermandata = (EndermanData) enderman.getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
			
			if (endermandata != null) {
				if (endermandata.isInaction()) {
					for (Entity collideEntity : enderman.world.getEntitiesWithinAABB(Entity.class, enderman.getEntityBoundingBox().grow(0.2D, 0.2D, 0.2D))) {
						if (collideEntity instanceof IProjectile) {
	                    	return;
	                    }
	                }
					
					event.setCanceled(true);
				} else if (endermandata.isRaging()) {
					event.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void jumpEvent(LivingJumpEvent event) {
		CapabilityEntity<?> entitydata = (CapabilityEntity<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
		
		if (entitydata != null && entitydata instanceof LivingData && entitydata.isRemote()) {
			if (!((LivingData)entitydata).isInaction() && !event.getEntity().isInWater()) {
				StaticAnimation jumpAnimation = ((LivingData)entitydata).getClientAnimator().getJumpAnimation();
				((LivingData)entitydata).getAnimator().playAnimation(jumpAnimation, 0);
				ModNetworkManager.sendToServer(new CTSPlayAnimation(jumpAnimation.getId(), 0, true, false));
			}
		}
	}
	
	@SubscribeEvent
	public static void deathEvent(LivingDeathEvent event) {
		CapabilityEntity<?> entitydata = (CapabilityEntity<?>) event.getEntityLiving().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
		
		if(entitydata != null && entitydata instanceof LivingData) {
			((LivingData)entitydata).getAnimator().playDeathAnimation();
		}
	}
	
	@SubscribeEvent
	public static void fallEvent(LivingFallEvent event) {
		if (event.getEntity().world.getGameRules().getBoolean("hasFallAnimation")) {
			CapabilityEntity<?> entitydata = (CapabilityEntity<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
			
			if (entitydata != null && entitydata instanceof LivingData && !((LivingData)entitydata).isInaction()) {
				float distance = event.getDistance();

				if (distance > 5.0F) {
					((LivingData)entitydata).getAnimator().playAnimation(Animations.BIPED_LAND_DAMAGE, 0);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void tickClientEvent(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			for (CapabilityEntity<?> cap : unInitializedEntitiesClient) {
				cap.postInit();
			}
			unInitializedEntitiesClient.clear();
		}
	}
	
	@SubscribeEvent
	public static void tickServerEvent(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			for (CapabilityEntity<?> cap : unInitializedEntitiesServer) {
				cap.postInit();
			}
			unInitializedEntitiesServer.clear();
		}
	}
}