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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.UUID;

public class LethalSlicingSkill extends SpecialAttackSkill {
    private static final UUID EVENT_UUID = UUID.fromString("bfa79c04-97a5-11eb-a8b3-0242ac130003");
    private AttackAnimation elbow;
    private AttackAnimation swing;
    private AttackAnimation doubleSwing;

    public LethalSlicingSkill(SkillSlot index, float restriction, String skillName, float chargeIn) {
        super( index,  restriction,   skillName, chargeIn, null);
        this.elbow = (AttackAnimation) Animations.LETHAL_SLICING;
        this.swing = (AttackAnimation)Animations.LETHAL_SLICING_ONCE;
        this.doubleSwing = (AttackAnimation)Animations.LETHAL_SLICING_TWICE;
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.executer.getEventListener().addEventListener(PlayerEventListener.EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID, (event) -> {
            if (event.getAnimationId() == Animations.LETHAL_SLICING.getId()) {
                List<Entity> hitEnemies = event.getAttackedEntity();
                if (hitEnemies.size() <= 1) {
                    event.getPlayerData().reserverAnimationSynchronize(this.swing);
                } else if (hitEnemies.size() > 1) {
                    event.getPlayerData().reserverAnimationSynchronize(this.doubleSwing);
                }
                //ModNetworkManager.sendToPlayer(new STCResetBasicAttackCool(), (EntityPlayerMP) container.executer.getOriginalEntity());
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
        executer.playAnimationSynchronize(this.elbow, 0.0F);
        ModNetworkManager.sendToPlayer(new STCResetBasicAttackCool(), executer.getOriginalEntity());
        //super.executeOnServer(executer, args);
    }

    @Override
    public List<ITextComponent> getTooltip() {
        List<ITextComponent> list = super.getTooltip();
        /**this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(0), "Elbow:");
        this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(1), "Each Strike:");**/
        return list;
    }

    @Override
    public SpecialAttackSkill registerPropertiesToAnimation() {

        this.elbow.addProperties(this.propertyMap.entrySet());
        this.swing.addProperties(this.propertyMap.entrySet());
        this.doubleSwing.addProperties(this.propertyMap.entrySet());


        return this;
    }


}
