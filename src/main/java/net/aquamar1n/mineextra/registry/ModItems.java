package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, AquaMod.MOD_ID);

    // === Серебряный самородок ===
    public static final DeferredHolder<Item, Item> SILVER_NUGGET = ITEMS.register("silver_nugget",
            () -> new Item(new Item.Properties()));

    // === Серебряный слиток ===
    public static final DeferredHolder<Item, Item> SILVER_INGOT = ITEMS.register("silver_ingot",
            () -> new Item(new Item.Properties()));
}
