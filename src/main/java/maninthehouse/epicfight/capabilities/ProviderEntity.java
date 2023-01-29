package maninthehouse.epicfight.capabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import chumbanotz.mutantbeasts.entity.EndersoulCloneEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantEndermanEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantSkeletonEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantSnowGolemEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantZombieEntity;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.cutievirus.creepingnether.entity.EntityPigman;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.windanesz.ancientspellcraft.entity.living.EntityClassWizard;
import com.windanesz.ancientspellcraft.entity.living.EntityEvilClassWizard;
import de.teamlapen.vampirism.entity.EntityGhost;
import de.teamlapen.vampirism.entity.hunter.EntityAdvancedHunter;
import de.teamlapen.vampirism.entity.hunter.EntityBasicHunter;
import de.teamlapen.vampirism.entity.hunter.EntityHunterTrainer;
import de.teamlapen.vampirism.entity.minions.vampire.EntityVampireMinionBase;
import de.teamlapen.vampirism.entity.minions.vampire.EntityVampireMinionSaveable;
import de.teamlapen.vampirism.entity.vampire.EntityAdvancedVampire;
import de.teamlapen.vampirism.entity.vampire.EntityBasicVampire;
import de.teamlapen.vampirism.entity.vampire.EntityVampireBaron;
import electroblob.wizardry.entity.living.*;
import maninthehouse.epicfight.capabilities.entity.CapabilityEntity;
import maninthehouse.epicfight.capabilities.entity.mob.*;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.*;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.BOSSES.*;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.custom.CustomKnightData;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.custom.CustomMobData;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.custom.CustomMonsterData;
import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninthehouse.epicfight.client.ClientEngine;
import maninthehouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninthehouse.epicfight.client.capabilites.entity.RemoteClientPlayerData;
import maninthehouse.epicfight.client.renderer.entity.CustomMobRenderer;
import maninthehouse.epicfight.client.renderer.entity.SimpleTexturBipedRenderer;
import maninthehouse.epicfight.config.ConfigurationCapability;
import maninthehouse.epicfight.config.EntityConfigurationCapability;
import maninthehouse.epicfight.main.EpicFightMod;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.shadowmage.ancientwarfare.npc.entity.*;
import net.shadowmage.ancientwarfare.npc.entity.faction.*;
import net.shadowmage.ancientwarfare.npc.entity.vehicle.NpcSiegeEngineer;
import net.shadowmage.ancientwarfare.vehicle.entity.VehicleBase;

import static maninthehouse.epicfight.main.LoaderConstants.*;

public class ProviderEntity implements ICapabilityProvider {
	private static final Map<Class<? extends Entity>, Supplier<CapabilityEntity<?>>> capabilityMap =
			new HashMap<Class<? extends Entity>, Supplier<CapabilityEntity<?>>> ();
	
