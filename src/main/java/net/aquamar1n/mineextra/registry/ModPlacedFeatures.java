package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> SILVER_ORE = ResourceKey.create(Registries.PLACED_FEATURE,
            AquaMod.id("silver_ore"));
    public static final ResourceKey<PlacedFeature> DEEPSLATE_SILVER_ORE = ResourceKey.create(Registries.PLACED_FEATURE,
            AquaMod.id("deepslate_silver_ore"));

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        // Регистрация происходит через JSON
    }
}