package maninthehouse.epicfight.skill;

import java.util.function.Function;

import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.animation.types.attack.AttackAnimation;
import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninthehouse.epicfight.network.ModNetworkManager;
import maninthehouse.epicfight.network.server.STCResetBasicAttackCool;
import net.minecraft.network.PacketBuffer;

public class SelectiveAttackSkill extends SpecialAttackSkill
{
	protected final StaticAnimation[] attackAnimations;
	protected final Function<ServerPlayerData, Integer> selector;
	
	public SelectiveAttackSkill(SkillSlot index, float restriction, String skillName, float chargeIn, Function<ServerPlayerData, Integer> func, StaticAnimation... animations)
	{
		super(index, restriction, skillName, chargeIn, null);
		this.attackAnimations = animations;
		this.selector = func;
	}
	
	public SelectiveAttackSkill(SkillSlot index, float restriction, int duration, String skillName, float chargeIn, Function<ServerPlayerData, Integer> func, StaticAnimation... animations)
	{
		super(index, restriction, duration, skillName, chargeIn, null);
		this.attackAnimations = animations;
		this.selector = func;
	}
	
	@Override
	public SpecialAttackSkill registerPropertiesToAnimation() {
		for(StaticAnimation animation : this.attackAnimations) {
			((AttackAnimation)animation).addProperties(this.propertyMap.entrySet());
		}
		
		return this;
	}
	
	@Override
	public void executeOnServer(ServerPlayerData executer, PacketBuffer args)
	{
		executer.playAnimationSynchronize(this.attackAnimations[this.getAnimationInCondition(executer)], 0);
		ModNetworkManager.sendToPlayer(new STCResetBasicAttackCool(), executer.getOriginalEntity());
	}
	
	public int getAnimationInCondition(ServerPlayerData executer)
	{
		return selector.apply(executer);
	}
}