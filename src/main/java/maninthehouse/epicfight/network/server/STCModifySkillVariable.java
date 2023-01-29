package maninthehouse.epicfight.network.server;

import io.netty.buffer.ByteBuf;
import maninthehouse.epicfight.client.ClientEngine;
import maninthehouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninthehouse.epicfight.network.ModNetworkManager;
import maninthehouse.epicfight.skill.SkillDataManager;
import maninthehouse.epicfight.skill.SkillSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class STCModifySkillVariable implements IMessage {
	private Object value;
	private int slot;
	private int id;

	public STCModifySkillVariable() {
		this.value = null;
	}



	public STCModifySkillVariable(SkillDataManager.SkillDataKey<?> key, int slot, Object value) {
		this.value = value;
		this.slot = slot;
		this.id = key.getId();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.value = SkillDataManager.SkillDataKey.findById(id).getValueType().readFromBuffer(buf);
		this.slot = buf.readInt();
		this.id = buf.readInt();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		SkillDataManager.SkillDataKey.findById(id).getValueType().writeToBuffer(buf, value); //maybe wrong reading of object
		buf.writeInt(this.slot);
		buf.writeInt(this.id);

	}

	public static class Handler implements IMessageHandler<STCModifySkillVariable, IMessage> {
		@Override
		public IMessage onMessage(STCModifySkillVariable msg, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				ClientPlayerData playerdata = ClientEngine.INSTANCE.getPlayerData();

				if (playerdata != null) {
					SkillDataManager dataManager = playerdata.getSkill(msg.slot).getDataManager();
					dataManager.setData(SkillDataManager.SkillDataKey.findById(msg.id), msg.value);
				}
		    });


			return null;
		}
	}


}