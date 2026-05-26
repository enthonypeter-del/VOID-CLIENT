package com.voidclient.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class HandshakeChannel extends SimpleNetworkWrapper implements IMessageHandler<HandshakeChannel.VoidClientHandshake, IMessage> {

    public HandshakeChannel() {
        super("VoidClientSync");
        registerMessage(this, VoidClientHandshake.class, 0, Side.SERVER);
        registerMessage(this, VoidClientHandshake.class, 1, Side.CLIENT);
    }

    @Override
    public IMessage onMessage(VoidClientHandshake message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            player.addChatMessage(new net.minecraft.util.ChatComponentText("§c[Void] Jogador detectado: " + player.getName()));
        }
        return null;
    }

    public static class VoidClientHandshake implements IMessage {
        private String name;

        public VoidClientHandshake() { }

        public VoidClientHandshake(String name) {
            this.name = name;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            int length = buf.readInt();
            byte[] data = new byte[length];
            buf.readBytes(data);
            name = new String(data);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            byte[] data = name.getBytes();
            buf.writeInt(data.length);
            buf.writeBytes(data);
        }
    }
}
