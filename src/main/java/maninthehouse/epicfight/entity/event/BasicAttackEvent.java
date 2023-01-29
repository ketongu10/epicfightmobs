package maninthehouse.epicfight.entity.event;

import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;

public class BasicAttackEvent extends PlayerEvent<ServerPlayerData> {
    public BasicAttackEvent(ServerPlayerData playerdata) {
        super(playerdata);
    }
}
