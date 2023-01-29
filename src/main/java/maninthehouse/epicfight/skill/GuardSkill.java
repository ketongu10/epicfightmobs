package maninthehouse.epicfight.skill;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.ModCapabilities;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.capabilities.entity.player.PlayerData;
import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninthehouse.epicfight.capabilities.item.CapabilityItem;
import maninthehouse.epicfight.client.gui.BattleModeGui;
import maninthehouse.epicfight.entity.event.HitEvent;
import maninthehouse.epicfight.entity.event.PlayerEvent;
import maninthehouse.epicfight.entity.event.PlayerEventListener;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Skills;
import maninthehouse.epicfight.gamedata.Sounds;
import maninthehouse.epicfight.network.server.STCLivingMotionChange;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class GuardSkill extends Skill{
    protected static final SkillDataManager.SkillDataKey<Integer> LAST_HIT_TICK = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
    protected static final SkillDataManager.SkillDataKey<Float> PENALTY = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.FLOAT);
    protected static final UUID EVENT_UUID = UUID.fromString("b422f7a0-f378-11eb-9a03-0242ac130003");

    private static final Map<CapabilityItem.WeaponCategory, BiFunction<CapabilityItem, PlayerData<?>, StaticAnimation>> AVAILABLE_WEAPON_TYPES =
            Maps.<CapabilityItem.WeaponCategory, BiFunction<CapabilityItem, PlayerData<?>, StaticAnimation>>newLinkedHashMap();

    static {
        AVAILABLE_WEAPON_TYPES.put(CapabilityItem.WeaponCategory.AXE, (item, player) -> Animations.SWORD_GUARD_HIT);
        AVAILABLE_WEAPON_TYPES.put(CapabilityItem.WeaponCategory.GREATSWORD, (item, player) -> Animations.GREATSWORD_GUARD_HIT);
        AVAILABLE_WEAPON_TYPES.put(CapabilityItem.WeaponCategory.KATANA, (item, player) -> Animations.KATANA_GUARD_HIT);
        AVAILABLE_WEAPON_TYPES.put(CapabilityItem.WeaponCategory.LONGSWORD, (item, player) -> Animations.LONGSWORD_GUARD_HIT);
        AVAILABLE_WEAPON_TYPES.put(CapabilityItem.WeaponCategory.SPEAR, (item, player) -> item.getStyle(player) == CapabilityItem.WieldStyle.TWO_HAND ? Animations.SPEAR_GUARD_HIT : null);
        AVAILABLE_WEAPON_TYPES.put(CapabilityItem.WeaponCategory.SWORD, (item, player) -> item.getStyle(player) == CapabilityItem.WieldStyle.ONE_HAND ? Animations.SWORD_GUARD_HIT : Animations.SWORD_DUAL_GUARD_HIT);
        //AVAILABLE_WEAPON_TYPES.put(WeaponCategory.TACHI, (item, player) -> Animations.LONGSWORD_GUARD_HIT);
    }

    public GuardSkill(String skillName) {
        super(SkillSlot.WEAPON_GUARD, 0,  skillName);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        container.getDataManager().registerData(LAST_HIT_TICK);
        container.getDataManager().registerData(PENALTY);
        container.executer.getEventListener().addEventListener(PlayerEventListener.EventType.CLIENT_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
            CapabilityItem itemCapability = event.getPlayerData().getHeldItemCapability(EnumHand.MAIN_HAND);
            if (itemCapability != null) {
                if (AVAILABLE_WEAPON_TYPES.getOrDefault(itemCapability.getWeaponCategory(), (a, b) -> null).apply(itemCapability, event.getPlayerData()) != null
                        && this.isExecutableState(event.getPlayerData())) {
                    event.getPlayerData().getOriginalEntity().setActiveHand(EnumHand.MAIN_HAND);
                }
            }
            return false;
        });

        container.executer.getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
            CapabilityItem itemCapability = event.getPlayerData().getHeldItemCapability(EnumHand.MAIN_HAND);
            if (itemCapability != null) {
                if (AVAILABLE_WEAPON_TYPES.getOrDefault(itemCapability.getWeaponCategory(), (a, b) -> null).apply(itemCapability, event.getPlayerData()) != null
                        && this.isExecutableState(event.getPlayerData())) {
                    event.getPlayerData().getOriginalEntity().setActiveHand(EnumHand.MAIN_HAND);
                }
            }
            this.onReset(container);
            return false;
        });

        container.executer.getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_STOP_EVENT, EVENT_UUID, (event) -> {
            EntityPlayerMP serverplayerentity = event.getPlayerData().getOriginalEntity();
            //container.getDataManager().setDataSync(LAST_HIT_TICK, serverplayerentity.ticksExisted, serverplayerentity);
            return false;
        });

        container.executer.getEventListener().addEventListener(PlayerEventListener.EventType.DEALT_DAMAGE_POST_EVENT, EVENT_UUID, (event) -> {
            //container.getDataManager().setDataSync(PENALTY, 0.0F, event.getPlayerData().getOriginalEntity());
            return false;
        });

        container.executer.getEventListener().addEventListener(PlayerEventListener.EventType.HIT_EVENT, EVENT_UUID, (event) -> {
            CapabilityItem itemCapability = event.getPlayerData().getHeldItemCapability(event.getPlayerData().getOriginalEntity().getActiveHand());
            if (itemCapability != null) {
                if (this.getHitMotion(event.getPlayerData(), itemCapability, 0) != null && event.getPlayerData().getOriginalEntity().isHandActive()
                        && this.isExecutableState(event.getPlayerData())) {
                    DamageSource damageSource = event.getForgeEvent().getSource();
                    boolean isFront = false;
                    Vec3d damageLocation = damageSource.getDamageLocation();

                    if (damageLocation != null) {
                        Vec3d vector3d = event.getPlayerData().getOriginalEntity().getLook(1.0F);
                        Vec3d vector3d1 = damageLocation.subtractReverse(event.getPlayerData().getOriginalEntity().getPositionVector()).normalize();
                        vector3d1 = new Vec3d(vector3d1.x, 0.0D, vector3d1.z);
                        if (vector3d1.dotProduct(vector3d) < 0.0D) {
                            isFront = true;
                        }
                    }

                    if (isFront) {
                        float impact = 0.5F;
                        float knockback = 0.25F;
                        if (event.getForgeEvent().getSource() instanceof IExtendedDamageSource) {
                            impact = ((IExtendedDamageSource) event.getForgeEvent().getSource()).getImpact();
                            knockback += impact * 0.1F;
                        }

                        return this.guard(container, itemCapability, event, knockback, impact, false);
                    }
                }
            }
            return false;
        }, 1);
    }

    public boolean guard(SkillContainer container, CapabilityItem itemCapability, HitEvent event, float knockback, float impact, boolean reinforced) {
        DamageSource damageSource = event.getForgeEvent().getSource();
        if (this.isBlockableSource(event.getForgeEvent().getSource(), reinforced)) {
            event.getPlayerData().playSound(Sounds.CLASH, -0.05F, 0.1F);
            Entity playerentity = event.getPlayerData().getOriginalEntity();
            /**Particles.HIT_BLUNT.get().spawnParticleWithArgument(((ServerWorld)playerentity.world), HitParticleType.POSITION_MIDDLE_OF_EACH_ENTITY,
                    HitParticleType.ARGUMENT_ZERO, playerentity, damageSource.getImmediateSource());**/

            if (damageSource.getImmediateSource() instanceof EntityLiving) {
                knockback += EnchantmentHelper.getKnockbackModifier((EntityLiving)damageSource.getImmediateSource()) * 0.1F;
            }

            float penalty = container.getDataManager().getDataValue(PENALTY) + this.getPenaltyStamina(itemCapability);
            event.getPlayerData().knockBackEntity(damageSource.getImmediateSource(), knockback);
            float stamina = event.getPlayerData().getStamina() - penalty * impact;
            event.getPlayerData().setStamina(stamina);
            //container.getDataManager().setDataSync(PENALTY, penalty, event.getPlayerData().getOriginalEntity());

            StaticAnimation animation = this.getHitMotion(event.getPlayerData(), itemCapability, stamina >= 0 ? 1 : 0);

            if (animation != null) {
                event.getPlayerData().playAnimationSynchronize(animation, 0);
            }

            return stamina >= 0.0F;
        } else {
            return false;
        }
    }

    protected float getPenaltyStamina(CapabilityItem itemCapability) {
        return 6F;
    }

    public static Map<CapabilityItem.WeaponCategory, BiFunction<CapabilityItem, PlayerData<?>, StaticAnimation>> getAvailableWeaponTypes(int meta) {
        return AVAILABLE_WEAPON_TYPES;
    }

    @Override
    public void update(SkillContainer container) {
        super.update(container);
        if (!container.executer.isRemote() && !container.executer.getOriginalEntity().isHandActive()) {
            float penalty = container.getDataManager().getDataValue(PENALTY);
            if (penalty > 0) {
                int hitTick = container.getDataManager().getDataValue(LAST_HIT_TICK);
                if (container.executer.getOriginalEntity().ticksExisted - hitTick > 40) {
                    container.getDataManager().setDataSync(PENALTY, 0.0F, (EntityPlayerMP) container.executer.getOriginalEntity());
                }
            }
        } else {
            container.executer.resetActionTick();
        }
    }

    @Override
    public void onDeleted(SkillContainer container) {
        container.executer.getEventListener().removeListener(PlayerEventListener.EventType.HIT_EVENT, EVENT_UUID);
        container.executer.getEventListener().removeListener(PlayerEventListener.EventType.CLIENT_ITEM_USE_EVENT, EVENT_UUID);
        container.executer.getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
        container.executer.getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_STOP_EVENT, EVENT_UUID);
        container.executer.getEventListener().removeListener(PlayerEventListener.EventType.DEALT_DAMAGE_POST_EVENT, EVENT_UUID);
    }

    @Override
    public boolean isExecutableState(PlayerData<?> executer) {
        LivingData.EntityState playerState = executer.getEntityState();
        return !(executer.getOriginalEntity().isElytraFlying() || executer.currentMotion == LivingMotion.FALL || !playerState.canAct());
    }

    protected boolean isBlockableSource(DamageSource damageSource, boolean specialSourceBlockCondition) {
        return !damageSource.isUnblockable() && !damageSource.isProjectile() && !damageSource.isExplosion() && !damageSource.isMagicDamage() && !damageSource.isFireDamage();
    }

    /**@Override
    public void onReset(SkillContainer container) {
        PlayerData<?> executer = container.executer;

        if (!executer.isRemote()) {
            EntityPlayerMP executePlayer = (EntityPlayerMP) executer.getOriginalEntity();
            container.getDataManager().setDataSync(SHEATH, false, executePlayer);

            STCLivingMotionChange msg = new STCLivingMotionChange(executePlayer.getEntityId(), 3);
            msg.setMotions(LivingMotion.IDLE, LivingMotion.WALKING, LivingMotion.RUNNING);
            msg.setAnimations(Animations.BIPED_IDLE_UNSHEATHING, Animations.BIPED_WALK_UNSHEATHING, Animations.BIPED_RUN_UNSHEATHING);
            ((ServerPlayerData)executer).modifiLivingMotionToAll(msg);


        }
    }**/

    /**@SideOnly(Side.CLIENT)
    @Override
    public List<Object> getTooltipArgs() {
        List<Object> list = Lists.<Object>newArrayList();
        list.add(String.format("%s, %s, %s, %s, %s, %s, %s", AVAILABLE_WEAPON_TYPES.keySet().toArray(new Object[0])).toLowerCase());
        return list;
    }**/

    @SideOnly(Side.CLIENT)
    public boolean shouldDraw(SkillContainer container) {
        return container.getDataManager().getDataValue(PENALTY) > 0.0F;
    }
    @Nullable
    protected StaticAnimation getHitMotion(PlayerData<?> playerdata, CapabilityItem itemCapability, int meta) {
        return this.getAvailableWeaponTypes(meta).getOrDefault(itemCapability.getWeaponCategory(), (a, b) -> null).apply(itemCapability, playerdata);
    }

    /**@SideOnly(Side.CLIENT)
    @Override
    public void drawOnGui(BattleModeGui gui, SkillContainer container, MatrixStack matStackIn, float x, float y, float scale, int width, int height) {
        matStackIn.push();
        matStackIn.scale(scale, scale, 1.0F);
        matStackIn.translate(0, (float)gui.getSlidingProgression() * 1.0F / scale, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(Skills.GUARD.getSkillTexture());
        float scaleMultiply = 1.0F / scale;
        gui.drawTexturedModalRectFixCoord(matStackIn.getLast().getMatrix(), (width - x) * scaleMultiply, (height - y) * scaleMultiply, 0, 0, 255, 255);
        matStackIn.scale(scaleMultiply, scaleMultiply, 1.0F);
        gui.font.drawStringWithShadow(matStackIn, String.format("x%.1f", container.getDataManager().getDataValue(PENALTY)), ((float)width - x), ((float)height - y+6), 16777215);
    }**/

    protected boolean isHighTierSkill() {
        return false;
    }
}

