package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import maninthehouse.epicfight.gamedata.Sounds;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public interface ISkillfulParry {
    public boolean isParrying();
    public void setParrying(boolean parrying);
    public boolean isBlockableSource(DamageSource damageSource);




}
