package net.aquamar1n.mineextra.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.core.particles.DustParticleOptions;

public class BluestoneDustBlock extends RedStoneWireBlock {

    public BluestoneDustBlock(BlockBehaviour.Properties properties) {
        // Наследует всю механику Redstone Dust: размещение, соединение, свойства.
        super(properties);
    }

    // --- Переопределение для постоянной мощности 15 ---

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    // Ключевой метод: всегда возвращает 15, обеспечивая постоянный сигнал.
    @Override
    public int getSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction dir) {
        return 15;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction dir) {
        return 15;
    }

    // --- Анимация частиц (Синий цвет) ---
    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        int power = pState.getValue(RedStoneWireBlock.POWER);

        // Генерируем частицы только если блок визуально включен (power > 0)
        if (power != 0) {
            // Вероятность появления частицы пропорциональна мощности
            if (pRandom.nextFloat() < (float) power / 15.0F) {

                double d0 = (double) pPos.getX() + 0.5D;
                double d1 = (double) pPos.getY() + 0.0625D;
                double d2 = (double) pPos.getZ() + 0.5D;

                // цвета в формате float 0..1 (синий)
                float r = 0.0f;
                float g = 0.5f;
                float b = 1.0f;
                float scale = 1.0f;

                pLevel.addParticle(
                        new DustParticleOptions(255, scale),
                        d0 + (double) pRandom.nextFloat() * 0.5D - 0.25D,
                        d1,
                        d2 + (double) pRandom.nextFloat() * 0.5D - 0.25D,
                        0.0D, 0.0D, 0.0D
                );
            }
        }
    }
}
