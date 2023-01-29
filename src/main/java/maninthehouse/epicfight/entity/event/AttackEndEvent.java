package maninthehouse.epicfight.entity.event;

import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

import java.util.List;

public class AttackEndEvent extends PlayerEvent<ServerPlayerData> {
    private List<Entity> attackedEntity;
    private int animationId;

    public AttackEndEvent(ServerPlayerData playerdata, List<Entity> attackedEntity, int animationId) {
        super(playerdata);
        this.attackedEntity = attackedEntity;
        this.animationId = animationId;
    }

    public List<Entity> getAttackedEntity() {
        return this.attackedEntity;
    }

    public int getAnimationId() {
        return this.animationId;
    }
}
