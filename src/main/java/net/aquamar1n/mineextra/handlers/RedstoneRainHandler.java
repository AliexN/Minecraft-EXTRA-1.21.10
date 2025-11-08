package net.aquamar1n.mineextra.handlers;

import net.aquamar1n.mineextra.AquaMod;
import net.aquamar1n.mineextra.config.RedstoneRainConfig;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = AquaMod.MOD_ID)
public class RedstoneRainHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedstoneRainHandler.class);

    private static final Map<BlockPos, Long> CHECK_COOLDOWN_MAP = new HashMap<>();

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        if (RedstoneRainConfig.ENABLED.get()) {
            if (level.isRaining() && level.getGameTime() % RedstoneRainConfig.CHECK_INTERVAL.get() == 0) {
                LOGGER.info("Rain tick triggered - checking for exposed redstone");
                checkForExposedRedstone(level);
            }
        }
    }

    private static void checkForExposedRedstone(ServerLevel level) {
        long currentTime = level.getGameTime();

        int cooldownTicks = RedstoneRainConfig.CHECK_COOLDOWN.get();
        CHECK_COOLDOWN_MAP.entrySet().removeIf(entry -> currentTime - entry.getValue() > (long) cooldownTicks);

        List<ServerPlayer> players = level.players();

        if (CHECK_COOLDOWN_MAP.size() > 5000) {  // Limit map
            CHECK_COOLDOWN_MAP.clear();
            LOGGER.warn("Cleared cooldown map to prevent memory issues");
        }
        if (players.isEmpty()) {
            LOGGER.warn("No players - skipping check");
            return;
        }

        for (int i = 0; i < RedstoneRainConfig.CHECKS_PER_TICK.get(); i++) {
            BlockPos randomPos = getRandomSurfacePosition(level);
            if (randomPos.equals(BlockPos.ZERO)) {
                LOGGER.debug("Zero pos - skipping");
                continue;
            }

            LOGGER.debug("Checking pos: " + randomPos);  // Debug: Какая pos проверяется

            if (CHECK_COOLDOWN_MAP.containsKey(randomPos)) {
                LOGGER.debug("Pos in cooldown: " + randomPos);
                continue;
            }

            CHECK_COOLDOWN_MAP.put(randomPos, currentTime);

            BlockState state = level.getBlockState(randomPos);
            if (state.getBlock() instanceof RedStoneWireBlock) {
                LOGGER.info("Found redstone wire at " + randomPos);  // Debug: Нашёл редстоун
                if (isRedstoneExposedToRain(level, randomPos)) {
                    LOGGER.info("Exposed redstone found at " + randomPos + " - destroying");
                    destroyRedstone(level, randomPos);
                } else {
                    LOGGER.debug("Redstone at " + randomPos + " - not exposed (has roof or no rain)");
                }
            } else {
                LOGGER.debug("Pos " + randomPos + " - not redstone wire");
            }
        }
    }

    private static BlockPos getRandomSurfacePosition(ServerLevel level) {
        List<ServerPlayer> players = level.players();
        if (players.isEmpty()) return BlockPos.ZERO;

        ServerPlayer randomPlayer = players.get(level.random.nextInt(players.size()));
        BlockPos playerPos = randomPlayer.blockPosition();

        int x = playerPos.getX() + level.random.nextInt(400) - 200;  // Увеличил range to 200 for better chance
        int z = playerPos.getZ() + level.random.nextInt(400) - 200;

        int y = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(x, 0, z)).getY();  // Surface y

        return new BlockPos(x, y, z);  // y for surface blocks like redstone
    }

    private static boolean isRedstoneExposedToRain(ServerLevel level, BlockPos pos) {
        if (level.canSeeSky(pos) && level.isRainingAt(pos.above())) {
            return true;
        }
        return false;
    }

    private static void destroyRedstone(ServerLevel level, BlockPos pos) {
        if (level.random.nextDouble() < RedstoneRainConfig.DESTROY_CHANCE.get()) {
            level.destroyBlock(pos, true);
            LOGGER.info("Destroyed redstone at " + pos);
        } else {
            LOGGER.debug("Chance failed - not destroying " + pos);
        }
    }
}