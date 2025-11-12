package net.aquamar1n.mineextra.registry;

import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BluestoneDustBlock extends RedStoneWireBlock {
    public BluestoneDustBlock(Properties properties) {
        super(properties);
    }

    // Пока оставляем базовую логику RedstoneWireBlock
    // Позже переопределим методы для постоянной активации
}