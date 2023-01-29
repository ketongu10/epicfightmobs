package maninthehouse.epicfight.skill;

import java.util.UUID;

import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.capabilities.entity.player.PlayerData;
import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninthehouse.epicfight.entity.event.PlayerEvent;
import maninthehouse.epicfight.entity.event.PlayerEventListener;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.network.ModNetworkManager;
import maninthehouse.epicfight.network.server.STCLivingMotionChange;
import maninthehouse.epicfight.network.server.STCModifySkillVariable;
import maninthehouse.epicfight.network.server.STCPlayAnimation;
import net.minecraft.entity.player.EntityPlayerMP;

public class KatanaPassive extends Skill {
	public static final SkillDataManager.SkillDataKey<Boolean> SHEATH = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final UUID EVENT_UUID = UUID.fromString("a416c93a-42cb-11eb-b378-0242ac130002");

	public KatanaPassive() {
		super(SkillSlot.WEAPON_GIMMICK, 5.0F, "katana_passive");
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(SHEATH);
		container.executer.getEventListener().addEventListener(PlayerEventListener.EventType.ACTION_EVENT, EVENT_UUID, (event) -> {
			container.getContaining().setCooldownSynchronize(event.getPlayerData(), 0.0F);
			//container.getContaining().setStackSynchronize(event.getPlayerData(), 0);
			return false;
		});
		container.executer.getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
			this.onReset(container);
			return false;
		});
	}
	
	@Override
	public void onDeleted(SkillContainer container) {
		container.executer.getEventListener().removeListener(PlayerEventListener.EventType.ACTION_EVENT, EVENT_UUID);
		container.executer.getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
	}
	
	@Override
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
	}
	
	@Override
	public void setCooldown(SkillContainer container, float value) {
		PlayerData<?> executer = container.executer;

		if (!executer.isRemote()) {
			if (this.cooldown < value) {
				EntityPlayerMP executePlayer = (EntityPlayerMP) executer.getOriginalEntity();
				container.getDataManager().setDataSync(SHEATH, true, executePlayer);
				
				STCLivingMotionChange msg = new STCLivingMotionChange(executePlayer.getEntityId(), 6);
				msg.setMotions(LivingMotion.IDLE, LivingMotion.WALKING, LivingMotion.RUNNING, LivingMotion.JUMPING, LivingMotion.KNEELING, LivingMotion.SNEAKING);
				msg.setAnimations(Animations.BIPED_IDLE_SHEATHING, Animations.BIPED_WALK_SHEATHING, Animations.BIPED_RUN_SHEATHING,
						Animations.BIPED_JUMP_SHEATHING, Animations.BIPED_KNEEL_SHEATHING, Animations.BIPED_SNEAK_SHEATHING);
				((ServerPlayerData)executer).modifiLivingMotionToAll(msg);


				
				STCPlayAnimation msg3 = new STCPlayAnimation(Animations.BIPED_KATANA_SCRAP.getId(), executePlayer.getEntityId(), 0.0F, true);
				ModNetworkManager.sendToAllPlayerTrackingThisEntityWithSelf(msg3, executePlayer);
			}
		}
		
		super.setCooldown(container, value);
	}
	
	@Override
	public float getRegenTimePerTick(PlayerData<?> player) {
		return 1.0F;
	}
}