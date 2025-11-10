package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, AquaMod.MOD_ID);

    // BlockItems для блоков - используем лямбды без .get()
    public static final DeferredHolder<Item, Item> SILVER_ORE_ITEM = registerBlockItem(
            "silver_ore",
            name -> new BlockItem(
                    ModBlocks.SILVER_ORE.value(),  // Используйте .value() вместо .get()
                    new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, AquaMod.id(name)))  // Установка ID
            )
    );

    public static final DeferredHolder<Item, Item> DEEPSLATE_SILVER_ORE_ITEM = registerBlockItem(
            "deepslate_silver_ore",
            name -> new BlockItem(
                    ModBlocks.DEEPSLATE_SILVER_ORE.value(),
                    new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, AquaMod.id(name)))
            )
    );

    public static final DeferredHolder<Item, Item> SILVER_BLOCK_ITEM = registerBlockItem(
            "silver_block",
            name -> new BlockItem(
                    ModBlocks.SILVER_BLOCK.value(),
                    new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, AquaMod.id(name)))
            )
    );

    // Простейшие предметы: слиток и самородок
    public static final DeferredHolder<Item, Item> SILVER_INGOT = registerItem(
            "silver_ingot",
            name -> new Item(
                    new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, AquaMod.id(name)))
            )
    );

    public static final DeferredHolder<Item, Item> SILVER_NUGGET = registerItem(
            "silver_nugget",
            name -> new Item(
                    new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, AquaMod.id(name)))
            )
    );

    private static DeferredHolder<Item, Item> registerBlockItem(String name, Function<String, BlockItem> itemSupplier) {
        return ITEMS.register(name, () -> itemSupplier.apply(name));
    }

    private static DeferredHolder<Item, Item> registerItem(String name, Function<String, Item> itemSupplier) {
        return ITEMS.register(name, () -> itemSupplier.apply(name));
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}