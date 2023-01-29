package maninthehouse.epicfight.client.events.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.item.ItemScroll;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WandHelper;
import maninthehouse.epicfight.capabilities.item.MagicWandCapability;
import maninthehouse.epicfight.entity.event.MovementInputEvent;
import maninthehouse.epicfight.entity.event.PlayerEventListener;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.item.LongswordItem;
import maninthehouse.epicfight.item.WeaponItem;
import maninthehouse.epicfight.skill.GuardSkill;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.ModCapabilities;
import maninthehouse.epicfight.capabilities.entity.LivingData.EntityState;
import maninthehouse.epicfight.capabilities.item.CapabilityItem;
import maninthehouse.epicfight.client.ClientEngine;
import maninthehouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninthehouse.epicfight.client.input.ModKeys;
import maninthehouse.epicfight.config.ConfigurationIngame;
import maninthehouse.epicfight.main.EpicFightMod;
import maninthehouse.epicfight.network.ModNetworkManager;
import maninthehouse.epicfight.network.client.CTSPlayAnimation;
import maninthehouse.epicfight.skill.SkillContainer;
import maninthehouse.epicfight.skill.SkillSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.google.common.primitives.Floats.max;
import static maninthehouse.epicfight.main.LoaderConstants.EBWIZARDRY;

@SideOnly(Side.CLIENT)
public class ControllEngine {
	private Map<KeyBinding, Consumer<KeyBinding>> keyFunctionMap;
	private EntityPlayerSP player;
	private ClientPlayerData playerdata;
	private int comboHoldCounter;
	private int comboCounter;
	private int mouseLeftPressCounter = 0;
	private int mouseRightPressCounter = 0;
	private int sneakPressCounter = 0;
	private int sprintPressCounter = 0;
	private int reservedSkill;
	private int skillReserveCounter;
	private boolean sneakPressToggle = false;
	private boolean sprintPressToggle = false;
	private boolean mouseLeftPressToggle = false;
	private boolean mouseRightPressToggle = false;
	private boolean lightPress;
	private boolean lightPress_item;
	
	public GameSettings gameSettings;

	public ControllEngine() {
		Events.controllEngine = this;
		this.gameSettings = Minecraft.getMinecraft().gameSettings;
		this.keyFunctionMap = new HashMap<KeyBinding, Consumer<KeyBinding>>();
		this.keyFunctionMap.put(this.gameSettings.keyBindAttack, this::attackKeyPressed);//keybindattack++
		this.keyFunctionMap.put(this.gameSettings.keyBindUseItem, this::UseItemKeyPressed);//by ketongu10
		this.keyFunctionMap.put(ModKeys.STEP_BACK, this::sprintKeyPressed);//by ketongu10
		this.keyFunctionMap.put(this.gameSettings.keyBindSwapHands, this::swapHandKeyPressed);
		this.keyFunctionMap.put(this.gameSettings.keyBindSneak, this::sneakKeyPressed);
		this.keyFunctionMap.put(ModKeys.SWITCH_MODE, this::switchModeKeyPressed);
		this.keyFunctionMap.put(ModKeys.DODGE, this::dodgeKeyPressed);

		//this.keyFunctionMap.put(ModKeys.CAST1, this::TrueMagic1KeyPressed);
	}
	
	public void setGamePlayer(ClientPlayerData playerdata) {
		this.comboCounter = 0;
		this.mouseLeftPressCounter = 0;
		this.mouseLeftPressToggle = false;
		this.mouseRightPressCounter = 0;
		this.mouseRightPressToggle = false;
		this.sneakPressCounter = 0;
		this.sneakPressToggle = false;
		this.sprintPressCounter = 0;
		this.sprintPressToggle = false;
		this.lightPress = false;
		this.lightPress_item = false;
		this.player = playerdata.getOriginalEntity();
		this.playerdata = playerdata;


	}

