package maninthehouse.epicfight.config;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import maninthehouse.epicfight.main.EpicFightMod;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static maninthehouse.epicfight.main.LoaderConstants.EBWIZARDRY;

@Config(modid = EpicFightMod.MODID, name = EpicFightMod.MODID+"_entity", category="custom")
public class EntityConfigurationCapability {

    @Config.Name("custom_entity")
    public static CustomEntityConfig entityConfig = new CustomEntityConfig();

    public static class CustomEntityConfig {
        @Config.Name("sample_entity1")
        public ZombieSampleConfig zombieEntity = new ZombieSampleConfig();
        @Config.Name("sample_entity2")
        public KnightConfig knightEntity = new KnightConfig();
        @Config.Name("sample_entity3")
        public MonsterSampleConfig monsterEntity = new MonsterSampleConfig();
    }

    public static class EntityConfig {
        @Config.Name("registry_name")
        public String registryName = "modid:registryname";
        @Config.Name("impact")
        public float impact = 1.5F;
        @Config.Name("stun_armor")
        public float stun_armor = 4.0F;
        @Config.Name("can_parry")
        public boolean parry = true;
        @Config.Name("64_x_32_texture")
        public boolean oldTexture = false;
        @Config.Name("model")
        public EntityType model = EntityType.ZOMBIE;
    }
    public static class ZombieSampleConfig {
        @Config.Name("registry_name")
        public String registryName = "modid:registryname";
        @Config.Name("impact")
        public float impact = 1.5F;
        @Config.Name("stun_armor")
        public float stun_armor = 2.0F;
        @Config.Name("model")
        public EntityType model = EntityType.ZOMBIE;
        @Config.Comment("USE 64_x_32_texture=true FOR ENTITIES THAT HAVE TEXTURE SIZE 64x32, OTHERWISE 64x64 TEXTURE WILL BE USED")
        @Config.Name("64_x_32_texture")
        public boolean oldTexture = true;
    }



    public static class MonsterSampleConfig {
        @Config.Comment("USE model=MONSTER FOR ENTITIES THAT ALREADY HAVE BEAUTIFUL MODEL")
        @Config.Name("registry_name")
        public String registryName = "iceandfire:if_troll";
        @Config.Name("impact")
        public float impact = 4.0F;
        @Config.Name("model")
        public EntityType model = EntityType.MONSTER;
    }


    public static class KnightConfig {
        @Config.Comment("USE model=KNIGHT FOR ENTITIES THAT SHOULD FIGHT LIKE PLAYER")
        @Config.Name("registry_name")
        public String registryName = "knightmod:usual_knight";
        @Config.Name("impact")
        public float impact = 3.5F;
        @Config.Name("model")
        public EntityType model = EntityType.KNIGHT;
        @Config.Name("stun_armor")
        public float stun_armor = 4.0F;
        @Config.Name("can_parry")
        public boolean parry = true;
    }



    public static enum EntityType {
        WIZARD, ENDERMAN, GOLEM, MONSTER, ILLAGER, ZOMBIE, SKELETON, PIGLIN, KNIGHT;



        static Map<String, EntityType> searchByName = Maps.<String, EntityType>newHashMap();

        static {
            searchByName.put("WIZARD", EntityType.WIZARD);
            searchByName.put("ENDERMAN", EntityType.ENDERMAN);
            searchByName.put("GOLEM", EntityType.GOLEM);
            searchByName.put("MONSTER", EntityType.MONSTER);
            searchByName.put("ILLAGER", EntityType.ILLAGER);
            searchByName.put("ZOMBIE", EntityType.ZOMBIE);
            searchByName.put("SKELETON", EntityType.SKELETON);
            searchByName.put("PIGLIN", EntityType.PIGLIN);
            searchByName.put("KNIGHT", EntityType.KNIGHT);
        }

        public static EntityType findByName(String name) {
            return searchByName.get(name);
        }
    }

    public static List<EntityConfig> getEntityConfigs() {
        List<EntityConfig> list = Lists.<EntityConfig>newArrayList();
        IConfigElement root = ConfigElement.from(EntityConfigurationCapability.class);
        IConfigElement entities = getElementByName(root.getChildElements(), "custom_entity");

        for (IConfigElement configElement : entities.getChildElements()) {
            List<IConfigElement> childElements = configElement.getChildElements();
            EntityConfig config = new EntityConfig();
            config.registryName = (String) getElementByName(childElements, "registry_name").get();
            config.impact = Float.parseFloat((String) getElementByName(childElements, "impact").get());
            config.model = EntityType.findByName((String) getElementByName(childElements, "model").get());
            IConfigElement sa = getElementByName(childElements, "stun_armor");
            if (sa!=null) config.stun_armor = Float.parseFloat((String) sa.get());
            IConfigElement p = getElementByName(childElements, "can_parry");
            if (p!=null) config.parry = new Boolean((String) p.get());
            IConfigElement t = getElementByName(childElements, "64_x_32_texture");
            if (t!=null) config.oldTexture = new Boolean((String) t.get());


            list.add(config);

        }

        return list;
    }



    public static IConfigElement getElementByName(List<IConfigElement> configElements, String name) {
        for (IConfigElement element : configElements) {
            if (element.getName().equals(name)) {
                return element;
            }
        }

        return null;
    }
}
