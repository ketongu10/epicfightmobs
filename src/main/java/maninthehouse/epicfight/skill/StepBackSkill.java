package maninthehouse.epicfight.skill;

import io.netty.buffer.Unpooled;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninthehouse.epicfight.client.events.engine.ControllEngine;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.PacketBuffer;

public class StepBackSkill extends DodgeSkill{

    public StepBackSkill(SkillSlot index, float cooldown, int maxStack, String skillName, float chargeIn, StaticAnimation... animation) {
        super(index, cooldown, maxStack, skillName, chargeIn, animation);

    }
    @Override
    public PacketBuffer gatherArguments(ClientPlayerData executer, ControllEngine controllEngine) {
        GameSettings gamesetting = controllEngine.gameSettings;

        int forward =  0;
        int backward = -1;
        int left = gamesetting.keyBindLeft.isKeyDown() ? 1 : 0;
        int right = gamesetting.keyBindRight.isKeyDown() ? -1 : 0;

        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());

        buf.writeInt(forward);
        buf.writeInt(backward);
        buf.writeInt(left);
        buf.writeInt(right);

        return buf;
    }
}