	public boolean playerCanMove(EntityState playerState) {
		/**if (Loader.isModLoaded("iceandfire")) {
			if (this.player.getRidingEntity() != null) {

			}
			return !playerState.isMovementLocked() || this.player.isRidingHorse() || this.player.isRidingHorse()
		}**/
		return !playerState.isMovementLocked() || this.player.isRidingHorse();
	}

	public boolean playerCanRotate(EntityState playerState) {
		return !playerState.isCameraRotationLocked() || this.player.isRidingHorse();
	}

	public boolean playerCanAct(EntityState playerState) {
		return !this.player.isSpectator() && !(this.player.isElytraFlying() || this.playerdata.currentMotion == LivingMotion.FALL || playerState.isMovementLocked());
	}

	public boolean playerCanDodging(EntityState playerState) {
		return !this.player.isSpectator() && !(this.player.isElytraFlying() || this.playerdata.currentMotion == LivingMotion.FALL || !playerState.canAct());
	}

	public boolean playerCanExecuteSkill(EntityState playerState) {
		return !this.player.isSpectator() && !(this.player.isElytraFlying() || this.playerdata.currentMotion == LivingMotion.FALL || !playerState.canAct());
	}
	
	private void attackKeyPressed(KeyBinding key) {
		if (ClientEngine.INSTANCE.isBattleMode()) {
			this.setKeyBind(this.gameSettings.keyBindAttack, false);//keybindattack++
			this.gameSettings.keyBindAttack.isPressed();//keybindattack++
			if (this.player.getItemInUseCount() == 0) {
				if (!this.mouseLeftPressToggle) {
					this.mouseLeftPressToggle = true;
				}
			}
		} else {
			this.gameSettings.keyBindAttack.pressTime++;//keybindattack++
		}
		
		if (this.player.getCooledAttackStrength(0) < 0.9F) {
			this.gameSettings.keyBindAttack.pressTime = 0;//keybindattack++
		}
	}


	/**BY KETONGU10 - just funning**/
	/**private void TrueMagic1KeyPressed(KeyBinding key) {
		this.playerdata.getAnimator().playAnimation(Animations.SWORD_DASH, 0);
	}**/

	/**BY KETONGU10**/
	private void UseItemKeyPressed(KeyBinding key) {
		if (ClientEngine.INSTANCE.isBattleMode()) {
			this.gameSettings.keyBindUseItem.isPressed();/**2**/
			if (this.player.getItemInUseCount() == 0) {
				if (!this.mouseRightPressToggle) {
					this.mouseRightPressToggle = true;
				}
			}
		} else {
			this.gameSettings.keyBindUseItem.pressTime++;
		}

		if (this.player.getCooledAttackStrength(0) < 0.9F) {
			this.gameSettings.keyBindUseItem.pressTime = 0;
		}
	}
	private void stepBackKeyPressed(KeyBinding key) {
		if (!Keyboard.isRepeatEvent()) {
			if (key.getKeyCode() == this.gameSettings.keyBindSprint.getKeyCode()) {
				if (this.player.getRidingEntity() == null && ClientEngine.INSTANCE.isBattleMode()) {
					if (!this.sprintPressToggle) {
						this.sprintPressToggle = true;
					}
				}
			} else {
				SkillContainer skill = this.playerdata.getSkill(SkillSlot.STEP_BACK);
				if (skill.canExecute(this.playerdata) && skill.getContaining().isExecutableState(this.playerdata)) {
					skill.execute(this.playerdata);
				}
			}
		}
	}

	private void sprintKeyPressed(KeyBinding key) {
		if (ModKeys.STEP_BACK.getKeyCode() == this.gameSettings.keyBindSprint.getKeyCode()) {
			if (!this.sprintPressToggle && !this.gameSettings.keyBindForward.isKeyDown()) {
				this.stepBackKeyPressed(ModKeys.STEP_BACK);
			}
		}
	}
	/**END KETONGU10**/


