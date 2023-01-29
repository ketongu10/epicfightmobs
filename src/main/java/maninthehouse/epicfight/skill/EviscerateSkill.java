package maninthehouse.epicfight.skill;

import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.animation.types.attack.AttackAnimation;
import maninthehouse.epicfight.capabilities.entity.player.PlayerData;
import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninthehouse.epicfight.capabilities.item.CapabilityItem;
import maninthehouse.epicfight.entity.event.PlayerEventListener;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.network.ModNetworkManager;
import maninthehouse.epicfight.network.server.STCResetBasicAttackCool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.UUID;

public class EviscerateSkill extends SpecialAttackSkill {
    private static final UUID EVENT_UUID = UUID.fromString("f082557a-b2f9-11eb-8529-0242ac130003");
    private AttackAnimation first;
    private AttackAnimation second;

    public EviscerateSkill(SkillSlot index, float restriction, String skillName, float chargeIn) {
        super(index, restriction, skillName, chargeIn, null);
        this.first = (AttackAnimation) Animations.EVISCERATE_FIRST;
        this.second = (AttackAnimation)Animations.EVISCERATE_SECOND;
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.executer.getEventListener().addEventListener(PlayerEventListener.EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID, (event) -> {
            if (event.getAnimationId() == Animations.EVISCERATE_FIRST.getId()) {
                List<Entity> hitEnemies = event.getAttackedEntity();
                if (hitEnemies.size() > 0 && hitEnemies.get(0).isEntityAlive()) {
                    event.getPlayerData().reserverAnimationSynchronize(this.second);
                    event.getPlayerData().getServerAnimator().getPlayerFor(null).resetPlayer();
                    event.getPlayerData().currentlyAttackedEntity.clear();
                    this.second.onUpdate(event.getPlayerData());
                }
            }

            return false;
        });
    }

    @Override
    public void onDeleted(SkillContainer container) {
        container.executer.getEventListener().removeListener(PlayerEventListener.EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID);
    }

    @Override
    public void executeOnServer(ServerPlayerData executer, PacketBuffer args) {
        executer.playAnimationSynchronize(this.first, 0);
        ModNetworkManager.sendToPlayer(new STCResetBasicAttackCool(), executer.getOriginalEntity());
        //super.executeOnServer(executer, args);
    }



    @Override
    public SpecialAttackSkill registerPropertiesToAnimation() {
        first.addProperties(this.propertyMap.entrySet());
        second.addProperties(this.propertyMap.entrySet());

        return this;
    }
}