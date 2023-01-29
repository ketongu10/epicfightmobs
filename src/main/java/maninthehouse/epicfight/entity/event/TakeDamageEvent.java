package maninthehouse.epicfight.entity.event;

import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class TakeDamageEvent extends PlayerEvent<ServerPlayerData> {
    private final LivingDamageEvent forgeEvent;

    public TakeDamageEvent(ServerPlayerData playerdata, LivingDamageEvent forgeEvent) {
        super(playerdata);
        this.forgeEvent = forgeEvent;
    }

    public LivingDamageEvent getForgeEvent() {
        return this.forgeEvent;
    }
}