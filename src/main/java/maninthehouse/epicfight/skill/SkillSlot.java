package maninthehouse.epicfight.skill;

public enum SkillSlot
{
	DODGE(0), WEAPON_GIMMICK(1), WEAPON_SPECIAL_ATTACK(2),  AIR_ATTACK(3),WEAPON_GUARD(4), STEP_BACK(5);
	
	int index;
	
	SkillSlot(int index)
	{
		this.index = index;
	}
	
	public int getIndex()
	{
		return this.index;
	}
}