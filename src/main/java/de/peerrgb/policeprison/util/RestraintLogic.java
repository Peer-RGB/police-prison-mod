package de.peerrgb.policeprison.util;

import de.peerrgb.policeprison.config.ModConfig;
import de.peerrgb.policeprison.data.PlayerPoliceState;
import de.peerrgb.policeprison.data.PoliceSavedData;
import de.peerrgb.policeprison.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public final class RestraintLogic {
    private RestraintLogic() {
    }

    public static boolean isWearingStraightjacket(ServerPlayer player) {
        return player.getInventory().armor.get(2).is(ModItems.STRAIGHTJACKET.get());
    }

    public static boolean isRestricted(ServerPlayer player) {
        PlayerPoliceState state = PoliceSavedData.get(player.serverLevel()).get(player.getUUID());
        return (state != null && state.isRestrained()) || isWearingStraightjacket(player);
    }

    public static void enforceMovement(ServerPlayer player) {
        PoliceSavedData data = PoliceSavedData.get(player.serverLevel());
        PlayerPoliceState state = data.get(player.getUUID());
        if (state == null) {
            return;
        }

        if (!state.isRestrained() && !state.isJailed()) {
            return;
        }

        ResourceLocation anchorDim = state.getAnchorDimension();
        BlockPos anchorPos = state.getAnchorPos();
        if (anchorDim == null || anchorPos == null) {
            state.setAnchorDimension(player.serverLevel().dimension().location());
            state.setAnchorPos(player.blockPosition());
            data.setDirty();
            return;
        }

        if (!ModConfig.LOCK_RESTRAINED_MOVEMENT.get()) {
            player.setDeltaMovement(Vec3.ZERO);
            return;
        }

        ServerLevel level = player.serverLevel();
        if (!anchorDim.equals(level.dimension().location())) {
            return;
        }

        Vec3 anchorCenter = Vec3.atCenterOf(anchorPos);
        double maxDistance = state.isJailed() ? 1.0D : ModConfig.RESTRAIN_RADIUS.get();
        if (player.position().distanceTo(anchorCenter) > maxDistance) {
            player.teleportTo(anchorCenter.x, anchorCenter.y, anchorCenter.z);
        }
        player.setDeltaMovement(Vec3.ZERO);
        player.hurtMarked = true;
    }
}
