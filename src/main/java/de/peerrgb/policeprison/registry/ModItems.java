package de.peerrgb.policeprison.registry;

import de.peerrgb.policeprison.PolicePrisonMod;
import de.peerrgb.policeprison.item.HandcuffKeyItem;
import de.peerrgb.policeprison.item.HandcuffsItem;
import de.peerrgb.policeprison.item.PrisonKeyItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PolicePrisonMod.MODID);

    public static final RegistryObject<Item> HANDCUFFS = ITEMS.register("handcuffs", () -> new HandcuffsItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HANDCUFF_KEY = ITEMS.register("handcuff_key", () -> new HandcuffKeyItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PRISON_KEY = ITEMS.register("prison_key", () -> new PrisonKeyItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> POLICE_BATON = ITEMS.register("police_baton", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> POLICE_BADGE = ITEMS.register("police_badge", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FLASHLIGHT = ITEMS.register("flashlight", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> STRAIGHTJACKET = ITEMS.register("straightjacket", () -> new ArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));

    private ModItems() {
    }
}
