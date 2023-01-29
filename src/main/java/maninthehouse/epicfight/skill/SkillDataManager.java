package maninthehouse.epicfight.skill;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import maninthehouse.epicfight.network.ModNetworkManager;
import maninthehouse.epicfight.network.server.STCModifySkillVariable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

import java.util.Map;

public class SkillDataManager {
    private final Map<Integer, Data> data = Maps.<Integer, Data>newHashMap();
    private final int slotIndex;

    public SkillDataManager(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    public <T> void registerData(SkillDataKey<T> key) {
        this.data.put(key.getId(), key.valueType.create());
    }

    public <T> void setData(SkillDataKey<T> key, Object data) {
        if (this.hasData(key)) {
            key.valueType.set(this.data.get(key.getId()), data);
        }
    }

    public <T> void setDataSync(SkillDataKey<T> key, Object data, EntityPlayerMP player) {
        this.setData(key, data);
        STCModifySkillVariable msg2 = new STCModifySkillVariable(key, this.slotIndex, data);
        ModNetworkManager.sendToPlayer(msg2, player);
    }

    public Data getData(SkillDataKey<?> key) {
        return this.data.get(key.getId());
    }

    public <T> T getDataValue(SkillDataKey<T> key) {
        if (this.hasData(key)) {
            return key.valueType.get(this.data.get(key.getId()));
        }

        return null;
    }

    public boolean hasData(SkillDataKey<?> key) {
        return this.data.containsKey(key.getId());
    }

    public void reset() {
        this.data.clear();
    }

    static abstract class Data {
        static class IntegerData extends Data {
            int data;
        }

        static class BooleanData extends Data {
            boolean data;
        }

        static class FloatData extends Data {
            float data;
        }
    }

    public static abstract class ValueType<T> {
        public static final IntegerType INTEGER = new IntegerType();
        public static final FloatType FLOAT = new FloatType();
        public static final BooleanType BOOLEAN = new BooleanType();

        public abstract Data create();
        public abstract void set(Data data, Object value);
        public abstract T get(Data data);
        public abstract void writeToBuffer(ByteBuf buf, Object data);
        public abstract T readFromBuffer(ByteBuf buf);

        private static class IntegerType extends ValueType<Integer> {
            @Override
            public Data.IntegerData create() {
                return new Data.IntegerData();
            }

            @Override
            public void set(Data data, Object value) {
                ((Data.IntegerData)data).data = (int)value;
            }

            @Override
            public Integer get(Data data) {
                return data != null ? ((Data.IntegerData)data).data : 0;
            }

            @Override
            public void writeToBuffer(ByteBuf buf, Object data) {
                buf.writeInt((int)data);
            }

            @Override
            public Integer readFromBuffer(ByteBuf buf) {
                return buf.readInt();
            }
        }

        private static class BooleanType extends ValueType<Boolean> {
            @Override
            public Data.BooleanData create() {
                return new Data.BooleanData();
            }

            @Override
            public void set(Data data, Object value) {
                ((Data.BooleanData)data).data = (boolean)value;
            }

            @Override
            public Boolean get(Data data) {
                return data != null ? ((Data.BooleanData)data).data : false;
            }

            @Override
            public void writeToBuffer(ByteBuf buf, Object data) {
                buf.writeBoolean((boolean)data);
            }

            @Override
            public Boolean readFromBuffer(ByteBuf buf) {
                return buf.readBoolean();
            }
        }

        private static class FloatType extends ValueType<Float> {
            @Override
            public Data.FloatData create() {
                return new Data.FloatData();
            }

            @Override
            public void set(Data data, Object value) {
                ((Data.FloatData)data).data = (float)value;
            }

            @Override
            public Float get(Data data) {
                return data != null ? ((Data.FloatData)data).data : 0.0F;
            }

            @Override
            public void writeToBuffer(ByteBuf buf, Object data) {
                buf.writeFloat((float)data);
            }

            @Override
            public Float readFromBuffer(ByteBuf buf) {
                return buf.readFloat();
            }
        }
    }

    public static class SkillDataKey<T> {
        private static int NEXT_ID;
        private static final Map<Integer, SkillDataKey<?>> KEYS = Maps.<Integer, SkillDataKey<?>>newHashMap();

        public static <V> SkillDataKey<V> createDataKey(ValueType<V> valueType) {
            int id = NEXT_ID++;
            SkillDataKey<V> key = new SkillDataKey<>(valueType, id);
            KEYS.put(id, key);
            return key;
        }

        public static SkillDataKey<?> findById(int id) {
            return KEYS.get(id);
        }

        private final ValueType<T> valueType;
        private final int id;

        private SkillDataKey(ValueType<T> valueType, int id) {
            this.valueType = valueType;
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public ValueType<T> getValueType() {
            return this.valueType;
        }
    }
}
