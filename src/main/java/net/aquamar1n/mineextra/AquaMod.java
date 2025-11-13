package net.aquamar1n.mineextra;

import com.mojang.logging.LogUtils;
import net.aquamar1n.mineextra.config.RedstoneRainConfig;
import net.aquamar1n.mineextra.handlers.RedstoneRainHandler;
import net.aquamar1n.mineextra.registry.ModBlocks;
import net.aquamar1n.mineextra.registry.ModItems;
import net.aquamar1n.mineextra.registry.ModCreativeTabs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import net.minecraft.client.renderer.RenderType;

@Mod(AquaMod.MOD_ID)
public class AquaMod {
    public static final String MOD_ID = "aquamod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AquaMod(IEventBus modEventBus, ModContainer modContainer) {
        try {
            modContainer.registerConfig(ModConfig.Type.COMMON, RedstoneRainConfig.SPEC);

            NeoForge.EVENT_BUS.register(RedstoneRainHandler.class);

            ModBlocks.register(modEventBus);
            ModItems.register(modEventBus);
            ModCreativeTabs.register(modEventBus);

            modEventBus.addListener(this::commonSetup);
            modEventBus.addListener(this::clientSetup);

            // listener для регистрации блоковых цветов (уже был)
            modEventBus.addListener(this::registerBlockColors);

            modEventBus.addListener(this::serverSetup);

        } catch (Exception e) {
            LOGGER.error("Error in AquaMod constructor", e);
            throw e;
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Инициализация AquaMod...");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Клиент AquaMod загружен!");
        // здесь можно инициализировать прочие клиентские вещи, если нужно
    }

    private void serverSetup(final FMLDedicatedServerSetupEvent event) {
        LOGGER.info("Сервер AquaMod загружен!");
    }

    private void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {

            int power = state.getValue(RedStoneWireBlock.POWER);

            float ratio = (float)power / 15.0F;
            int color_low = 0x000044;
            int color_high = 0x0078FF;

            int r1 = (color_low >> 16) & 0xFF;
            int g1 = (color_low >> 8) & 0xFF;
            int b1 = color_low & 0xFF;

            int r2 = (color_high >> 16) & 0xFF;
            int g2 = (color_high >> 8) & 0xFF;
            int b2 = color_high & 0xFF;

            int r = (int) (r1 + (r2 - r1) * ratio);
            int g = (int) (g1 + (g2 - g1) * ratio);
            int b = (int) (b1 + (b2 - b1) * ratio);

            return r << 16 | g << 8 | b;

        }, ModBlocks.BLUESTONE_DUST.get());
    }
}
