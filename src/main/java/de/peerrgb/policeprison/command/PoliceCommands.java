package de.peerrgb.policeprison.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import de.peerrgb.policeprison.PolicePrisonMod;
import de.peerrgb.policeprison.data.PoliceStateManager;
import de.peerrgb.policeprison.data.PlayerPoliceState;
import de.peerrgb.policeprison.data.PoliceSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public final class PoliceCommands {
    private PoliceCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("police")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("arrest")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> setArrestState(context, true))))
                .then(Commands.literal("release")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> setArrestState(context, false))))
                .then(Commands.literal("jail")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(PoliceCommands::jailPlayer)))
                .then(Commands.literal("unjail")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(PoliceCommands::unjailPlayer)))
                .then(Commands.literal("status")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(PoliceCommands::statusPlayer)))
                .then(Commands.literal("setjail")
                        .then(Commands.argument("dimension", StringArgumentType.string())
                                .executes(PoliceCommands::setJailPosition))));
    }

    private static int setArrestState(CommandContext<CommandSourceStack> context, boolean restrained) {
        ServerPlayer sourcePlayer = context.getSource().getPlayer();
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        PoliceStateManager.setRestrained(target, restrained, sourcePlayer);

        Component feedback = restrained
                ? Component.translatable("message.policeprison.command.arrested", target.getDisplayName())
                : Component.translatable("message.policeprison.command.released", target.getDisplayName());
        context.getSource().sendSuccess(() -> feedback, true);
        return 1;
    }

    private static int jailPlayer(CommandContext<CommandSourceStack> context) {
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        ServerPlayer source = context.getSource().getPlayer();
        if (source == null) {
            context.getSource().sendFailure(Component.translatable("message.policeprison.command.player_only"));
            return 0;
        }

        if (!PoliceStateManager.hasJailPosition(source)) {
            context.getSource().sendFailure(Component.translatable("message.policeprison.command.no_jail_config"));
            return 0;
        }

        ResourceLocation dimId = PoliceStateManager.getJailDimension(source);
        ServerLevel level = context.getSource().getServer().getLevel(ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, dimId));
        if (level == null) {
            context.getSource().sendFailure(Component.translatable("message.policeprison.command.invalid_dimension", dimId.toString()));
            return 0;
        }

        BlockPos jailPos = PoliceStateManager.getJailPos(source);
        PoliceStateManager.setJailed(target, true);
        PoliceStateManager.setRestrained(target, true, source);
        PoliceStateManager.updateAnchor(target, dimId, jailPos);
        target.teleportTo(level, jailPos.getX() + 0.5D, jailPos.getY() + 0.1D, jailPos.getZ() + 0.5D, target.getYRot(), target.getXRot());

        context.getSource().sendSuccess(() -> Component.translatable("message.policeprison.command.jailed", target.getDisplayName()), true);
        target.displayClientMessage(Component.translatable("message.policeprison.you_jailed"), true);
        return 1;
    }

    private static int unjailPlayer(CommandContext<CommandSourceStack> context) {
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        PoliceStateManager.setJailed(target, false);
        context.getSource().sendSuccess(() -> Component.translatable("message.policeprison.command.unjailed", target.getDisplayName()), true);
        return 1;
    }

    private static int statusPlayer(CommandContext<CommandSourceStack> context) {
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        PlayerPoliceState state = PoliceSavedData.get(target.serverLevel()).getOrCreate(target.getUUID());
        context.getSource().sendSuccess(() -> Component.translatable("message.policeprison.command.status",
                target.getDisplayName(),
                state.isRestrained() ? Component.translatable("message.policeprison.status.yes") : Component.translatable("message.policeprison.status.no"),
                state.isJailed() ? Component.translatable("message.policeprison.status.yes") : Component.translatable("message.policeprison.status.no")), false);
        return 1;
    }

    private static int setJailPosition(CommandContext<CommandSourceStack> context) {
        ServerPlayer source = context.getSource().getPlayer();
        if (source == null) {
            context.getSource().sendFailure(Component.translatable("message.policeprison.command.player_only"));
            return 0;
        }

        String dimString = StringArgumentType.getString(context, "dimension");
        ResourceLocation dimensionId;
        try {
            dimensionId = new ResourceLocation(dimString);
        } catch (Exception ex) {
            context.getSource().sendFailure(Component.translatable("message.policeprison.command.invalid_dimension", dimString));
            return 0;
        }

        ServerLevel level = context.getSource().getServer().getLevel(ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, dimensionId));
        if (level == null) {
            context.getSource().sendFailure(Component.translatable("message.policeprison.command.invalid_dimension", dimString));
            return 0;
        }

        BlockPos pos = source.blockPosition();
        PoliceStateManager.setJailPosition(source, dimensionId, pos);
        context.getSource().sendSuccess(() -> Component.translatable("message.policeprison.command.jail_set", pos.getX(), pos.getY(), pos.getZ(), dimensionId.toString()), true);
        PolicePrisonMod.LOGGER.info("Jail position set to {} in {}", pos, dimensionId);
        return 1;
    }
}
