package maninthehouse.epicfight.entity.event;

import maninthehouse.epicfight.capabilities.entity.player.PlayerData;
import maninthehouse.epicfight.client.capabilites.entity.ClientPlayerData;
import net.minecraft.util.MovementInput;

import java.util.UUID;
import java.util.function.Function;

public class MovementInputEvent extends PlayerEvent<ClientPlayerData> {
    private MovementInput movementInput;

    public MovementInputEvent(ClientPlayerData playerdata, MovementInput movementInput) {
        super(playerdata);
    }

    public MovementInput getMovementInput() {
        return this.movementInput;
    }
}