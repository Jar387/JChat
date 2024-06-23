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
        packTypes.put(Command.MESSAGE_REQUEST, MessagePacket.class);
        serializer = new JsonSerializer();
    }

    public ByteBuf encode(ByteBuf buf, Packet packet){
        byte[] bytes = serializer.serialize(packet);
        // encode raw packet
        buf.writeInt(MAGIC);
        buf.writeShort(packet.getVersion());
        buf.writeShort(packet.getCommand());
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        return buf;
    }

    public Packet decode(ByteBuf buf){
        int magic = buf.readInt();
        short version = buf.readShort();

        if(magic!=MAGIC){
            System.out.println("Unknown packet format");
            System.exit(-1);
        }
        if(version>1){
            // TODO: change this when protocol version changes
            System.out.println("Unsupported protocol version");
            System.exit(-1);
        }

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
