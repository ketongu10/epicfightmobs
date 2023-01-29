package maninthehouse.epicfight.client.events.engine;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.entity.EndersoulCloneEntity;
import com.cutievirus.creepingnether.CreepingNether;
import com.cutievirus.creepingnether.entity.EntityPigman;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.windanesz.ancientspellcraft.entity.living.EntityClassWizard;
import com.windanesz.ancientspellcraft.entity.living.EntityEvilClassWizard;
import com.windanesz.ancientspellcraft.entity.living.EntityEvilWizardAS;
import de.teamlapen.vampirism.VampirismMod;
import de.teamlapen.vampirism.entity.EntityGhost;
import de.teamlapen.vampirism.entity.hunter.EntityAdvancedHunter;
import de.teamlapen.vampirism.entity.hunter.EntityBasicHunter;
import de.teamlapen.vampirism.entity.hunter.EntityHunterTrainer;
import de.teamlapen.vampirism.entity.minions.vampire.EntityVampireMinionSaveable;
import de.teamlapen.vampirism.entity.vampire.EntityAdvancedVampire;
import de.teamlapen.vampirism.entity.vampire.EntityBasicVampire;
import de.teamlapen.vampirism.entity.vampire.EntityVampireBaron;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.*;
import maninthehouse.epicfight.capabilities.entity.mob.*;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.*;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.custom.CustomKnightData;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.custom.CustomMobData;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.custom.CustomMonsterData;
import maninthehouse.epicfight.client.renderer.entity.*;
import maninthehouse.epicfight.client.renderer.item.*;
import maninthehouse.epicfight.config.EntityConfigurationCapability;
import me.paulf.wings.server.item.WingsItems;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.Loader;
import net.shadowmage.ancientwarfare.npc.entity.*;
import net.shadowmage.ancientwarfare.npc.entity.faction.*;
import net.shadowmage.ancientwarfare.npc.entity.vehicle.NpcSiegeEngineer;
import net.shadowmage.ancientwarfare.npc.skin.NpcSkinManager;
import org.lwjgl.opengl.GL11;

import maninthehouse.epicfight.capabilities.ModCapabilities;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.capabilities.item.CapabilityItem;
import maninthehouse.epicfight.client.ClientEngine;
import maninthehouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninthehouse.epicfight.client.gui.BattleModeGui;
import maninthehouse.epicfight.client.gui.EntityIndicator;
import maninthehouse.epicfight.client.input.ModKeys;
import maninthehouse.epicfight.client.renderer.AimHelperRenderer;
import maninthehouse.epicfight.client.renderer.FirstPersonRenderer;
import maninthehouse.epicfight.config.ConfigurationIngame;
import maninthehouse.epicfight.item.KatanaItem;
import maninthehouse.epicfight.item.ModItems;
import maninthehouse.epicfight.main.EpicFightMod;
import maninthehouse.epicfight.utils.game.Formulars;
import maninthehouse.epicfight.utils.math.MathUtils;
import maninthehouse.epicfight.utils.math.Vec3f;
import maninthehouse.epicfight.utils.math.Vec4f;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static maninthehouse.epicfight.main.LoaderConstants.*;

@SideOnly(Side.CLIENT)
public class RenderEngine {
	private static final Vec3f AIMING_CORRECTION = new Vec3f(-1.5F, 0.0F, 1.25F);
	public static final ResourceLocation NULL_TEXTURE = new ResourceLocation(EpicFightMod.MODID, "textures/gui/null.png");
	public AimHelperRenderer aimHelper;
	public BattleModeGui guiSkillBar = new BattleModeGui();
	private Minecraft minecraft;
	private VisibleMatrix4f projectionMatrix;
	public Map<Class<? extends Entity>, ArmatureRenderer> entityRendererMap;
	private Map<Item, RenderItemBase> itemRendererMapByInstance;
	private Map<Class<? extends Item>, RenderItemBase> itemRendererMapByClass;
	private FirstPersonRenderer firstPersonRenderer;
	private boolean aiming;
	private int zoomOutTimer = 0;
	private int zoomCount;
	private int zoomMaxCount = 20;
	
