package maninthehouse.epicfight.entity.event;

import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;

public class ActionEvent extends PlayerEvent<ServerPlayerData> {
    private StaticAnimation actionAnimation;

    public ActionEvent(ServerPlayerData playerdata, StaticAnimation actionAnimation) {
        super(playerdata);
        this.actionAnimation = actionAnimation;
    }

    public StaticAnimation getAnimation() {
        return this.actionAnimation;
    }
}
