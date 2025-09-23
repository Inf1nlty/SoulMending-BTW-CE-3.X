package com.inf1nlty.soultotem.network;

import btw.BTWAddon;
import com.inf1nlty.soultotem.client.ParticleHelper;
import net.minecraft.src.*;

import java.io.*;

public final class TotemParticleNet {
    public static String CHANNEL;
    private static final int OPCODE_TOTEM_REVIVE = 1;

    private TotemParticleNet() {}

    public static void register(BTWAddon addon) {

        CHANNEL = addon.getModID() + "|TotemFX";
        addon.registerPacketHandler(CHANNEL, (packet, player) -> {
            if (packet == null || packet.data == null || player == null) return;

            if (!player.worldObj.isRemote) return;

            try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(packet.data))) {

                int op = in.readUnsignedByte();
                if (op == OPCODE_TOTEM_REVIVE) {
                    double x = in.readDouble();
                    double y = in.readDouble();
                    double z = in.readDouble();

                    ParticleHelper.spawnTotemReviveParticles(player.worldObj, x, y, z);
                }
            } catch (Exception ignored) {}
        });
    }

    public static void sendTotemRevive(EntityPlayerMP player, double x, double y, double z) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(OPCODE_TOTEM_REVIVE);
            dos.writeDouble(x);
            dos.writeDouble(y);
            dos.writeDouble(z);
            dos.close();
            Packet250CustomPayload pkt = new Packet250CustomPayload();
            pkt.channel = CHANNEL;
            pkt.data = bos.toByteArray();
            pkt.length = pkt.data.length;
            player.playerNetServerHandler.sendPacket(pkt);
        } catch (Exception ignored) {}
    }
}