	public RenderEngine() {
		Events.renderEngine = this;
		RenderItemBase.renderEngine = this;
		EntityIndicator.init();
		this.minecraft = Minecraft.getMinecraft();
		this.entityRendererMap = new HashMap<Class<? extends Entity>, ArmatureRenderer>();
		this.itemRendererMapByInstance = new HashMap<Item, RenderItemBase>();
		this.itemRendererMapByClass = new HashMap<Class<? extends Item>, RenderItemBase>();
		this.projectionMatrix = new VisibleMatrix4f();
		this.firstPersonRenderer = new FirstPersonRenderer();
	}
	
	public void buildRenderer() {
		entityRendererMap.put(EntityCreeper.class, new CreeperRenderer());
		entityRendererMap.put(EntityEnderman.class, new EndermanRenderer(new ResourceLocation("textures/entity/enderman/enderman.png")));
		entityRendererMap.put(EntityZombie.class, new SimpleTexturBipedRenderer<EntityZombie, ZombieData<EntityZombie>>("textures/entity/zombie/zombie.png"));
		entityRendererMap.put(EntityZombieVillager.class, new ZombieVillagerRenderer());
		entityRendererMap.put(EntityPigZombie.class, new SimpleTexturBipedRenderer<EntityPigZombie, ZombifiedPiglinData<EntityPigZombie>>(EpicFightMod.MODID + ":textures/entity/zombified_piglin.png"));
		entityRendererMap.put(EntityHusk.class, new SimpleTexturBipedRenderer<EntityHusk, ZombieData<EntityHusk>>("textures/entity/zombie/husk.png"));
		entityRendererMap.put(EntitySkeleton.class, new SimpleTexturBipedRenderer<EntitySkeleton, SkeletonData<EntitySkeleton>>("textures/entity/skeleton/skeleton.png"));
		entityRendererMap.put(EntityWitherSkeleton.class, new WitherSkeletonRenderer<EntityWitherSkeleton, WitherSkeletonData>("textures/entity/skeleton/wither_skeleton.png"));
		entityRendererMap.put(EntityStray.class, new SimpleTexturBipedRenderer<EntityStray, SkeletonData<EntityStray>>("textures/entity/skeleton/stray.png"));
		entityRendererMap.put(EntityPlayerSP.class, new PlayerRenderer());
		entityRendererMap.put(EntityOtherPlayerMP.class, new PlayerRenderer());
		entityRendererMap.put(EntitySpider.class, new SpiderRenderer());
		entityRendererMap.put(EntityCaveSpider.class, new CaveSpiderRenderer());
		entityRendererMap.put(EntityIronGolem.class, new GolemRenderer<EntityIronGolem, IronGolemData<EntityIronGolem>>("textures/entity/iron_golem.png"));
		entityRendererMap.put(EntityVindicator.class, new SimpleTexturBipedRenderer<EntityVindicator, VindicatorData>("textures/entity/illager/vindicator.png"));
		entityRendererMap.put(EntityEvoker.class, new SimpleTexturBipedRenderer<EntityEvoker, EvokerData>("textures/entity/illager/evoker.png"));
		entityRendererMap.put(EntityWitch.class, new SimpleTexturBipedRenderer<EntityWitch, WitchData>(EpicFightMod.MODID + ":textures/entity/witch.png"));
		entityRendererMap.put(EntityVex.class, new VexRenderer());
		/**AW2 Soldiers**/
		if (ANCIENT_WARFARE) {
			entityRendererMap.put(NpcFactionSoldier.class, new SimpleTexturBipedRenderer<NpcFactionSoldier, FactionNpcData<NpcFactionSoldier>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionSoldierElite.class, new SimpleTexturBipedRenderer<NpcFactionSoldierElite, FactionNpcData<NpcFactionSoldierElite>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionMountedSoldier.class, new SimpleTexturBipedRenderer<NpcFactionMountedSoldier, FactionMountedData<NpcFactionMountedSoldier>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionArcher.class, new SimpleTexturBipedRenderer<NpcFactionArcher, FactionNpcData<NpcFactionArcher>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionArcherElite.class, new SimpleTexturBipedRenderer<NpcFactionArcherElite, FactionArcherEliteData>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionMountedArcher.class, new SimpleTexturBipedRenderer<NpcFactionMountedArcher, FactionNpcData<NpcFactionMountedArcher>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionLeader.class, new SimpleTexturBipedRenderer<NpcFactionLeader, FactionNpcData<NpcFactionLeader>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionLeaderElite.class, new SimpleTexturBipedRenderer<NpcFactionLeaderElite, FactionNpcData<NpcFactionLeaderElite>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionPriest.class, new SimpleTexturBipedRenderer<NpcFactionPriest, FactionNpcData<NpcFactionPriest>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionSpellcasterWizardry.class, new SimpleTexturBipedRenderer<NpcFactionSpellcasterWizardry, FactionSpellcasterData>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionCivilianFemale.class, new SimpleTexturBipedRenderer<NpcFactionCivilianFemale, FactionNpcData<NpcFactionCivilianFemale>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionCivilianMale.class, new SimpleTexturBipedRenderer<NpcFactionCivilianMale, FactionNpcData<NpcFactionCivilianMale>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionTrader.class, new SimpleTexturBipedRenderer<NpcFactionTrader, FactionNpcData<NpcFactionTrader>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionSiegeEngineer.class, new SimpleTexturBipedRenderer<NpcFactionSiegeEngineer, FactionEngineerData>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcFactionBard.class, new SimpleTexturBipedRenderer<NpcFactionBard, FactionNpcData<NpcFactionBard>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));

			entityRendererMap.put(NpcCombat.class, new SimpleTexturBipedRenderer<NpcCombat, PlayerNpcData<NpcCombat>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcPriest.class, new SimpleTexturBipedRenderer<NpcPriest, PlayerNpcData<NpcPriest>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcWorker.class, new SimpleTexturBipedRenderer<NpcWorker, PlayerNpcData<NpcWorker>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcCourier.class, new SimpleTexturBipedRenderer<NpcCourier, PlayerNpcData<NpcCourier>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcTrader.class, new SimpleTexturBipedRenderer<NpcTrader, PlayerNpcData<NpcTrader>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcBard.class, new SimpleTexturBipedRenderer<NpcBard, PlayerNpcData<NpcBard>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			entityRendererMap.put(NpcSiegeEngineer.class, new SimpleTexturBipedRenderer<NpcSiegeEngineer, PlayerEngineerData>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));



		}
		/**EBWizardry mobs**/
		if (EBWIZARDRY) {
			entityRendererMap.put(EntityWitherSkeletonMinion.class, new WitherSkeletonRenderer<EntityWitherSkeletonMinion, WitherSkeletonMinionData>("textures/entity/skeleton/wither_skeleton.png"));
			entityRendererMap.put(EntityStrayMinion.class, new SimpleTexturBipedRenderer<EntityStrayMinion, SkeletonData<EntityStrayMinion>>("textures/entity/skeleton/stray.png"));
			entityRendererMap.put(EntityHuskMinion.class, new SimpleTexturBipedRenderer<EntityHuskMinion, ZombieData<EntityHuskMinion>>("textures/entity/zombie/husk.png"));
			entityRendererMap.put(EntityZombieMinion.class, new SimpleTexturBipedRenderer<EntityZombieMinion, ZombieData<EntityZombieMinion>>("textures/entity/zombie/zombie.png"));
			entityRendererMap.put(EntitySkeletonMinion.class, new SimpleTexturBipedRenderer<EntitySkeletonMinion, SkeletonData<EntitySkeletonMinion>>("textures/entity/skeleton/skeleton.png"));


			//entityRendererMap.put(EntityWizard.class, new SimpleTexturBipedRenderer<EntityWizard, WizardData<EntityWizard>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
			//entityRendererMap.put(EntityWizard.class, new WizardRenderer<EntityWizard, WizardData<EntityWizard>>(false));
			entityRendererMap.put(EntityWizard.class, new WizardRenderer<EntityWizard, WizardData<EntityWizard>>(false));
			entityRendererMap.put(EntityEvilWizard.class, new WizardRenderer<EntityEvilWizard, EvilWizardData<EntityEvilWizard>>(true));
			entityRendererMap.put(EntitySpectralGolem.class, new GolemRenderer<EntitySpectralGolem, IronGolemData<EntitySpectralGolem>>(Wizardry.MODID+ ":textures/entity/spectral_golem.png"));

			/**AS**/
			if (ANCIENT_SPELLCRAFT) {
				//entityRendererMap.put(EntityClassWizard.class, new SimpleTexturBipedRenderer<EntityClassWizard, WizardData<EntityClassWizard>>(EpicFightMod.MODID + ":textures/entity/npc_default.png"));
				entityRendererMap.put(EntityEvilClassWizard.class, new EntityClassWizardRenderer<EntityEvilClassWizard, EvilWizardData<EntityEvilClassWizard>>());
			}
			}
		/**Vampirism**/
		if (VAMPIRISM) {
			entityRendererMap.put(EntityHunterTrainer.class, new SimpleTexturBipedRenderer<EntityHunterTrainer, HunterData<EntityHunterTrainer>>(EpicFightMod.MODID+  ":textures/entity/vampirism/vampire_hunter_trainer.png"));
			entityRendererMap.put(EntityAdvancedHunter.class, new WitcherRenderer(EpicFightMod.MODID + ":textures/entity/vampirism/vampire_hunter_base2.png"));
			entityRendererMap.put(EntityBasicHunter.class, new SimpleTexturBipedRenderer<EntityBasicHunter, HunterData<EntityBasicHunter>>(EpicFightMod.MODID + ":textures/entity/vampirism/vampire_hunter_base3.png"));
			entityRendererMap.put(EntityBasicVampire.class, new VampireRenderer());
			entityRendererMap.put(EntityAdvancedVampire.class, new SimpleTexturBipedRenderer<EntityAdvancedVampire, VampireData<EntityAdvancedVampire>>("vampirism:textures/entity/vampire16.png"));
			entityRendererMap.put(EntityVampireBaron.class, new SimpleTexturBipedRenderer<EntityVampireBaron, VampireData<EntityVampireBaron>>("vampirism:textures/entity/vampire17.png"));
			entityRendererMap.put(EntityVampireMinionSaveable.class, new SimpleTexturBipedRenderer<EntityVampireMinionSaveable, VampireData<EntityVampireMinionSaveable>>(EpicFightMod.MODID+":textures/entity/vampirism/minion.png"));
			entityRendererMap.put(EntityGhost.class, new GhostRenderer<EntityGhost, GhostData>(EpicFightMod.MODID + ":textures/entity/ghost.png"));
		}
		/**Mutant Beasts**/
		if (MUTANT_BEASTS) {
			entityRendererMap.put(EndersoulCloneEntity.class, new EndermanRenderer(new ResourceLocation(MutantBeasts.MOD_ID +":textures/entity/endersoul.png")));
		}
		if (ICE_AND_FIRE) {

		}
		if (CREEPING_NETHER) {
			entityRendererMap.put(EntityPigman.class, new SimpleTexturBipedRenderer<EntityPigman, ZombifiedPiglinData<EntityPigman>>(EpicFightMod.MODID + ":textures/entity/piglin_brute.png"));
		}
		addCustomRenderers();
		RenderBow bowRenderer = new RenderBow();
		RenderElytra elytraRenderer = new RenderElytra();
		RenderHat hatRenderer = new RenderHat();
		RenderKatana katanaRenderer = new RenderKatana();
		RenderShield shieldRenderer = new RenderShield();
		if (VAMPIRISM) {
			RenderCape capeRenderer1 = new RenderCape(0, 32, new ResourceLocation(EpicFightMod.MODID + ":textures/entity/vampirism/vampire_hunter_trainer.png"));
			RenderCape capeRenderer2 = new RenderCape(34, 34, new ResourceLocation(EpicFightMod.MODID + ":textures/entity/vampirism/vampire_baron.png"));
			RenderWitcherBackpack witcher_backpack = new RenderWitcherBackpack();
			itemRendererMapByInstance.put(ModItems.WITCHER_BACKPACK, witcher_backpack);
			itemRendererMapByInstance.put(ModItems.CAPE, capeRenderer1);
			itemRendererMapByInstance.put(de.teamlapen.vampirism.core.ModItems.vampire_cloak, capeRenderer2);
		}
		
