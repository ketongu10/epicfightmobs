package maninthehouse.epicfight.item;

import maninthehouse.epicfight.client.model.compatibility.WitcherEquipment.ItemCape;
import maninthehouse.epicfight.client.model.compatibility.WitcherEquipment.ItemHair;
import maninthehouse.epicfight.client.model.compatibility.WitcherEquipment.ItemWitcherBackpack;
import maninthehouse.epicfight.client.model.compatibility.WizardEquipment.GhostRobe;
import maninthehouse.epicfight.client.model.compatibility.WizardEquipment.ItemBeard;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;

import static maninthehouse.epicfight.main.LoaderConstants.*;

public class ModItems {
	public static final Item KATANA = new KatanaItem().setUnlocalizedName("katana").setRegistryName("katana").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item KATANA_SHEATH = new KatanaSheathItem().setUnlocalizedName("katana_sheath").setRegistryName("katana_sheath");
	public static final Item GREATSWORD = new GreatswordItem().setUnlocalizedName("greatsword").setRegistryName("greatsword").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item STONE_SPEAR = new SpearItem(Item.ToolMaterial.STONE).setUnlocalizedName("stone_spear").setRegistryName("stone_spear").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item IRON_SPEAR = new SpearItem(Item.ToolMaterial.IRON).setUnlocalizedName("iron_spear").setRegistryName("iron_spear").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item GOLDEN_SPEAR = new SpearItem(Item.ToolMaterial.GOLD).setUnlocalizedName("golden_spear").setRegistryName("golden_spear").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item DIAMOND_SPEAR = new SpearItem(Item.ToolMaterial.DIAMOND).setUnlocalizedName("diamond_spear").setRegistryName("diamond_spear").setCreativeTab(EpicFightItemGroup.ITEMS);

	//public static final Item STONE_LONGSWORD = new LongswordItem(Item.ToolMaterial.STONE).setUnlocalizedName("stone_longsword").setRegistryName("stone_longsword").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item IRON_LONGSWORD = new LongswordItem(Item.ToolMaterial.IRON).setUnlocalizedName("iron_longsword").setRegistryName("iron_longsword").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item GOLDEN_LONGSWORD = new LongswordItem(Item.ToolMaterial.GOLD).setUnlocalizedName("golden_longsword").setRegistryName("golden_longsword").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item DIAMOND_LONGSWORD = new LongswordItem(Item.ToolMaterial.DIAMOND).setUnlocalizedName("diamond_longsword").setRegistryName("diamond_longsword").setCreativeTab(EpicFightItemGroup.ITEMS);

	public static final Item IRON_DAGGER = new DaggerItem(Item.ToolMaterial.IRON).setUnlocalizedName("iron_dagger").setRegistryName("iron_dagger").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item GOLDEN_DAGGER = new DaggerItem(Item.ToolMaterial.GOLD).setUnlocalizedName("golden_dagger").setRegistryName("golden_dagger").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item DIAMOND_DAGGER = new DaggerItem(Item.ToolMaterial.DIAMOND).setUnlocalizedName("diamond_dagger").setRegistryName("diamond_dagger").setCreativeTab(EpicFightItemGroup.ITEMS);

	public static final Item KNUCKLE = new KnuckleItem().setUnlocalizedName("knuckle").setRegistryName("knuckle").setCreativeTab(EpicFightItemGroup.ITEMS);

	public static final Item STRAY_HAT = new ItemArmor(ModMaterials.STRAY_CLOTH, 0, EntityEquipmentSlot.HEAD)
			.setUnlocalizedName("stray_hat").setRegistryName("stray_hat").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item STRAY_ROBE = new ItemArmor(ModMaterials.STRAY_CLOTH, 0, EntityEquipmentSlot.CHEST)
			.setUnlocalizedName("stray_robe").setRegistryName("stray_robe").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item STRAY_PANTS = new ItemArmor(ModMaterials.STRAY_CLOTH, 0, EntityEquipmentSlot.LEGS)
			.setUnlocalizedName("stray_pants").setRegistryName("stray_pants").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item SKILLBOOK = new SkillBookItem().setCreativeTab(EpicFightItemGroup.ITEMS);



	/**BY KETONGU10**/
	public static final Item WITCHER_BACKPACK = new ItemWitcherBackpack().setUnlocalizedName("witcher_backpack").setRegistryName("witcher_backpack");
	public static final Item HAIR = new ItemHair(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("hair_item").setRegistryName("hair_item").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item WIZARD_BEARD = new ItemBeard(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD, 0).setUnlocalizedName("wizard_beard").setRegistryName("wizard_beard").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item GHOST_HOOD = new GhostRobe(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("ghost_hood").setRegistryName("ghost_hood").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item GHOST_ROBE = new GhostRobe(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("ghost_robe").setRegistryName("ghost_robe").setCreativeTab(EpicFightItemGroup.ITEMS);
	public static final Item CAPE = new ItemCape().setUnlocalizedName("cape").setRegistryName("cape");
	
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(ModItems.KATANA);
		event.getRegistry().register(ModItems.KATANA_SHEATH);
		event.getRegistry().register(ModItems.GREATSWORD);
		event.getRegistry().register(ModItems.STONE_SPEAR);
		event.getRegistry().register(ModItems.IRON_SPEAR);
		event.getRegistry().register(ModItems.GOLDEN_SPEAR);
		event.getRegistry().register(ModItems.DIAMOND_SPEAR);

		event.getRegistry().register(ModItems.IRON_LONGSWORD);
		event.getRegistry().register(ModItems.GOLDEN_LONGSWORD);
		event.getRegistry().register(ModItems.DIAMOND_LONGSWORD);

		event.getRegistry().register(ModItems.IRON_DAGGER);
		event.getRegistry().register(ModItems.GOLDEN_DAGGER);
		event.getRegistry().register(ModItems.DIAMOND_DAGGER);
		event.getRegistry().register(ModItems.KNUCKLE);

		event.getRegistry().register(ModItems.STRAY_HAT);
		event.getRegistry().register(ModItems.STRAY_ROBE);
		event.getRegistry().register(ModItems.STRAY_PANTS);
		event.getRegistry().register(ModItems.SKILLBOOK);

		/**BY KETONGU10**/
		if (VAMPIRISM) {
			event.getRegistry().register(ModItems.CAPE);
			event.getRegistry().register(ModItems.GHOST_ROBE);
			event.getRegistry().register(ModItems.GHOST_HOOD);
			event.getRegistry().register(ModItems.WITCHER_BACKPACK);
			event.getRegistry().register(ModItems.HAIR);
		}
		if (EBWIZARDRY) {
			event.getRegistry().register(ModItems.WIZARD_BEARD);
		}
	}
}