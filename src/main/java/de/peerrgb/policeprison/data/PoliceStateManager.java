package de.peerrgb.policeprison.data;

import de.peerrgb.policeprison.network.ModNetwork;
import de.peerrgb.policeprison.network.StateSyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public final class PoliceStateManager {
    private PoliceStateManager() {
    }

    public static PlayerPoliceState getState(ServerPlayer player) {
        return PoliceSavedData.get(player.serverLevel()).getOrCreate(player.getUUID());
    }

    public static boolean isRestrained(ServerPlayer player) {
        return getState(player).isRestrained();
    }

    public static boolean isJailed(ServerPlayer player) {
        return getState(player).isJailed();
    }

    public static void setRestrained(ServerPlayer player, boolean restrained, ServerPlayer actor) {
        PoliceSavedData data = PoliceSavedData.get(player.serverLevel());
        PlayerPoliceState state = data.getOrCreate(player.getUUID());
        state.setRestrained(restrained);
        if (restrained) {
            state.setAnchorDimension(player.serverLevel().dimension().location());
            state.setAnchorPos(player.blockPosition());
            state.setJailed(false);
        } else if (!state.isJailed()) {
            state.setAnchorDimension(null);
            state.setAnchorPos(null);
        }
        data.setDirty();
        sync(player);
        if (actor != null && actor != player) {
            sync(actor);
        }
    }

    public static void setJailed(ServerPlayer player, boolean jailed) {
        PoliceSavedData data = PoliceSavedData.get(player.serverLevel());
        PlayerPoliceState state = data.getOrCreate(player.getUUID());
        state.setJailed(jailed);

        if (!jailed) {
            state.setRestrained(false);
            state.setAnchorDimension(null);
            state.setAnchorPos(null);
        }

        data.setDirty();
        sync(player);
    }

    public static void updateAnchor(ServerPlayer player, ResourceLocation dimension, BlockPos pos) {
        PoliceSavedData data = PoliceSavedData.get(player.serverLevel());
        PlayerPoliceState state = data.getOrCreate(player.getUUID());
        state.setAnchorDimension(dimension);
        state.setAnchorPos(pos);
        data.setDirty();
    }

    public static PlayerPoliceState getStateOrNull(ServerPlayer player) {
        return PoliceSavedData.get(player.serverLevel()).get(player.getUUID());
    }

    public static void clearState(ServerPlayer player) {
        PoliceSavedData data = PoliceSavedData.get(player.serverLevel());
        data.remove(player.getUUID());
        sync(player);
    }

    public static void copyOnRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer) {
        PoliceSavedData oldData = PoliceSavedData.get(oldPlayer.serverLevel());
        PlayerPoliceState oldState = oldData.get(oldPlayer.getUUID());
        if (oldState == null) {
            return;
        }

        PoliceSavedData newData = PoliceSavedData.get(newPlayer.serverLevel());
        PlayerPoliceState copied = newData.getOrCreate(newPlayer.getUUID());
        copied.setRestrained(oldState.isRestrained());
        copied.setJailed(oldState.isJailed());
        copied.setAnchorDimension(oldState.getAnchorDimension());
        copied.setAnchorPos(oldState.getAnchorPos());
        newData.setDirty();
        sync(newPlayer);
    }

    public static void sync(ServerPlayer player) {
        PlayerPoliceState state = getState(player);
        ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new StateSyncPacket(state.isRestrained(), state.isJailed()));
    }

    public static String statusText(ServerPlayer player) {
        PlayerPoliceState state = getState(player);
        return "restrained=" + state.isRestrained() + ", jailed=" + state.isJailed();
    }

    public static void setJailPosition(ServerPlayer source, ResourceLocation dimension, BlockPos pos) {
        PoliceSavedData.get(source.serverLevel()).setJailPosition(dimension, pos);
    }

    public static boolean hasJailPosition(ServerPlayer source) {
        PoliceSavedData data = PoliceSavedData.get(source.serverLevel());
        return data.getJailDimension() != null && data.getJailPos() != null;
    }

    public static ResourceLocation getJailDimension(ServerPlayer source) {
        return PoliceSavedData.get(source.serverLevel()).getJailDimension();
    }

    public static BlockPos getJailPos(ServerPlayer source) {
        return PoliceSavedData.get(source.serverLevel()).getJailPos();
    }

    public static void removeByUuid(ServerPlayer context, UUID playerUuid) {
        PoliceSavedData.get(context.serverLevel()).remove(playerUuid);
    }
}
