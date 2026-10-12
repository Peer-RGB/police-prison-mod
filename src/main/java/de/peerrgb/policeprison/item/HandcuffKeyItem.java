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

public class HandcuffKeyItem extends Item {
    public HandcuffKeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
        if (!(user instanceof ServerPlayer serverUser) || !(target instanceof ServerPlayer serverTarget)) {
            return InteractionResult.PASS;
        }

        if (serverUser == serverTarget) {
            serverUser.displayClientMessage(Component.translatable("message.policeprison.no_self_release"), true);
            return InteractionResult.FAIL;
        }

        if (!PoliceStateManager.isRestrained(serverTarget)) {
            serverUser.displayClientMessage(Component.translatable("message.policeprison.not_restrained", serverTarget.getDisplayName()), true);
            return InteractionResult.FAIL;
        }

        PoliceStateManager.setRestrained(serverTarget, false, serverUser);
        serverTarget.displayClientMessage(Component.translatable("message.policeprison.you_released"), true);
        serverUser.displayClientMessage(Component.translatable("message.policeprison.released_target", serverTarget.getDisplayName()), true);
        return InteractionResult.sidedSuccess(user.level().isClientSide);
    }
}
