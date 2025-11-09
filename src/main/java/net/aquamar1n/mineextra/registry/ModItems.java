package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, AquaMod.MOD_ID);

    // BlockItems для блоков - используем лямбды, но без .get() при создании
    public static final DeferredHolder<Item, Item> SILVER_ORE_ITEM = ITEMS.register("silver_ore",
            () -> new BlockItem(ModBlocks.SILVER_ORE.get(), new Item.Properties()));

    public static final DeferredHolder<Item, Item> DEEPSLATE_SILVER_ORE_ITEM = ITEMS.register("deepslate_silver_ore",
            () -> new BlockItem(ModBlocks.DEEPSLATE_SILVER_ORE.get(), new Item.Properties()));

    public static final DeferredHolder<Item, Item> SILVER_BLOCK_ITEM = ITEMS.register("silver_block",
            () -> new BlockItem(ModBlocks.SILVER_BLOCK.get(), new Item.Properties()));

    // Простейшие предметы: слиток и самородок
    public static final DeferredHolder<Item, Item> SILVER_INGOT = ITEMS.register("silver_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> SILVER_NUGGET = ITEMS.register("silver_nugget",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}