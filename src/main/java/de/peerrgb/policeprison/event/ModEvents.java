package de.peerrgb.policeprison.event;

import de.peerrgb.policeprison.PolicePrisonMod;
import de.peerrgb.policeprison.data.PoliceSavedData;
import de.peerrgb.policeprison.data.PoliceStateManager;
import de.peerrgb.policeprison.util.RestraintLogic;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PolicePrisonMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (RestraintLogic.isRestricted(player)) {
            event.setCanceled(true);
            player.displayClientMessage(Component.translatable("message.policeprison.action_blocked"), true);
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (RestraintLogic.isRestricted(player)) {
            event.setCanceled(true);
            player.displayClientMessage(Component.translatable("message.policeprison.action_blocked"), true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (RestraintLogic.isRestricted(player)) {
            event.setCanceled(true);
            player.displayClientMessage(Component.translatable("message.policeprison.action_blocked"), true);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (RestraintLogic.isRestricted(player)) {
            event.setCanceled(true);
            player.displayClientMessage(Component.translatable("message.policeprison.action_blocked"), true);
        }
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) {
            return;
        }
        if (RestraintLogic.isRestricted(player)) {
            event.setCanceled(true);
            player.displayClientMessage(Component.translatable("message.policeprison.action_blocked"), true);
        }
    }

    @SubscribeEvent
    public static void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (RestraintLogic.isRestricted(player)) {
            event.setCanceled(true);
            player.displayClientMessage(Component.translatable("message.policeprison.action_blocked"), true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }
        if (event.player instanceof ServerPlayer player) {
            RestraintLogic.enforceMovement(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PoliceStateManager.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getOriginal() instanceof ServerPlayer oldPlayer && event.getEntity() instanceof ServerPlayer newPlayer) {
            PoliceStateManager.copyOnRespawn(oldPlayer, newPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PoliceStateManager.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (player.server.isSingleplayer()) {
            return;
        }
        var state = PoliceSavedData.get(player.serverLevel()).get(player.getUUID());
        if (state != null && !state.isJailed() && !state.isRestrained()) {
            PoliceStateManager.removeByUuid(player, player.getUUID());
        }
    }

    @Mod.EventBusSubscriber(modid = PolicePrisonMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase != TickEvent.Phase.END || !event.player.level().isClientSide || !event.player.isLocalPlayer()) {
                return;
            }
            if (de.peerrgb.policeprison.client.ClientState.isRestrained() || de.peerrgb.policeprison.client.ClientState.isJailed()) {
                event.player.setSprinting(false);
            }
        }
    }
}
