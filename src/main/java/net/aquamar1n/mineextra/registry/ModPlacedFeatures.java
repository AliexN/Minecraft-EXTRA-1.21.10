package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> SILVER_ORE_PLACED_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(AquaMod.MOD_ID, "silver_ore_placed"));

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var holder = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(SILVER_ORE_PLACED_KEY, new PlacedFeature(
                holder.getOrThrow(ModConfiguredFeatures.SILVER_ORE_KEY),
                List.of(
                        CountPlacement.of(10), // количество жил
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)),
                        BiomeFilter.biome()
                )
        ));
    }
}
