package maninthehouse.epicfight.entity.event;

import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;

public class ItemUseEndEvent extends PlayerEvent<ServerPlayerData> {
    public ItemUseEndEvent(ServerPlayerData playerdata) {
        super(playerdata);
    }
}
