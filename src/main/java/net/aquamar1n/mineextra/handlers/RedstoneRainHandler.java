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
import net.minecraft.world.level.levelgen.Heightmap;

@EventBusSubscriber(modid = AquaMod.MOD_ID)
public class RedstoneRainHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedstoneRainHandler.class);
    private static final Map<BlockPos, Long> CHECK_COOLDOWN_MAP = new HashMap<>();

    // Кэш для быстрого доступа к известным позициям с редстоуном
    private static final Map<BlockPos, Boolean> KNOWN_REDSTONE_POSITIONS = new HashMap<>();
    private static long lastCacheClear = 0;

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        // Очищаем кэш каждые 2 минуты
        long currentTime = level.getGameTime();
        if (currentTime - lastCacheClear > 2400) {
            KNOWN_REDSTONE_POSITIONS.clear();
            lastCacheClear = currentTime;
            LOGGER.debug("Cleared redstone position cache");
        }

        if (RedstoneRainConfig.ENABLED.get() && level.isRaining()) {
            // Проверяем каждый тик (убрали интервал)
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
        int checksDone = 0;

        // Сначала проверяем известные позиции с редстоуном (более эффективно)
        if (!KNOWN_REDSTONE_POSITIONS.isEmpty()) {
            var iterator = KNOWN_REDSTONE_POSITIONS.entrySet().iterator();
            while (iterator.hasNext() && checksDone < checksPerTick) {
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
                        // Удаляем из кэша после разрушения
                        iterator.remove();
                    }
                } else {
                    // Редстоун исчез - удаляем из кэша
                    iterator.remove();
                }
            }
        }

        // Затем делаем случайные проверки для поиска нового редстоуна
        while (checksDone < checksPerTick) {
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
                // Добавляем в кэш известных позиций
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

        // Увеличиваем радиус и добавляем вертикальную вариацию
        int x = playerPos.getX() + level.random.nextInt(500) - 250;
        int z = playerPos.getZ() + level.random.nextInt(500) - 250;

        // Получаем высоту поверхности
        int surfaceY = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(x, 0, z)).getY();

        // Добавляем случайное смещение по Y (±3 блока от поверхности)
        int y = surfaceY + level.random.nextInt(7) - 3;
        y = Math.max(level.getMinY(), Math.min(level.getMaxY(), y));

        return new BlockPos(x, y, z);
    }

    private static boolean isRedstoneExposedToRain(ServerLevel level, BlockPos pos) {
        // Более либеральная проверка - учитываем больше условий
        if (!level.canSeeSky(pos)) {
            return false;
        }

        // Проверяем, идет ли дождь в этой позиции или рядом
        if (level.isRainingAt(pos.above())) {
            return true;
        }

        // Также проверяем соседние позиции выше
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
        // Всегда уничтожаем (шанс 100%)
        level.destroyBlock(pos, true);
        LOGGER.info("Destroyed redstone at " + pos);

        // Немедленно проверяем и разрушаем соседний редстоун
        checkAndDestroyAdjacentRedstone(level, pos);
    }

    private static void checkAndDestroyAdjacentRedstone(ServerLevel level, BlockPos centerPos) {
        // Проверяем все соседние блоки в радиусе 2 блоков
        for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos adjacentPos = centerPos.offset(x, y, z);
                    BlockState adjacentState = level.getBlockState(adjacentPos);

                    if (adjacentState.getBlock() instanceof RedStoneWireBlock) {
                        if (isRedstoneExposedToRain(level, adjacentPos)) {
                            // Немедленно уничтожаем без дополнительных проверок
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