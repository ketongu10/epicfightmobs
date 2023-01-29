package maninthehouse.epicfight.capabilities.item;

import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.item.ItemWand;
import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.ModCapabilities;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.capabilities.entity.player.PlayerData;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Colliders;
import maninthehouse.epicfight.gamedata.Skills;
import maninthehouse.epicfight.gamedata.Sounds;
import maninthehouse.epicfight.physics.Collider;
import maninthehouse.epicfight.skill.Skill;
import maninthehouse.epicfight.utils.game.Pair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagicWandCapability extends CapabilityItem{
    private static List<StaticAnimation> AttackMotion;
    private static List<StaticAnimation> magicAttackMotion;
    private static StaticAnimation summonMotion;
    private static StaticAnimation longMagicMotion;
    protected Map<LivingMotion, StaticAnimation> magicAnimationSet;
    protected static List<StaticAnimation> mountAttackMotion;


    public MagicWandCapability(Item item) {
        super(item, WeaponCategory.NONE_WEAPON);

        this.magicAnimationSet = new HashMap<LivingMotion, StaticAnimation>();
        this.magicAnimationSet.put(LivingMotion.SPELLCASTING, Animations.MAGIC_DASH_WAND);

        AttackMotion = new ArrayList<StaticAnimation>();
        AttackMotion.add(Animations.FIST_AUTO_1);
        AttackMotion.add(Animations.FIST_AUTO_2);
        AttackMotion.add(Animations.FIST_AUTO_1);
        AttackMotion.add(Animations.FIST_DASH);

        mountAttackMotion = new ArrayList<StaticAnimation> ();
        mountAttackMotion.add(Animations.TOOL_AUTO_2);
    }

    @Override
    public List<StaticAnimation> getMagicMotion(PlayerData<?> playerdata) {
        List<StaticAnimation> magicAttackMotion = new ArrayList<StaticAnimation>();
        magicAttackMotion.add(Animations.KATANA_AUTO_1);
        magicAttackMotion.add(Animations.KATANA_AUTO_2);
        magicAttackMotion.add(Animations.KATANA_AUTO_3);
        magicAttackMotion.add(Animations.SWORD_DASH);
        return magicAttackMotion;
    }

    @Override
    protected void registerAttribute() {
        this.addStyleAttibute(WieldStyle.ONE_HAND, Pair.of(ModAttributes.MAX_STRIKES, ModAttributes.getMaxStrikesModifier(1)));
        this.addStyleAttibute(WieldStyle.ONE_HAND, Pair.of(ModAttributes.IMPACT, ModAttributes.getImpactModifier(0.5D + 0.2D * 2)));
        this.addStyleAttibute(WieldStyle.TWO_HAND, Pair.of(ModAttributes.MAX_STRIKES, ModAttributes.getMaxStrikesModifier(3)));
        this.addStyleAttibute(WieldStyle.TWO_HAND, Pair.of(ModAttributes.IMPACT, ModAttributes.getImpactModifier(0.5D + 0.2D * 2)));
    }

    @Override
    public Skill getSpecialAttack(PlayerData<?> playerdata) {
        if(this.getStyle(playerdata) == WieldStyle.ONE_HAND) {
            return Skills.SWEEPING_EDGE;
        } else {
            return Skills.SLAUGHTER_STANCE;
        }
    }

    @Override
    public List<StaticAnimation> getAutoAttckMotion(PlayerData<?> playerdata) {
            return AttackMotion;
    }


    @Override
    public StaticAnimation getSummonMotion(PlayerData<?> playerdata) {
        return summonMotion;
    }

    @Override
    public StaticAnimation getLongMagicMotion(PlayerData<?> playerdata) {return longMagicMotion;}

    @Override
    public WieldStyle getStyle(LivingData<?> entitydata) {
        return WieldStyle.ONE_HAND;
    }

    @Override
    public SoundEvent getHitSound() {
        return Sounds.BLUNT_HIT;
    }

    @Override
    public Collider getWeaponCollider() {
        return Colliders.spearNarrow;
    }

    @Override
    public Map<LivingMotion, StaticAnimation> getLivingMotionChanges(PlayerData<?> playerdata) {
        return magicAnimationSet;
    }

    @Override
    public boolean canUseOnMount() {
        return true;
    }

    @Override
    public List<StaticAnimation> getMountAttackMotion() {
        return mountAttackMotion;
    }
    /**@SideOnly(Side.CLIENT)
    @Override
    public boolean canBeRenderedBoth(ItemStack item) {
        CapabilityItem cap = item.getCapability(ModCapabilities.CAPABILITY_ITEM, null);
        return super.canBeRenderedBoth(item) || (cap != null && cap.weaponCategory == WeaponCategory.SWORD);
    }**/
}
