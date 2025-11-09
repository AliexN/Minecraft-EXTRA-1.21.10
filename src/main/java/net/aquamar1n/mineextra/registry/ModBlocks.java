package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, AquaMod.MOD_ID);

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, AquaMod.MOD_ID);

    // === Серебряная руда ===
    public static final DeferredHolder<Block, Block> SILVER_ORE = registerBlock(
            "silver_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(1, 3),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(3.0f, 3.0f)
                            .sound(SoundType.STONE)
            )
    );

    // === Глубинная серебряная руда ===
    public static final DeferredHolder<Block, Block> DEEPSLATE_SILVER_ORE = registerBlock(
            "deepslate_silver_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(1, 3),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(4.5f, 3.0f)
                            .sound(SoundType.DEEPSLATE)
            )
    );

    /** Универсальный метод для регистрации блока и его BlockItem */
    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> blockSupplier) {
        DeferredHolder<Block, T> block = BLOCKS.register(name, blockSupplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
}
