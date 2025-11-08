package net.aquamar1n.mineextra.handlers;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = AquaMod.MOD_ID)
public class PlayerStepOnRedstoneHandler {

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        // Проверяем, не присел ли игрок
        if (player.isCrouching()) {
            return;
        }

        // Получаем позицию под игроком
        BlockPos posBelow = player.blockPosition().below();
        BlockState state = level.getBlockState(posBelow);

        // Проверяем, стоит ли игрок на редстоуне
        if (state.getBlock() instanceof RedStoneWireBlock) {
            // Ломаем редстоун
            level.destroyBlock(posBelow, true);
            return;
        }

        // Дополнительная проверка для позиции, где находятся ноги игрока
        BlockPos feetPos = BlockPos.containing(player.getX(), player.getY(), player.getZ());
        if (!feetPos.equals(posBelow)) {
            BlockState feetState = level.getBlockState(feetPos);
            if (feetState.getBlock() instanceof RedStoneWireBlock) {
                level.destroyBlock(feetPos, true);
            }
        }
    }
}