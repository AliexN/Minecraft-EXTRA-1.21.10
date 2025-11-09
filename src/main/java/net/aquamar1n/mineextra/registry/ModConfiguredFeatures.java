package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Holder;

import java.util.List;

public class ModConfiguredFeatures {

    // Ключ для конфигурации генерации серебряной руды
    public static final ResourceKey<ConfiguredFeature<?, ?>> SILVER_ORE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(AquaMod.MOD_ID, "silver_ore"));

    // Регистрация конфигурации руды
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // Типы блоков, которые руда может заменять
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        var blockLookup = context.lookup(Registries.BLOCK);

        // Цели замещения
        List<OreConfiguration.TargetBlockState> targets = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.SILVER_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState())
        );

        // Регистрируем новую фичу
        context.register(
                SILVER_ORE_KEY,
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 8)) // 8 — размер жилы
        );
    }
}
