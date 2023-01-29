package maninthehouse.epicfight.events;


import maninthehouse.epicfight.capabilities.ModCapabilities;
import maninthehouse.epicfight.capabilities.entity.CapabilityEntity;
import maninthehouse.epicfight.capabilities.entity.player.PlayerData;
import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninthehouse.epicfight.capabilities.item.AxeCapability;
import maninthehouse.epicfight.capabilities.item.CapabilityItem;
import maninthehouse.epicfight.capabilities.item.ModWeaponCapability;
import maninthehouse.epicfight.capabilities.item.SwordCapability;
import maninthehouse.epicfight.client.ClientEngine;
import maninthehouse.epicfight.entity.event.ItemUseEndEvent;
import maninthehouse.epicfight.entity.event.PlayerEventListener;
import maninthehouse.epicfight.entity.event.RightClickItemEvent;
import maninthehouse.epicfight.gamedata.Colliders;
import maninthehouse.epicfight.item.WeaponItem;
import maninthehouse.epicfight.main.EpicFightMod;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid=EpicFightMod.MODID)
public class PlayerEvents {
	@SubscribeEvent
	public static void arrowLooseEvent(ArrowLooseEvent event) {
		Colliders.update();
	}


	@SubscribeEvent
	public static void rightClickItemServer(PlayerInteractEvent.RightClickItem event) {
		if (event.getSide() == Side.SERVER) {
			ServerPlayerData playerdata = (ServerPlayerData) event.getEntityPlayer().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
			CapabilityItem cap = playerdata.getHeldItemCapability(EnumHand.MAIN_HAND);
			if (playerdata != null && (playerdata.getOriginalEntity().getHeldItemOffhand().getItemUseAction() == EnumAction.NONE || (cap != null && cap.isTwoHanded()))) {
				playerdata.getEventListener().activateEvents(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, new RightClickItemEvent<>(playerdata));

			}
		}

	}

	@SubscribeEvent
	public static void itemUseStartEvent(LivingEntityUseItemEvent.Start event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getEntity();
			PlayerData<?> playerdata = (PlayerData<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
			CapabilityItem itemCap = playerdata.getHeldItemCapability(EnumHand.MAIN_HAND);

			if (playerdata.isInaction()) {
				event.setCanceled(true);
			} else if (event.getItem() == player.getHeldItemOffhand() && itemCap != null && itemCap.isTwoHanded()) {
				event.setCanceled(true);
			}
			CapabilityItem cap = event.getItem().getCapability(ModCapabilities.CAPABILITY_ITEM, null);
			if (event.getItem().getItemUseAction() == EnumAction.BLOCK || cap instanceof SwordCapability || cap instanceof ModWeaponCapability || cap instanceof AxeCapability) {
				event.setDuration(72000);
			}
		}
	}
	
	@SubscribeEvent
	public static void itemUseStopEvent(LivingEntityUseItemEvent.Stop event) {
		if (event.getEntity().world.isRemote) {
			if (event.getEntity() instanceof EntityPlayerSP) {
				ClientEngine.INSTANCE.renderEngine.zoomOut(0);
			}
		} else {
			if (event.getEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getEntity();
				ServerPlayerData playerdata = (ServerPlayerData) player.getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
				if (playerdata != null) {
					playerdata.getEventListener().activateEvents(PlayerEventListener.EventType.SERVER_ITEM_STOP_EVENT, new ItemUseEndEvent(playerdata));
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void itemUseTickEvent(LivingEntityUseItemEvent.Tick event) {
		if (event.getEntity() instanceof EntityPlayer) {
			if (event.getItem().getItem() instanceof ItemBow) {
				PlayerData<?> playerdata = (PlayerData<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);

				if (playerdata.isInaction()) {
					event.setCanceled(true);
				}
			}
		}

	}
	
	@SubscribeEvent
	public static void playerTickPost(PlayerTickEvent event) {
		if (event.phase == Phase.END) {
			PlayerData<?> playerdata = (PlayerData<?>) event.player.getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
			if (playerdata != null) {
				playerdata.resetSize();
			}
		}
	}
	
	@SubscribeEvent
	public static void attackEntityEvent(AttackEntityEvent event) {
		if (!event.getEntity().world.getGameRules().getBoolean("doVanillaAttack")) {
			event.setCanceled(true);
		}
	}

	/**@SubscribeEvent
	public static void castEvent(SpellCastEvent.Pre event) {//ketongu10
		CapabilityEntity entitydata = event.getCaster().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
		if (event.getCaster() instanceof EntityPlayer && entitydata != null) {
			entitydata.onEntityCast(event.getCaster());
		}

	}**/
}