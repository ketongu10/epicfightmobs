package maninthehouse.epicfight.entity.event;

import maninthehouse.epicfight.capabilities.entity.player.PlayerData;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import net.minecraft.entity.EntityLiving;

public class DealtDamageEvent<T extends PlayerData<?>> extends PlayerEvent<T> {
    private float attackDamage;
    private EntityLiving target;
    private IExtendedDamageSource damageSource;

    public DealtDamageEvent(T playerdata, EntityLiving target, IExtendedDamageSource source, float damage) {
        super(playerdata);
        this.target = target;
        this.damageSource = source;
        this.attackDamage = damage;
    }

    public EntityLiving getTarget() {
        return this.target;
    }

    public IExtendedDamageSource getDamageSource() {
        return this.damageSource;
    }

    public void setAttackDamage(float damage) {
        this.attackDamage = damage;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }
}
