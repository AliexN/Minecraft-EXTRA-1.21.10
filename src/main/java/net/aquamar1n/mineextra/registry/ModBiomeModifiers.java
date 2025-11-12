package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_SILVER_ORE = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
            AquaMod.id("add_silver_ore"));
    public static final ResourceKey<BiomeModifier> ADD_DEEPSLATE_SILVER_ORE = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
            AquaMod.id("add_deepslate_silver_ore"));

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // Используем HolderSet.direct() для создания HolderSet из одного элемента
        context.register(ADD_SILVER_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SILVER_ORE)), // Исправлено здесь
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));

        context.register(ADD_DEEPSLATE_SILVER_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DEEPSLATE_SILVER_ORE)), // И здесь
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
    }
}