	public static void makeMap() {
		capabilityMap.put(EntityPlayerMP.class, ServerPlayerData::new);
		capabilityMap.put(EntityZombie.class, ZombieData<EntityZombie>::new);
		capabilityMap.put(EntityCreeper.class, CreeperData::new);
		capabilityMap.put(EntityEnderman.class, EndermanData::new);
		capabilityMap.put(EntitySkeleton.class, SkeletonData<EntitySkeleton>::new);
		capabilityMap.put(EntityWitherSkeleton.class, WitherSkeletonData::new);
		capabilityMap.put(EntityStray.class, StrayData::new);
		capabilityMap.put(EntityPigZombie.class, ZombifiedPiglinData<EntityPigZombie>::new);
		capabilityMap.put(EntityZombieVillager.class, ZombieVillagerData::new);
		capabilityMap.put(EntityHusk.class, ZombieData<EntityHusk>::new);
		capabilityMap.put(EntitySpider.class, SpiderData::new);
		capabilityMap.put(EntityCaveSpider.class, CaveSpiderData::new);
		capabilityMap.put(EntityIronGolem.class, IronGolemData::new);
		capabilityMap.put(EntityVindicator.class, VindicatorData::new);
		capabilityMap.put(EntityEvoker.class, EvokerData::new);
		capabilityMap.put(EntityWitch.class, WitchData::new);
		capabilityMap.put(EntityVex.class, VexData::new);
		if (EBWIZARDRY) {
			capabilityMap.put(EntityZombieMinion.class, ZombieData<EntityZombieMinion>::new);
			capabilityMap.put(EntitySkeletonMinion.class, SkeletonData<EntitySkeletonMinion>::new);
			capabilityMap.put(EntityHuskMinion.class, ZombieData<EntityHuskMinion>::new);
			capabilityMap.put(EntityWitherSkeletonMinion.class, WitherSkeletonMinionData::new);
			capabilityMap.put(EntitySpectralGolem.class, IronGolemData<EntitySpectralGolem>::new);
			capabilityMap.put(EntityEvilWizard.class, EvilWizardData<EntityEvilWizard>::new);
			capabilityMap.put(EntityWizard.class, WizardData<EntityWizard>::new);
		}
		if (ANCIENT_SPELLCRAFT) {
			capabilityMap.put(EntityEvilClassWizard.class, EvilWizardData<EntityEvilClassWizard>::new);
			capabilityMap.put(EntityClassWizard.class, WizardData<EntityClassWizard>::new);
		}
		if (ANCIENT_WARFARE) {
			capabilityMap.put(NpcFactionSoldier.class, FactionNpcData<NpcFactionSoldier>::new);
			capabilityMap.put(NpcFactionSoldierElite.class, FactionNpcData<NpcFactionSoldierElite>::new);
			capabilityMap.put(NpcFactionMountedSoldier.class, FactionMountedData<NpcFactionMountedSoldier>::new);//1111111111111111111111111111
			capabilityMap.put(NpcFactionSpellcasterWizardry.class, FactionSpellcasterData::new);
			capabilityMap.put(NpcFactionArcherElite.class, FactionArcherEliteData::new);
			capabilityMap.put(NpcFactionArcher.class, FactionNpcData<NpcFactionArcher>::new);
			capabilityMap.put(NpcFactionMountedArcher.class, FactionNpcData<NpcFactionMountedArcher>::new);//1111111111111111111111111111
			capabilityMap.put(NpcFactionLeader.class, FactionLeadersData<NpcFactionLeader>::new);
			capabilityMap.put(NpcFactionLeaderElite.class, FactionLeadersData<NpcFactionLeaderElite>::new);
			capabilityMap.put(NpcFactionPriest.class, FactionNpcData<NpcFactionPriest>::new);
			capabilityMap.put(NpcFactionCivilianFemale.class, FactionNpcData<NpcFactionCivilianFemale>::new);
			capabilityMap.put(NpcFactionCivilianMale.class, FactionNpcData<NpcFactionCivilianMale>::new);
			capabilityMap.put(NpcFactionTrader.class, FactionNpcData<NpcFactionTrader>::new);
			capabilityMap.put(NpcFactionSiegeEngineer.class, FactionEngineerData::new);
			capabilityMap.put(NpcFactionBard.class, FactionNpcData<NpcFactionBard>::new);

			capabilityMap.put(NpcBard.class, PlayerNpcData<NpcBard>::new);
			capabilityMap.put(NpcPriest.class, PlayerNpcData<NpcPriest>::new);
			capabilityMap.put(NpcTrader.class, PlayerNpcData<NpcTrader>::new);
			capabilityMap.put(NpcCourier.class, PlayerNpcData<NpcCourier>::new);
			capabilityMap.put(NpcCombat.class, PlayerNpcData<NpcCombat>::new);
			capabilityMap.put(NpcWorker.class, PlayerNpcData<NpcWorker>::new);
			capabilityMap.put(NpcSiegeEngineer.class, PlayerEngineerData::new);
			capabilityMap.put(VehicleBase.class, AWVehicleData::new);

		}
		if (VAMPIRISM) {
			capabilityMap.put(EntityHunterTrainer.class, HunterData<EntityHunterTrainer>::new);
			capabilityMap.put(EntityAdvancedHunter.class, HunterData<EntityAdvancedHunter>::new);
			capabilityMap.put(EntityBasicHunter.class, HunterData<EntityBasicHunter>::new);
			capabilityMap.put(EntityGhost.class, GhostData::new);

			capabilityMap.put(EntityBasicVampire.class, VampireData<EntityBasicVampire>::new);
			capabilityMap.put(EntityAdvancedVampire.class, VampireData<EntityAdvancedVampire>::new);
			capabilityMap.put(EntityVampireBaron.class, VampireData<EntityVampireBaron>::new);
			capabilityMap.put(EntityVampireMinionSaveable.class, VampireData<EntityVampireMinionSaveable>::new);
		}
		if (CREEPING_NETHER) {
			capabilityMap.put(EntityPigman.class, ZombifiedPiglinData<EntityPigman>::new);
		}
		if (MUTANT_BEASTS) {
			capabilityMap.put(MutantSkeletonEntity.class, MutantSkeletonData::new);
			capabilityMap.put(MutantSnowGolemEntity.class, MutantSnowGolemData::new);
			capabilityMap.put(MutantZombieEntity.class, MutantZombieData::new);
			capabilityMap.put(MutantEndermanEntity.class, MutantEndermanData::new);
			capabilityMap.put(EndersoulCloneEntity.class, EndermanData<EndersoulCloneEntity>::new);
		}
		if (MOWZIES_MOBS) {
			capabilityMap.put(EntityWroughtnaut.class, WroughtnautData::new);
			capabilityMap.put(EntityFrostmaw.class, FrostMawData::new);

		}
		addCustomEntityCapabilities();


	}

