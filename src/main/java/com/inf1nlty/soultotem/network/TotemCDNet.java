package com.inf1nlty.soultotem.network;

import btw.BTWAddon;
import com.inf1nlty.soultotem.item.ITotemCD;
import net.minecraft.src.*;
import java.io.*;

public final class TotemCDNet {
    public static String CHANNEL;
    private static final int OPCODE_TOTEM_CD = 2;

    private TotemCDNet() {}

    public static void register(BTWAddon addon) {
        CHANNEL = addon.getModID() + "|TotemCD";
        addon.registerPacketHandler(CHANNEL, (packet, player) -> {
            if (packet == null || packet.data == null || player == null) return;

            if (!player.worldObj.isRemote) return;

            try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(packet.data))) {
                int op = in.readUnsignedByte();
                if (op == OPCODE_TOTEM_CD) {
                    int tick = in.readInt();
                    if (player instanceof ITotemCD cd) {
                        cd.soulTotem$setSoulTotemLastReviveTick(tick);
                    }
                }
            } catch (Exception ignored) {}
        });
    }

    public static void sendTotemCD(EntityPlayerMP player, int tick) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(OPCODE_TOTEM_CD);
            dos.writeInt(tick);
            dos.close();
            Packet250CustomPayload pkt = new Packet250CustomPayload();
            pkt.channel = CHANNEL;
            pkt.data = bos.toByteArray();
            pkt.length = pkt.data.length;
            player.playerNetServerHandler.sendPacket(pkt);
        } catch (Exception ignored) {}
    }
}