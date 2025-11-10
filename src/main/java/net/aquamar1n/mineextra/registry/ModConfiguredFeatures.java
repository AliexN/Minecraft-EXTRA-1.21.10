package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> SILVER_ORE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(AquaMod.MOD_ID, "silver_ore"));

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        // ВАЖНО: Используем Holder вместо прямого доступа к блокам
        var silverOreHolder = context.lookup(Registries.BLOCK).getOrThrow(ModBlocks.SILVER_ORE_KEY);
        var deepslateSilverOreHolder = context.lookup(Registries.BLOCK).getOrThrow(ModBlocks.DEEPSLATE_SILVER_ORE_KEY);

        List<OreConfiguration.TargetBlockState> targets = List.of(
                OreConfiguration.target(stoneReplaceables, silverOreHolder.value().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, deepslateSilverOreHolder.value().defaultBlockState())
        );

        context.register(
                SILVER_ORE_KEY,
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 8))
        );
    }
}