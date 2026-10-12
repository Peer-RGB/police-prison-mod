package de.peerrgb.policeprison.item;

import de.peerrgb.policeprison.data.PoliceStateManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HandcuffsItem extends Item {
    public HandcuffsItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
        if (!(user instanceof ServerPlayer serverUser) || !(target instanceof ServerPlayer serverTarget)) {
            return InteractionResult.PASS;
        }

        if (serverUser == serverTarget) {
            return InteractionResult.FAIL;
        }

        if (PoliceStateManager.isRestrained(serverTarget)) {
            serverUser.displayClientMessage(Component.translatable("message.policeprison.already_restrained", serverTarget.getDisplayName()), true);
            return InteractionResult.FAIL;
        }

        PoliceStateManager.setRestrained(serverTarget, true, serverUser);
        serverTarget.displayClientMessage(Component.translatable("message.policeprison.you_restrained"), true);
        serverUser.displayClientMessage(Component.translatable("message.policeprison.restrained_target", serverTarget.getDisplayName()), true);
        return InteractionResult.sidedSuccess(user.level().isClientSide);
    }
}
