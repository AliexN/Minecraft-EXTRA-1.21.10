package net.aquamar1n.mineextra.handlers;

import net.aquamar1n.mineextra.AquaMod;
import net.aquamar1n.mineextra.config.RedstoneRainConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = AquaMod.MOD_ID)
public class RedstoneRainHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedstoneRainHandler.class);
    private static final Map<BlockPos, Long> CHECK_COOLDOWN_MAP = new HashMap<>();
    private static final Map<BlockPos, Boolean> KNOWN_REDSTONE_POSITIONS = new HashMap<>();
    private static long lastCacheClear = 0;

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        long currentTime = level.getGameTime();

        // Очищаем кэш каждые 5 минут или если размер превышает 5000
        if (currentTime - lastCacheClear > 6000 || KNOWN_REDSTONE_POSITIONS.size() > 5000) {
            KNOWN_REDSTONE_POSITIONS.clear();
            lastCacheClear = currentTime;
            LOGGER.debug("Cleared redstone position cache");
        }

        if (RedstoneRainConfig.ENABLED.get() && level.isRaining() && currentTime % RedstoneRainConfig.CHECK_INTERVAL.get() == 0) {
            LOGGER.debug("Rain tick - checking for exposed redstone");
            checkForExposedRedstone(level);
        }
    }

    private static void checkForExposedRedstone(ServerLevel level) {
        long currentTime = level.getGameTime();
        int cooldownTicks = RedstoneRainConfig.CHECK_COOLDOWN.get();

        // Очищаем старые записи из кэша кулдауна
        CHECK_COOLDOWN_MAP.entrySet().removeIf(entry -> currentTime - entry.getValue() > cooldownTicks);

        // Ограничиваем размер мапы
        if (CHECK_COOLDOWN_MAP.size() > 8000) {
            CHECK_COOLDOWN_MAP.clear();
            LOGGER.warn("Cleared cooldown map to prevent memory issues");
        }

        List<ServerPlayer> players = level.players();
        if (players.isEmpty()) {
            return;
        }

        int checksPerTick = RedstoneRainConfig.CHECKS_PER_TICK.get();
        // Лимит на проверки по количеству игроков (max 50 на игрока)
        int totalChecks = Math.min(checksPerTick, players.size() * 50);
        int checksDone = 0;

        // Сначала проверяем известные позиции с редстоуном
        if (!KNOWN_REDSTONE_POSITIONS.isEmpty()) {
            var iterator = KNOWN_REDSTONE_POSITIONS.entrySet().iterator();
            while (iterator.hasNext() && checksDone < totalChecks) {
                var entry = iterator.next();
                BlockPos knownPos = entry.getKey();

                if (CHECK_COOLDOWN_MAP.containsKey(knownPos)) {
                    continue;
                }

                CHECK_COOLDOWN_MAP.put(knownPos, currentTime);
                checksDone++;

                BlockState state = level.getBlockState(knownPos);
                if (state.getBlock() instanceof RedStoneWireBlock) {
                    if (isRedstoneExposedToRain(level, knownPos)) {
                        destroyRedstone(level, knownPos);
                        iterator.remove();
                    }
                } else {
                    iterator.remove();
                }
            }
        }

        // Затем случайные проверки для поиска нового редстоуна
        while (checksDone < totalChecks) {
            BlockPos randomPos = getRandomSurfacePosition(level);
            if (randomPos.equals(BlockPos.ZERO)) {
                continue;
            }

            if (CHECK_COOLDOWN_MAP.containsKey(randomPos)) {
                continue;
            }

            CHECK_COOLDOWN_MAP.put(randomPos, currentTime);
            checksDone++;

            BlockState state = level.getBlockState(randomPos);
            if (state.getBlock() instanceof RedStoneWireBlock) {
                KNOWN_REDSTONE_POSITIONS.put(randomPos, true);

                if (isRedstoneExposedToRain(level, randomPos)) {
                    destroyRedstone(level, randomPos);
                    KNOWN_REDSTONE_POSITIONS.remove(randomPos);
                }
            }
        }
    }

    private static BlockPos getRandomSurfacePosition(ServerLevel level) {
        List<ServerPlayer> players = level.players();
        if (players.isEmpty()) return BlockPos.ZERO;

        ServerPlayer randomPlayer = players.get(level.random.nextInt(players.size()));
        BlockPos playerPos = randomPlayer.blockPosition();

        // Уменьшили радиус до 256 для оптимизации
        int x = playerPos.getX() + level.random.nextInt(257) - 128;
        int z = playerPos.getZ() + level.random.nextInt(257) - 128;

        int surfaceY = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(x, 0, z)).getY();

        int y = surfaceY + level.random.nextInt(7) - 3;
        y = Math.max(level.getMinY(), Math.min(level.getMaxY(), y));

        return new BlockPos(x, y, z);
    }

    private static boolean isRedstoneExposedToRain(ServerLevel level, BlockPos pos) {
        if (!level.canSeeSky(pos)) {
            return false;
        }

        if (level.isRainingAt(pos.above())) {
            return true;
        }

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (level.isRainingAt(pos.above().offset(x, 0, z))) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void destroyRedstone(ServerLevel level, BlockPos pos) {
        if (level.random.nextDouble() < RedstoneRainConfig.DESTROY_CHANCE.get()) {
            level.destroyBlock(pos, true);
            LOGGER.info("Destroyed redstone at " + pos);
            checkAndDestroyAdjacentRedstone(level, pos);
        }
    }

    private static void checkAndDestroyAdjacentRedstone(ServerLevel level, BlockPos centerPos) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos adjacentPos = centerPos.offset(x, y, z);
                    BlockState adjacentState = level.getBlockState(adjacentPos);

                    if (adjacentState.getBlock() instanceof RedStoneWireBlock) {
                        if (isRedstoneExposedToRain(level, adjacentPos)) {
                            level.destroyBlock(adjacentPos, true);
                            KNOWN_REDSTONE_POSITIONS.remove(adjacentPos);
                            LOGGER.info("Destroyed adjacent redstone at " + adjacentPos);
                        }
                    }
                }
            }
        }
    }
}