	private void dodgeKeyPressed(KeyBinding key) {
		if (!Keyboard.isRepeatEvent()) {
			if (key.getKeyCode() == this.gameSettings.keyBindSneak.getKeyCode()) {
				if (this.player.getRidingEntity() == null && ClientEngine.INSTANCE.isBattleMode()) {
					if (!this.sneakPressToggle) {
						this.sneakPressToggle = true;
					}
				}
			} else {
				SkillContainer skill = this.playerdata.getSkill(SkillSlot.DODGE);
				if (skill.canExecute(this.playerdata) && skill.getContaining().isExecutableState(this.playerdata)) {
					skill.execute(this.playerdata);
				}
			}
		}
	}
	
	private void sneakKeyPressed(KeyBinding key) {
		if (ModKeys.DODGE.getKeyCode() == this.gameSettings.keyBindSneak.getKeyCode()) {
			if (!this.sneakPressToggle) {
				this.dodgeKeyPressed(ModKeys.DODGE);
			}
		}
	}

	
	private void swapHandKeyPressed(KeyBinding key) {
		CapabilityItem cap = this.playerdata.getHeldItemCapability(EnumHand.MAIN_HAND);
		if (this.playerdata.isInaction() || (cap != null && !cap.canUsedInOffhand())) {
			key.pressTime = 0;
			this.setKeyBind(key, false);
		} else {
			key.pressTime++;
		}
	}
	
	private void switchModeKeyPressed(KeyBinding key) {
		if (!Keyboard.isRepeatEvent()) {
			ClientEngine.INSTANCE.toggleActingMode();
		}
	}
	
