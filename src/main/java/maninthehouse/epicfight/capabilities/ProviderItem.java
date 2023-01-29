package maninthehouse.epicfight.capabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.items.VampirismItem;
import de.teamlapen.vampirism.items.VampirismItemWeapon;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.registry.WizardryItems;
import maninthehouse.epicfight.capabilities.item.*;
import maninthehouse.epicfight.capabilities.item.CapabilityItem.WieldStyle;
import maninthehouse.epicfight.config.ConfigurationCapability;
import maninthehouse.epicfight.config.ConfigurationCapability.ArmorConfig;
import maninthehouse.epicfight.config.ConfigurationCapability.WeaponConfig;
import maninthehouse.epicfight.main.EpicFightMod;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import static maninthehouse.epicfight.main.LoaderConstants.EBWIZARDRY;
import static maninthehouse.epicfight.main.LoaderConstants.VAMPIRISM;

public class ProviderItem implements ICapabilityProvider {
	private static final Map<Item, CapabilityItem> CAPABILITY_BY_INSTANCE = new HashMap<Item, CapabilityItem> ();
	private static final Map<Class<? extends Item>, Function<Item, CapabilityItem>> CAPABILITY_BY_CLASS = new HashMap<Class<? extends Item>, Function<Item, CapabilityItem>> ();
	
	public static void makeMap() {
		addCustomItemCapabilities();
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.WOODEN_AXE, AxeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.STONE_AXE, AxeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.IRON_AXE, AxeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.GOLDEN_AXE, AxeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.DIAMOND_AXE, AxeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.WOODEN_PICKAXE, PickaxeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.STONE_PICKAXE, PickaxeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.IRON_PICKAXE, PickaxeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.GOLDEN_PICKAXE, PickaxeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.DIAMOND_PICKAXE, PickaxeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.WOODEN_HOE, HoeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.STONE_HOE, HoeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.IRON_HOE, HoeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.GOLDEN_HOE, HoeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.DIAMOND_HOE, HoeCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.WOODEN_SHOVEL, ShovelCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.STONE_SHOVEL, ShovelCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.IRON_SHOVEL, ShovelCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.GOLDEN_SHOVEL, ShovelCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.DIAMOND_SHOVEL, ShovelCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.WOODEN_SWORD, SwordCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.STONE_SWORD, SwordCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.IRON_SWORD, SwordCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.GOLDEN_SWORD, SwordCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.DIAMOND_SWORD, SwordCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.LEATHER_BOOTS, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.LEATHER_CHESTPLATE, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.LEATHER_HELMET, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.LEATHER_LEGGINGS, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.GOLDEN_BOOTS, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.GOLDEN_CHESTPLATE, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.GOLDEN_HELMET, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.GOLDEN_LEGGINGS, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.CHAINMAIL_BOOTS, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.CHAINMAIL_CHESTPLATE, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.CHAINMAIL_HELMET, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.CHAINMAIL_LEGGINGS, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.IRON_BOOTS, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.IRON_CHESTPLATE, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.IRON_HELMET, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.IRON_LEGGINGS, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.DIAMOND_BOOTS, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.DIAMOND_CHESTPLATE, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.DIAMOND_HELMET, VanillaArmorCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.DIAMOND_LEGGINGS, VanillaArmorCapability::new);

		CAPABILITY_BY_INSTANCE.computeIfAbsent(Items.BOW, BowCapability::new);