	public static void addCustomEntityCapabilities() {
		for (EntityConfigurationCapability.EntityConfig config: EntityConfigurationCapability.getEntityConfigs()) {
			try {
				String entityName = config.registryName;
				Class clazz = EntityList.getClassFromName(entityName);
				for(int i=0;i<5;i++) {System.out.println();}
				if (clazz != null) {
					if (!capabilityMap.containsKey(clazz)) {
						switch (config.model) {
							case ENDERMAN:
								if (EntityMob.class.isAssignableFrom(clazz)) {capabilityMap.put(clazz, EndermanData::new);}break;
							case GOLEM:
								if (EntityCreature.class.isAssignableFrom(clazz)) {capabilityMap.put(clazz, IronGolemData::new);}break;
							case PIGLIN:
								if (EntityCreature.class.isAssignableFrom(clazz)) {capabilityMap.put(clazz, ZombifiedPiglinData::new);}break;
							case MONSTER:
								if (EntityLiving.class.isAssignableFrom(clazz)) {capabilityMap.put(clazz, () -> {CustomMonsterData cap = new CustomMonsterData(config.impact);return cap;});}break;
							case WIZARD:
								if (EBWIZARDRY && EntityCreature.class.isAssignableFrom(clazz) && ISpellCaster.class.isInstance(clazz)) {capabilityMap.put(clazz, () -> {WizardData cap = new WizardData(config.impact); return cap;});}break;
							default:
								if (EntityCreature.class.isAssignableFrom(clazz)) {capabilityMap.put(clazz, () -> {
									if (config.model== EntityConfigurationCapability.EntityType.KNIGHT) {
										CustomKnightData cap = new CustomKnightData(config.impact, config.stun_armor, config.parry, config.oldTexture);
										return cap;
									} else {
										CustomMobData cap = new CustomMobData(config.impact, config.stun_armor, config.model, config.oldTexture);
										return cap;
									}
								});}break;
						}
					}
				}
			} catch (Exception e) {
				EpicFightMod.LOGGER.warn("Failed to load custom entity " + config.registryName);
				System.err.println(e);
			}
		}
	}




	
	public static void makeMapClient() {
		capabilityMap.put(EntityOtherPlayerMP.class, RemoteClientPlayerData<EntityOtherPlayerMP>::new);
		capabilityMap.put(EntityPlayerSP.class, ClientPlayerData::new);
	}
	
	private CapabilityEntity<?> capability;
	
	public ProviderEntity(Entity entity) {
		if(capabilityMap.containsKey(entity.getClass())) {
			capability = capabilityMap.get(entity.getClass()).get();
		}
	}
	
	public boolean hasCapability() {
		return capability != null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == ModCapabilities.CAPABILITY_ENTITY && this.capability != null ? true : false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == ModCapabilities.CAPABILITY_ENTITY && this.capability != null) {
			return (T) this.capability;
		}
		return null;
	}
}