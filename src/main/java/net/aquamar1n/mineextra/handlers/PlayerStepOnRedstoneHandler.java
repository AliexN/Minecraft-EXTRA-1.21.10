package net.aquamar1n.mineextra.handlers;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AquaMod.MOD_ID)
public class PlayerStepOnRedstoneHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return; // Хотя событие только для игроков, на всякий случай
        }

        if (player.isCrouching()) {
            return; // Не разрушаем, если игрок присел
        }

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        // Позиция под ногами
        BlockPos posBelow = player.blockPosition().below();
        BlockState state = level.getBlockState(posBelow);

        if (state.getBlock() instanceof RedStoneWireBlock) {
            level.destroyBlock(posBelow, true);
            return;
        }

        // Проверка позиции ног (если отличается)
        BlockPos feetPos = BlockPos.containing(player.getX(), player.getY(), player.getZ());
        if (!feetPos.equals(posBelow)) {
            BlockState feetState = level.getBlockState(feetPos);
            if (feetState.getBlock() instanceof RedStoneWireBlock) {
                level.destroyBlock(feetPos, true);
            }
        }
    }
}