		/**Wizardry**/
		if (EBWIZARDRY) {
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.master_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.master_earth_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.master_fire_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.master_healing_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.master_ice_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.master_lightning_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.master_necromancy_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.master_sorcery_wand, MagicStaffCapability::new);

		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.advanced_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.advanced_ice_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.advanced_earth_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.advanced_fire_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.advanced_healing_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.advanced_lightning_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.advanced_necromancy_wand, MagicStaffCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.advanced_sorcery_wand, MagicStaffCapability::new);

		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.apprentice_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.apprentice_earth_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.apprentice_fire_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.apprentice_healing_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.apprentice_ice_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.apprentice_lightning_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.apprentice_necromancy_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.apprentice_sorcery_wand, MagicWandCapability::new);

		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.magic_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.novice_earth_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.novice_fire_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.novice_healing_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.novice_ice_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.novice_lightning_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.novice_necromancy_wand, MagicWandCapability::new);
		CAPABILITY_BY_INSTANCE.computeIfAbsent(WizardryItems.novice_sorcery_wand, MagicWandCapability::new);
		}
		/**Vampirism**/
		if (VAMPIRISM) {
			CAPABILITY_BY_INSTANCE.computeIfAbsent(ModItems.basic_crossbow, CrossbowCapability::new);
		}



		CAPABILITY_BY_CLASS.put(ItemSword.class, SwordCapability::new);
		CAPABILITY_BY_CLASS.put(ItemPickaxe.class, PickaxeCapability::new);
		CAPABILITY_BY_CLASS.put(ItemAxe.class, AxeCapability::new);
		CAPABILITY_BY_CLASS.put(ItemSpade.class, ShovelCapability::new);
		CAPABILITY_BY_CLASS.put(ItemHoe.class, HoeCapability::new);
		CAPABILITY_BY_CLASS.put(ItemBow.class, BowCapability::new);
		CAPABILITY_BY_CLASS.put(ItemArmor.class, ArmorCapability::new);



	}
	
	public static void addInstance(Item item, CapabilityItem cap) {
		CAPABILITY_BY_INSTANCE.put(item, cap);
	}
	
	public static void addCustomItemCapabilities() {
		for (WeaponConfig config : ConfigurationCapability.getWeaponConfigs()) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(config.registryName));
			if (item != null) {
				EpicFightMod.LOGGER.info("Register Custom Capaiblity for " + config.registryName);
				ModWeaponCapability cap = config.weaponType.get();
				cap.addStyleAttributeSimple(WieldStyle.ONE_HAND, config.onehand.armorNegation, config.onehand.impact, config.onehand.maxStrikes);
				cap.addStyleAttributeSimple(WieldStyle.TWO_HAND, config.twohand.armorNegation, config.twohand.impact, config.twohand.maxStrikes);
				CAPABILITY_BY_INSTANCE.put(item, cap);
			} else {
				EpicFightMod.LOGGER.warn("Failed to load custom item " + config.registryName + ". Item not exist!");
			}
		}
		
		for (ArmorConfig config : ConfigurationCapability.getArmorConfigs()) {
			try {
				Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(config.registryName));
				if (item != null && item instanceof ItemArmor) {
					ArmorCapability cap = new ArmorCapability(item, config.weight, config.stunArmor);
					CAPABILITY_BY_INSTANCE.put(item, cap);
					EpicFightMod.LOGGER.info("Register Custom Capaiblity for " + config.registryName);
				} else {
					if (item == null) {
						EpicFightMod.LOGGER.warn("Failed to load custom item " + config.registryName + ". Item not exist!");
					} else if (!(item instanceof ItemArmor)) {
						EpicFightMod.LOGGER.warn("Failed to load custom item " + config.registryName + ". Item is not armor!");
					}
				}
			} catch (Exception e) {
				EpicFightMod.LOGGER.warn("Failed to load custom item " + config.registryName);
				System.err.println(e);
			}
		}
	}
	
	private CapabilityItem capability;
	
	public ProviderItem(Item item, boolean autogenerate) {
		capability = CAPABILITY_BY_INSTANCE.get(item);
		if (capability == null && autogenerate) {
			capability = this.makeCustomCapability(item);
			if(capability != null) {
				CAPABILITY_BY_INSTANCE.put(item, capability);
			}
		}
	}
	
	private CapabilityItem makeCustomCapability(Item item) {
		Class<?> clazz = item.getClass();
		CapabilityItem cap = null;
		for(; clazz != null && cap == null; clazz = clazz.getSuperclass()) {
			cap = CAPABILITY_BY_CLASS.getOrDefault(clazz, (argIn) -> null).apply(item);
		}
		
		return cap;
	}
	
	public boolean hasCapability() {
		return this.capability != null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == ModCapabilities.CAPABILITY_ITEM && this.capability != null ? true : false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == ModCapabilities.CAPABILITY_ITEM && this.capability != null) {
			return (T) this.capability;
		}
		return null;
	}
}