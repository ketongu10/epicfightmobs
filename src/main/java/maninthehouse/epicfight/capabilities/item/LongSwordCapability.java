package maninthehouse.epicfight.capabilities.item;

import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.ModCapabilities;
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
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class LongSwordCapability extends MaterialItemCapability {

    private static List<StaticAnimation> LongswordAttackMotion;


    @Override
    protected void registerAttribute() {
        int i = this.material.getHarvestLevel();
        this.addStyleAttibute(WieldStyle.LIECHTENHAUER, Pair.of(ModAttributes.MAX_STRIKES, ModAttributes.getMaxStrikesModifier(1)));
        this.addStyleAttibute(WieldStyle.ONE_HAND, Pair.of(ModAttributes.IMPACT, ModAttributes.getImpactModifier(0.5D + 0.2D * i)));
        this.addStyleAttibute(WieldStyle.TWO_HAND, Pair.of(ModAttributes.MAX_STRIKES, ModAttributes.getMaxStrikesModifier(1)));
        this.addStyleAttibute(WieldStyle.TWO_HAND, Pair.of(ModAttributes.IMPACT, ModAttributes.getImpactModifier(0.5D + 0.2D * i)));
    }


    public LongSwordCapability(Item item) {
        super(item, WeaponCategory.LONGSWORD);
        if (LongswordAttackMotion == null) {
            LongswordAttackMotion = new ArrayList<StaticAnimation>();
            LongswordAttackMotion.add(Animations.LONGSWORD_AUTO_1);
            LongswordAttackMotion.add(Animations.LONGSWORD_AUTO_2);
            LongswordAttackMotion.add(Animations.LONGSWORD_AUTO_3);
            LongswordAttackMotion.add(Animations.LONGSWORD_DASH);

        }
    }

    @Override
    public Skill getSpecialAttack(PlayerData<?> playerdata) {
        if(this.getStyle(playerdata) == WieldStyle.LIECHTENHAUER) {
            return Skills.SWEEPING_EDGE;
        } else {
            return Skills.DANCING_EDGE;
        }
    }

    @Override
    public List<StaticAnimation> getAutoAttckMotion(PlayerData<?> playerdata) {
            return LongswordAttackMotion;
    }

    @Override
    public SoundEvent getHitSound() {
        return this.material == Item.ToolMaterial.WOOD ? Sounds.BLUNT_HIT : Sounds.BLADE_HIT;
    }

    @Override
    public Collider getWeaponCollider() {
        return Colliders.longsword;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canBeRenderedBoth(ItemStack item) {
        CapabilityItem cap = item.getCapability(ModCapabilities.CAPABILITY_ITEM, null);
        return super.canBeRenderedBoth(item) || (cap != null && cap.weaponCategory == WeaponCategory.LONGSWORD);
    }

}