	public void tick() {
		if (this.playerdata == null) {
			return;
		}
		ItemStack itemheld = this.player.getHeldItemMainhand();
		EntityState playerState = this.playerdata.getEntityState();

		if (this.mouseLeftPressToggle) {
			if (!this.isKeyDown(this.gameSettings.keyBindAttack)) {
				lightPress = true;
				this.mouseLeftPressToggle = false;
				this.mouseLeftPressCounter = 0;
			} else {
				if (this.mouseLeftPressCounter > ConfigurationIngame.longPressCount) {
					if (this.playerCanExecuteSkill(playerState)) {
						CapabilityItem itemCap = this.playerdata.getHeldItemCapability(EnumHand.MAIN_HAND);
						if(itemCap != null) {
							this.playerdata.getSkill(SkillSlot.WEAPON_SPECIAL_ATTACK).execute(this.playerdata);
						}
					} else {
						if (!this.player.isSpectator()) {
							this.reserveSkill(SkillSlot.WEAPON_SPECIAL_ATTACK);
						}
					}
					this.mouseLeftPressToggle = false;
					this.mouseLeftPressCounter = 0;
					this.resetAttackCounter();
				} else {
					this.setKeyBind(this.gameSettings.keyBindAttack, false);
					this.mouseLeftPressCounter++;
				}
			}
		}
		/**BY KETONGU10**/
		if (this.mouseRightPressToggle && EBWIZARDRY && itemheld.getItem() instanceof ItemWand) {
            Spell currentSpell = (((ItemWand) itemheld.getItem()).getCurrentSpell(itemheld));
            if (!currentSpell.isContinuous && currentSpell.getChargeup() == 0){
                lightPress_item = true;
            }
            if (this.lightPress_item) {
                if (this.playerCanAct(playerState)) {
                    playSpellCastingMotion(this.player.getHeldItemMainhand(), this.player.isSprinting());
                    this.player.resetCooldown();
                } else {
                    if (this.player.isSpectator() || playerState.getLevel() < 2) {
                    }
                }
                this.lightPress_item = false;
                this.mouseRightPressToggle = false;
                this.mouseRightPressCounter = 0;
            }
		} else {
			this.mouseRightPressToggle = false;
		}

		/**END KETONGU10**/

		if (this.lightPress) {
			if (this.playerCanAct(playerState)) {
				playAttackMotion(this.player.getHeldItemMainhand(), this.player.isSprinting());
				this.player.resetCooldown();
				this.lightPress = false;
			} else {
				if (this.player.isSpectator() || playerState.getLevel() < 2) {
					this.lightPress = false;
				}
			}
			
			this.lightPress = false;
			this.mouseLeftPressToggle = false;
			this.mouseLeftPressCounter = 0;

		}


		if (this.sneakPressToggle) {
			if (!this.isKeyDown(this.gameSettings.keyBindSneak)) {
				SkillContainer skill = this.playerdata.getSkill(SkillSlot.DODGE);
				
				if (skill.canExecute(this.playerdata) && skill.getContaining().isExecutableState(this.playerdata)) {
					skill.execute(this.playerdata);
				} else {
					this.reserveSkill(SkillSlot.DODGE);
				}
				
				this.sneakPressToggle = false;
				this.sneakPressCounter = 0;
			} else {
				if (this.sneakPressCounter > ConfigurationIngame.longPressCount) {
					this.sneakPressToggle = false;
					this.sneakPressCounter = 0;
				} else {
					this.sneakPressCounter++;
				}
			}
		}
		/**BY KETONGU10**/
		if (this.sprintPressToggle) {
			if (!this.isKeyDown(this.gameSettings.keyBindSprint)) {
				SkillContainer skill = this.playerdata.getSkill(SkillSlot.STEP_BACK);

				if (skill.canExecute(this.playerdata) && skill.getContaining().isExecutableState(this.playerdata)) {
					skill.execute(this.playerdata);
				} else {
					this.reserveSkill(SkillSlot.STEP_BACK);
				}

				this.sprintPressToggle = false;
				this.sprintPressCounter = 0;
			} else {
				if (this.sprintPressCounter > ConfigurationIngame.longPressCount) {
					this.sprintPressToggle = false;
					this.sprintPressCounter = 0;
				} else {
					this.sprintPressCounter++;
				}
			}
		}/**END KETONGU10**/
		
		if (this.reservedSkill >= 0) {
			if (skillReserveCounter > 0) {
				SkillContainer skill = this.playerdata.getSkill(this.reservedSkill);
				this.skillReserveCounter--;
				if (skill.getContaining() != null && skill.canExecute(this.playerdata) && skill.getContaining().isExecutableState(this.playerdata)) {
					skill.execute(this.playerdata);
					this.reservedSkill = -1;
					this.skillReserveCounter = -1;
				}
			} else {
				this.reservedSkill = -1;
				this.skillReserveCounter = -1;
			}
		}
		
		if (this.comboHoldCounter > 0) {
			float f = this.player.getCooledAttackStrength(0);
			
			if (!playerState.isMovementLocked() && !playerState.isCameraRotationLocked() && f >= 1.0F) {
				--this.comboHoldCounter;
				
				if (this.comboHoldCounter == 0) {
					this.resetAttackCounter();
				}
			}
		}


		for (int i = 0; i < 9; ++i) {
			if (isKeyDown(this.gameSettings.keyBindsHotbar[i])) {
				if (this.playerdata.isInaction()) {
					this.gameSettings.keyBindsHotbar[i].isPressed();
				}
			}
		}
	}
	
