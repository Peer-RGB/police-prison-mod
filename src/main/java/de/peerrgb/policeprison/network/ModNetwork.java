package de.peerrgb.policeprison.network;

import de.peerrgb.policeprison.PolicePrisonMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ModNetwork {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PolicePrisonMod.MODID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    private static int index;

    private ModNetwork() {
    }

    public static void register() {
        CHANNEL.registerMessage(index++, StateSyncPacket.class, StateSyncPacket::encode, StateSyncPacket::decode, StateSyncPacket::handle);
    }
}