		itemRendererMapByInstance.put(Items.AIR, new RenderItemBase());
		itemRendererMapByInstance.put(Items.BOW, bowRenderer);
		itemRendererMapByInstance.put(Items.SHIELD, shieldRenderer);
		itemRendererMapByInstance.put(Items.ELYTRA, elytraRenderer);
		itemRendererMapByInstance.put(ModItems.KATANA, katanaRenderer);

		/**if (Loader.isModLoaded("wings")) {

			itemRendererMapByInstance.put(WingsItems.BAT_WINGS, new RenderWings());
		}**/
		
		itemRendererMapByClass.put(ItemBlock.class, hatRenderer);
		itemRendererMapByClass.put(ItemBow.class, bowRenderer);
		itemRendererMapByClass.put(ItemElytra.class, elytraRenderer);
		itemRendererMapByClass.put(ItemShield.class, shieldRenderer);
		itemRendererMapByClass.put(KatanaItem.class, katanaRenderer);
		if (ANCIENT_WARFARE) {
			itemRendererMapByClass.put(net.shadowmage.ancientwarfare.npc.item.ItemShield.class, shieldRenderer);
		}
		aimHelper = new AimHelperRenderer();
	}
	public void addCustomRenderers() {
		for (EntityConfigurationCapability.EntityConfig config: EntityConfigurationCapability.getEntityConfigs()) {
			try {
				String entityName = config.registryName;
				Class clazz = EntityList.getClassFromName(entityName);
				for(int i=0;i<5;i++) {System.out.println();}
				System.out.println("+++++++++++++Class, impact, model "+clazz+" "+config.impact+" "+config.model);
				if (clazz != null) {
					if (!entityRendererMap.containsKey(clazz)) {
						switch (config.model) {
							case MONSTER:
								if (EntityLiving.class.isAssignableFrom(clazz)) {}break;
							default:
								if (EntityCreature.class.isAssignableFrom(clazz)) {
									entityRendererMap.put(clazz, new CustomMobRenderer());
									}break;
						}
					}
				}
			} catch (Exception e) {
				EpicFightMod.LOGGER.warn("Failed to load custom entity renderer" + config.registryName);
				System.err.println(e);
			}
		}
	}
	
	public RenderItemBase getItemRenderer(Item item) {
		RenderItemBase renderItem = itemRendererMapByInstance.get(item);
		if (renderItem == null) {
			renderItem = this.findMatchingRendererByClass(item.getClass());
			if (renderItem == null) {
				renderItem = itemRendererMapByInstance.get(Items.AIR);
			}
			this.itemRendererMapByInstance.put(item, renderItem);
		}
		
		return renderItem;
	}

	private RenderItemBase findMatchingRendererByClass(Class<?> clazz) {
		RenderItemBase renderer = null;
		for (; clazz != null && renderer == null; clazz = clazz.getSuperclass())
			renderer = itemRendererMapByClass.getOrDefault(clazz, null);
		
		return renderer;
	}
	
	public void renderEntityArmatureModel(EntityLivingBase livingEntity, LivingData<?> entitydata, RenderLivingBase<?> renderer, double x, double y, double z, float partialTicks) {
		/**RenderLivingBase<?> b = renderer;
		renderer.getMainModel().render();**/
		this.entityRendererMap.get(livingEntity.getClass()).render(livingEntity, entitydata, renderer, x, y, z, partialTicks);
	}
	
	public boolean isEntityContained(Entity entity) {
		return this.entityRendererMap.containsKey(entity.getClass());
	}
	
	public void zoomIn() {
		aiming = true;
		zoomCount = zoomCount == 0 ? 1 : zoomCount;
		zoomOutTimer = 0;
	}

	public void zoomOut(int timer) {
		aiming = false;
		zoomOutTimer = timer;
	}
	
	private void updateCameraInfo(int pov, double partialTicks) {
		if(pov != 1) {
			return;
		}
		
		FloatBuffer fb1 = GLAllocation.createDirectFloatBuffer(16);
		GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, fb1);
		
		Entity entity = minecraft.getRenderViewEntity();
		double posX = MathUtils.lerp(partialTicks, entity.lastTickPosX, entity.posX);
		double posY = MathUtils.lerp(partialTicks, entity.lastTickPosY, entity.posY)  + entity.getEyeHeight();
		double posZ = MathUtils.lerp(partialTicks, entity.lastTickPosZ, entity.posZ);
        float intpol = pov == 1 ? ((float)zoomCount/(float)zoomMaxCount) : 0;
        Vec3f interpolatedCorrection = new Vec3f(AIMING_CORRECTION.x * intpol, AIMING_CORRECTION.y * intpol, AIMING_CORRECTION.z * intpol);
        VisibleMatrix4f rotationMatrix = ClientEngine.INSTANCE.getPlayerData().getMatrix((float)partialTicks);
        Vec4f rotateVec = VisibleMatrix4f.transform(rotationMatrix, new Vec4f(interpolatedCorrection.x, interpolatedCorrection.y, interpolatedCorrection.z - 4.0F, 1.0F), null);
        double d3 = Math.sqrt((rotateVec.x * rotateVec.x) + (rotateVec.y * rotateVec.y) + (rotateVec.z * rotateVec.z));
        double smallest = d3;
		double d00 = posX + rotateVec.x;
		double d11 = posY - rotateVec.y;
		double d22 = posZ + rotateVec.z;
		
		for (int i = 0; i < 8; ++i) {
            float f = (float)((i & 1) * 2 - 1);
            float f1 = (float)((i >> 1 & 1) * 2 - 1);
            float f2 = (float)((i >> 2 & 1) * 2 - 1);
            f = f * 0.1F;
            f1 = f1 * 0.1F;
            f2 = f2 * 0.1F;
            
            RayTraceResult raytraceresult = minecraft.world.rayTraceBlocks(new Vec3d(posX + f, posY + f1, posZ + f2),
            		new Vec3d(d00 + f + f2, d11 + f1, d22 + f2), false, true, false
            );
            
			if (raytraceresult != null) {
            	double d7 = raytraceresult.hitVec.distanceTo(new Vec3d(posX, posY, posZ));
                if (d7 < smallest) {
                	smallest = d7;
                }
            }
        }
        
		float dist = d3 == 0 ? 0 : (float)(smallest / d3);
        
        GlStateManager.translate(interpolatedCorrection.x * dist, -interpolatedCorrection.y * dist, interpolatedCorrection.z * dist);
        GlStateManager.translate(0.0F, 0.0F, -fb1.get(14));
        GlStateManager.translate(0.0F, 0.0F, (float)(-smallest));
        
		FloatBuffer fb2 = GLAllocation.createDirectFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, fb2);
		this.projectionMatrix.load(fb2.asReadOnlyBuffer());
	}

	public VisibleMatrix4f getCurrentProjectionMatrix() {
		return this.projectionMatrix;
	}

	@SideOnly(Side.CLIENT)
	@Mod.EventBusSubscriber(modid = EpicFightMod.MODID, value = Side.CLIENT)
	public static class Events {
		static RenderEngine renderEngine;
		@SubscribeEvent
		public static void renderLivingEvent(RenderLivingEvent.Pre<? extends EntityLivingBase> event) {
			EntityLivingBase livingentity = event.getEntity();
			if (renderEngine.isEntityContained(livingentity)) {
				if (livingentity instanceof EntityPlayerSP) {
					if (event.getPartialRenderTick() == 1.0F) {
						return;
					} else if (!ClientEngine.INSTANCE.isBattleMode() && ConfigurationIngame.filterAnimation) {
						return;
					}
				}
				if (ICE_AND_FIRE){
					if (event.getEntity().getRidingEntity() != null) {
						if (event.getEntity().getRidingEntity() instanceof EntityDragonBase) {
							return;
						}
					}
				}
				
				LivingData<?> entitydata = (LivingData<?>) livingentity.getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
				if (entitydata != null) {
					event.setCanceled(true);
					renderEngine.renderEntityArmatureModel(livingentity, entitydata, event.getRenderer(), event.getX(), event.getY(), event.getZ(), event.getPartialRenderTick());
				}
			}
			
			if (!Minecraft.getMinecraft().gameSettings.hideGUI) {
				for (EntityIndicator entityIndicator : EntityIndicator.ENTITY_INDICATOR_RENDERERS) {
					if (entityIndicator.shouldDraw(event.getEntity())) {
						entityIndicator.drawIndicator(event.getEntity(), event.getX(), event.getY(), event.getZ(), event.getPartialRenderTick());
					}
				}
			}
		}

		@SubscribeEvent
		public static void itemTooltip(ItemTooltipEvent event) {
			if (event.getEntityPlayer() != null) {
				CapabilityItem cap = ModCapabilities.stackCapabilityGetter(event.getItemStack());
				
				if (cap != null && ClientEngine.INSTANCE.getPlayerData() != null) {
					if (ClientEngine.INSTANCE.inputController.isKeyDown(ModKeys.SPECIAL_ATTACK_TOOLTIP)) {
						if (cap.getSpecialAttack(ClientEngine.INSTANCE.getPlayerData()) != null) {
							event.getToolTip().clear();
							
							for (ITextComponent s : cap.getSpecialAttack(ClientEngine.INSTANCE.getPlayerData()).getTooltip()) {
								event.getToolTip().add(s.getFormattedText());
							}
						}
					} else {
						List<String> tooltip = event.getToolTip();
						ClientPlayerData entityCap = (ClientPlayerData) event.getEntityPlayer().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
						cap.modifyItemTooltip(event.getToolTip(), entityCap);
						double weaponSpeed = entityCap.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue();
						
						for (AttributeModifier modifier : event.getItemStack().getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_SPEED.getName())) {
							weaponSpeed += modifier.getAmount();
						}
						
						double attackSpeedPanelty = Formulars.getAttackSpeedPenalty(entityCap.getWeight(), weaponSpeed, entityCap);
						tooltip.add(new TextComponentTranslation("item.tooltip.weight_penalty", ItemStack.DECIMALFORMAT.format(attackSpeedPanelty)).setStyle(new Style().setColor(TextFormatting.GRAY))
								.getFormattedText());
					}
				}
			}
		}
		
		@SubscribeEvent
		public static void cameraSetupEvent(CameraSetup event) {
			renderEngine.updateCameraInfo(Minecraft.getMinecraft().gameSettings.thirdPersonView, event.getRenderPartialTicks());
			if (renderEngine.zoomCount > 0) {
				if (renderEngine.zoomOutTimer > 0) {
					renderEngine.zoomOutTimer--;
				} else {
					renderEngine.zoomCount = renderEngine.aiming ? renderEngine.zoomCount + 1 : renderEngine.zoomCount - 1;
				}
				renderEngine.zoomCount = Math.min(renderEngine.zoomMaxCount, renderEngine.zoomCount);
			}
		}
		
		@SubscribeEvent
		public static void fogEvent(RenderFogEvent event) {
			/**
			 GlStateManager.fogMode(FogMode.LINEAR); GlStateManager.fogStart(0.0F);
			 GlStateManager.fogEnd(75.0F); GlStateManager.fogDensity(0.1F);
			 ***/
		}
		
		@SubscribeEvent
		public static void renderGameOverlay(RenderGameOverlayEvent.Pre event) {
			if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
				ClientPlayerData playerdata = ClientEngine.INSTANCE.getPlayerData();
				if (playerdata != null) {
					if (Minecraft.isGuiEnabled()) {
						renderEngine.guiSkillBar.renderGui(playerdata, event.getPartialTicks());
					}
				}
			}
		}
		
		@SubscribeEvent
		public static void renderHand(RenderSpecificHandEvent event) {
			if(ClientEngine.INSTANCE.isBattleMode()) {
				if(event.getHand() == EnumHand.MAIN_HAND) {
					renderEngine.firstPersonRenderer.render(Minecraft.getMinecraft().player, ClientEngine.INSTANCE.getPlayerData(), null, event.getPartialTicks());
				}
				
				event.setCanceled(true);
			}
		}
		
		@SubscribeEvent
		public static void renderWorldLast(RenderWorldLastEvent event) {
			if (renderEngine.zoomCount > 0 && renderEngine.minecraft.gameSettings.thirdPersonView == 1) {
				renderEngine.aimHelper.doRender(event.getPartialTicks());
			}
		}
	}
}