	private void playAttackMotion(ItemStack holdItem, boolean dashAttack) {
		CapabilityItem cap = holdItem.getCapability(ModCapabilities.CAPABILITY_ITEM, null);
		StaticAnimation attackMotion = null;
		
		if (this.player.getRidingEntity() != null) {
			if (this.player.isRidingHorse() && cap != null && cap.canUseOnMount()) {
				attackMotion = cap.getMountAttackMotion().get(this.comboCounter);
				this.comboCounter += 1;
				this.comboCounter %= cap.getMountAttackMotion().size();
			}
		} else {
			List<StaticAnimation> combo = null;
			
			if(combo == null) {
				combo = (cap != null) ? combo = cap.getAutoAttckMotion(this.playerdata) : CapabilityItem.getBasicAutoAttackMotion();
			}
			
			int comboSize = combo.size();
			if(dashAttack) {
				this.comboCounter = comboSize - 1;
			} else {
				this.comboCounter %= comboSize - 2;
			}

			if (!this.player.onGround && !this.player.isInWater() && this.player.motionY > -0.05D) {
				this.comboCounter = comboSize - 2;
			}

			attackMotion = combo.get(this.comboCounter);
			this.comboCounter = dashAttack ? 0 : this.comboCounter+1;
		}
		
		this.comboHoldCounter = 10;
		
		if(attackMotion != null) {
			this.playerdata.getAnimator().playAnimation(attackMotion, 0); //only animation
			ModNetworkManager.sendToServer(new CTSPlayAnimation(attackMotion, 0, false, false)); //only damage
		}
	}

	private void playGuardMotion(ItemStack holdItem, Boolean isBlocking) {
		CapabilityItem cap = holdItem.getCapability(ModCapabilities.CAPABILITY_ITEM, null);
		StaticAnimation animation = Animations.LONGSWORD_GUARD;

		if (animation != null) {
			playerdata.playAnimationSynchronize(animation, 0);
		}
	}


	private void playSpellCastingMotion(ItemStack holdItem, boolean dashAttack) {
        this.setKeyBind(this.gameSettings.keyBindUseItem, false);
        CapabilityItem cap = holdItem.getCapability(ModCapabilities.CAPABILITY_ITEM, null);
        StaticAnimation attackMotion = null;
        Spell currentSpell = (((ItemWand) holdItem.getItem()).getCurrentSpell(holdItem));
        EnumAction useAction = holdItem.getItemUseAction();
        if (this.player.getRidingEntity() != null) {
            if (this.player.isRidingHorse() && cap != null && cap.canUseOnMount()) {
                attackMotion = cap.getMountAttackMotion().get(this.comboCounter);
                this.comboCounter += 1;
                this.comboCounter %= cap.getMountAttackMotion().size();
            }
        } else if (currentSpell.getChargeup() == 0) {
            if (useAction == SpellActions.POINT_UP || useAction == SpellActions.POINT_DOWN || useAction == SpellActions.IMBUE || useAction == SpellActions.SUMMON) {
                this.playerdata.getAnimator().playAnimation(Animations.TOOL_AUTO_2, max(currentSpell.getChargeup() / 5 - 1, 0)); //NOT FOR SUMMON!
            } else {
                List<StaticAnimation> combo = null;

                if(combo == null) {
                    combo = (cap != null) ? combo = cap.getMagicMotion(this.playerdata) : CapabilityItem.getBasicAutoAttackMotion();
                }

                int comboSize = combo.size();
                if(dashAttack) {
                    this.comboCounter = comboSize - 1;
                } else {
                    this.comboCounter %= comboSize - 1;
                }

                attackMotion = combo.get(this.comboCounter);
                this.comboCounter = dashAttack ? 0 : this.comboCounter+1;
            }
        }

		this.comboHoldCounter = 10;

		if(attackMotion != null) {
			this.playerdata.getAnimator().playAnimation(attackMotion, 0); //only animation
			//ModNetworkManager.sendToServer(new CTSPlayAnimation(attackMotion, 0, false, false)); //only damage

			//castSpell(this.player.world, EnumHand.MAIN_HAND);
		}
	}



	private void reserveSkill(SkillSlot slot) {
		this.reservedSkill = slot.getIndex();
		this.skillReserveCounter = 8;
	}
	
	public boolean isKeyDown(KeyBinding key) {
		int keyCode = key.getKeyCode();
		
		if (keyCode > 0) {
			return Keyboard.isKeyDown(key.getKeyCode());
		} else {
			return Mouse.isButtonDown(key.getKeyCode() + 100);
		}
	}
	
