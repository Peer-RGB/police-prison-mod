package de.peerrgb.policeprison.registry;

import de.peerrgb.policeprison.PolicePrisonMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PolicePrisonMod.MODID);

    public static final RegistryObject<Block> REINFORCED_PRISON_BRICK = register("reinforced_prison_brick",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(8.0F, 12.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> PRISON_BARS = register("prison_bars",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0F, 10.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> PRISON_CELL_DOOR = register("prison_cell_door",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(7.0F, 12.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> RESTRAINT_CHAIR = register("restraint_chair",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PRISON_BED = register("prison_bed",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(2.0F).sound(SoundType.WOOL)));
    public static final RegistryObject<Block> PRISON_LAMP = register("prison_lamp",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F).sound(SoundType.GLASS).lightLevel(state -> 15)));
    public static final RegistryObject<Block> SECURITY_CAMERA = register("security_camera",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.0F).sound(SoundType.METAL)));

    private static RegistryObject<Block> register(String name, Supplier<Block> supplier) {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private ModBlocks() {
    }
}
