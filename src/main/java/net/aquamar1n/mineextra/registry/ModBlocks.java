package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, AquaMod.MOD_ID);

    public static final ResourceKey<Block> SILVER_ORE_KEY =
            ResourceKey.create(Registries.BLOCK, AquaMod.id("silver_ore"));
    public static final ResourceKey<Block> DEEPSLATE_SILVER_ORE_KEY =
            ResourceKey.create(Registries.BLOCK, AquaMod.id("deepslate_silver_ore"));

    // === Серебряная руда ===
    public static final DeferredHolder<Block, Block> SILVER_ORE = registerBlock(
            "silver_ore",
            name -> new DropExperienceBlock(
                    UniformInt.of(1, 3),
                    BlockBehaviour.Properties.of()
                            .requiresCorrectToolForDrops()
                            .strength(3.0f, 3.0f)
                            .sound(SoundType.STONE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .mapColor(MapColor.STONE)
                            .setId(ResourceKey.create(Registries.BLOCK, AquaMod.id(name)))
            )
    );

    public static final DeferredHolder<Block, Block> DEEPSLATE_SILVER_ORE = registerBlock(
            "deepslate_silver_ore",
            name -> new DropExperienceBlock(
                    UniformInt.of(1, 3),
                    BlockBehaviour.Properties.of()
                            .requiresCorrectToolForDrops()
                            .strength(4.5f, 3.0f)
                            .sound(SoundType.DEEPSLATE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .mapColor(MapColor.DEEPSLATE)
                            .setId(ResourceKey.create(Registries.BLOCK, AquaMod.id(name)))
            )
    );

    public static final DeferredHolder<Block, Block> SILVER_BLOCK = registerBlock(
            "silver_block",
            name -> new Block(
                    BlockBehaviour.Properties.of()
                            .requiresCorrectToolForDrops()
                            .strength(5.0f, 6.0f)
                            .sound(SoundType.METAL)
                            .mapColor(MapColor.METAL)
                            .setId(ResourceKey.create(Registries.BLOCK, AquaMod.id(name)))
            )
    );

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Function<String, T> blockSupplier) {
        return BLOCKS.register(name, () -> blockSupplier.apply(name));
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}