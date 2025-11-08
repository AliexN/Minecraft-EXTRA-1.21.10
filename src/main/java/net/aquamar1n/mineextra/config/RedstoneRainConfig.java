package net.aquamar1n.mineextra.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class RedstoneRainConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue ENABLED;
    public static final ModConfigSpec.IntValue CHECK_INTERVAL;
    public static final ModConfigSpec.IntValue CHECKS_PER_TICK;
    public static final ModConfigSpec.DoubleValue DESTROY_CHANCE;
    public static final ModConfigSpec.IntValue CHECK_COOLDOWN;

    static {
        BUILDER.push("Redstone Rain Mechanics");

        //Редстоун может ломаться от дождя? true - да
        ENABLED = BUILDER
                .comment("Enable redstone destruction by rain")
                .define("enabled", true);

        CHECK_INTERVAL = BUILDER
                .comment("Interval between checks (in ticks, 20 ticks = 1 second)")
                .defineInRange("checkInterval", 1, 1, 20);

        CHECKS_PER_TICK = BUILDER
                .comment("Number of positions to check per tick")
                .defineInRange("checksPerTick", 50, 1, 100);

        DESTROY_CHANCE = BUILDER
                .comment("Chance that exposed redstone will be destroyed (0.0 - 1.0)")
                .defineInRange("destroyChance", 1.0, 0.0, 1.0);

        CHECK_COOLDOWN = BUILDER
                .comment("Cooldown for checked positions (in ticks)")
                .defineInRange("checkCooldown", 20, 10, 100);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}