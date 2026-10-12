package de.peerrgb.policeprison;

import com.mojang.logging.LogUtils;
import de.peerrgb.policeprison.command.PoliceCommands;
import de.peerrgb.policeprison.config.ModConfig;
import de.peerrgb.policeprison.event.ModEvents;
import de.peerrgb.policeprison.network.ModNetwork;
import de.peerrgb.policeprison.registry.ModBlocks;
import de.peerrgb.policeprison.registry.ModCreativeTabs;
import de.peerrgb.policeprison.registry.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(PolicePrisonMod.MODID)
public class PolicePrisonMod {
    public static final String MODID = "policeprison";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PolicePrisonMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(new ModEvents());
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModNetwork::register);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        PoliceCommands.register(event.getDispatcher());
    }
}