	public void setKeyBind(KeyBinding key, boolean setter) {
		KeyBinding.setKeyBindState(key.getKeyCode(), setter);
	}
	
	public void resetAttackCounter() {
		comboCounter = 0;
	}
	
	@SideOnly(Side.CLIENT)
	@Mod.EventBusSubscriber(modid = EpicFightMod.MODID, value = Side.CLIENT)
	public static class Events {
		static ControllEngine controllEngine;
		
		@SubscribeEvent
		public static void mouseEvent(MouseInputEvent event) {
			if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().currentScreen == null) {
				for (Map.Entry<KeyBinding, Consumer<KeyBinding>> entry : controllEngine.keyFunctionMap.entrySet()) {
					if (entry.getKey().pressTime > 0) {
						entry.getKey().pressTime--;
						controllEngine.keyFunctionMap.get(entry.getKey()).accept(entry.getKey());
					}
				}
			}
		}
		
		@SubscribeEvent
		public static void keyboardEvent(KeyInputEvent event) {
			if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().currentScreen == null) {
				for (Map.Entry<KeyBinding, Consumer<KeyBinding>> entry : controllEngine.keyFunctionMap.entrySet()) {
					if (entry.getKey().pressTime > 0) {
						entry.getKey().pressTime--;
						controllEngine.keyFunctionMap.get(entry.getKey()).accept(entry.getKey());
					}
				}
			}
		}
		
		@SubscribeEvent
		public static void mouseEvent(MouseEvent event) {
			int dwheel = event.getDwheel();
			if (dwheel != 0) {
				if (controllEngine.playerdata != null) {
					if (controllEngine.playerdata.isInaction() ) {
						event.setCanceled(true);
					} else {
						controllEngine.playerdata.cancelUsingItem();
						controllEngine.resetAttackCounter();
					}
				}
			}
		}
		
		/**
		@SubscribeEvent
		public static void mouseScrollEvent(MouseScrollEvent event) {
			if (Minecraft.getMinecraft().player != null && controllEngine.playerdata != null && controllEngine.playerdata.isInaction()) {
				if(Minecraft.getMinecraft().currentScreen == null) {
					event.setCanceled(true);
				}
			}
		}**/
		
		@SubscribeEvent
		public static void cancelPlayerRotationWhenInaction(TickEvent.RenderTickEvent event) {
			if (controllEngine.playerdata != null) {
				EntityState playerState = controllEngine.playerdata.getEntityState();
				if (!controllEngine.playerCanRotate(playerState) && controllEngine.player.isEntityAlive()) {
					Mouse.getDX();
					Mouse.getDY();
				}
			}
		}
		
		@SubscribeEvent
		public static void moveInputEvent(InputUpdateEvent event) {
			if(controllEngine.playerdata == null) {
				return;
			}
			
			EntityState playerState = controllEngine.playerdata.getEntityState();
			
			if (!controllEngine.playerCanMove(playerState)) {
				event.getMovementInput().moveForward = 0F;
				event.getMovementInput().moveStrafe = 0F;
				event.getMovementInput().forwardKeyDown = false;
				event.getMovementInput().backKeyDown = false;
				event.getMovementInput().leftKeyDown = false;
				event.getMovementInput().rightKeyDown = false;
				event.getMovementInput().jump = false;
				event.getMovementInput().sneak = false;
				((EntityPlayerSP)event.getEntityPlayer()).sprintToggleTimer = -1;
			}

			if (event.getEntityPlayer().isEntityAlive()) {
				controllEngine.playerdata.getEventListener().activateEvents(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, new MovementInputEvent(controllEngine.playerdata,
						event.getMovementInput()));
			}
		}
		
		@SubscribeEvent
		public static void preProcessKeyBindings(TickEvent.ClientTickEvent event) {
			if (event.phase == TickEvent.Phase.START) {
				if (Minecraft.getMinecraft().player != null) {
					controllEngine.tick();
				}
			}
		}
	}
}