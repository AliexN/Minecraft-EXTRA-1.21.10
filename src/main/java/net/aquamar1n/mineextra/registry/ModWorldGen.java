package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public class ModWorldGen {

    public static void registerDataMaps(RegisterDataMapTypesEvent event) {
        // Регистрируем наши фичи
    }

    public static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        ModConfiguredFeatures.bootstrap(context);
    }

    public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        ModPlacedFeatures.bootstrap(context);
    }
}