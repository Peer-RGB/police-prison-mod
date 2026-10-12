package de.peerrgb.policeprison.registry;

import de.peerrgb.policeprison.PolicePrisonMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(ForgeRegistries.CREATIVE_MODE_TABS, PolicePrisonMod.MODID);

    public static final RegistryObject<CreativeModeTab> POLICE_TAB = CREATIVE_MODE_TABS.register("policeprison_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.policeprison.main"))
            .icon(() -> new ItemStack(ModItems.HANDCUFFS.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.HANDCUFFS.get());
                output.accept(ModItems.HANDCUFF_KEY.get());
                output.accept(ModItems.PRISON_KEY.get());
                output.accept(ModItems.POLICE_BATON.get());
                output.accept(ModItems.POLICE_BADGE.get());
                output.accept(ModItems.FLASHLIGHT.get());
                output.accept(ModItems.STRAIGHTJACKET.get());
                output.accept(ModBlocks.REINFORCED_PRISON_BRICK.get());
                output.accept(ModBlocks.PRISON_BARS.get());
                output.accept(ModBlocks.PRISON_CELL_DOOR.get());
                output.accept(ModBlocks.RESTRAINT_CHAIR.get());
                output.accept(ModBlocks.PRISON_BED.get());
                output.accept(ModBlocks.PRISON_LAMP.get());
                output.accept(ModBlocks.SECURITY_CAMERA.get());
            })
            .build());

    private ModCreativeTabs() {
    }
}
