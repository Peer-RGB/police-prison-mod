package de.peerrgb.policeprison.network;

import de.peerrgb.policeprison.client.ClientState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record StateSyncPacket(boolean restrained, boolean jailed) {
    public static void encode(StateSyncPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.restrained);
        buffer.writeBoolean(packet.jailed);
    }

    public static StateSyncPacket decode(FriendlyByteBuf buffer) {
        return new StateSyncPacket(buffer.readBoolean(), buffer.readBoolean());
    }

    public static void handle(StateSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientState.set(packet.restrained, packet.jailed)));
        context.setPacketHandled(true);
    }
}
