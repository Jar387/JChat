package com.jar36.jchat.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;

public class PacketCodec {
    private static final int MAGIC = 0x6a686174;
    public static final PacketCodec INSTANCE = new PacketCodec();

    private final HashMap<Short, Class<? extends Packet>> packTypes;
    private final JsonSerializer serializer;

    private PacketCodec(){
        packTypes = new HashMap<>();
        packTypes.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packTypes.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packTypes.put(Command.LOGOUT_REQUEST, LogoutRequestPacket.class);
        serializer = new JsonSerializer();
    }

    public ByteBuf encode(ByteBufAllocator allocator, Packet packet){
        ByteBuf buf = allocator.ioBuffer();
        byte[] bytes = serializer.serialize(packet);
        // encode raw packet
        buf.writeInt(MAGIC);
        buf.writeShort(packet.getVersion());
        buf.writeShort(packet.getCommand());
        buf.writeInt(bytes.length);
        buf.writeBytes(buf);
        return buf;
    }

    public Packet decode(ByteBuf buf){
        buf.skipBytes(4); // skip magic
        buf.skipBytes(2); // skip version

        short command = buf.readShort();
        int length = buf.readInt();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        Class <? extends Packet> clazz = packTypes.get(command);
        if(clazz!=null){
            return serializer.deserialize(clazz, bytes);
        }
        return null;
    }
}
