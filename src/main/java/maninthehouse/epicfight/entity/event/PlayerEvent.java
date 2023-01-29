package maninthehouse.epicfight.entity.event;

import java.util.UUID;
import java.util.function.Function;

import maninthehouse.epicfight.capabilities.entity.player.PlayerData;

public class PlayerEvent<T extends PlayerData<?>> {
	private T playerdata;

	public PlayerEvent(T playerdata) {
		this.playerdata = playerdata;
	}

	public T getPlayerData() {
		return this.playerdata;
	}
}