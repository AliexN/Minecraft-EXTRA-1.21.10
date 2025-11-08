package net.aquamar1n.mineextra;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.aquamar1n.mineextra.config.RedstoneRainConfig;
import net.aquamar1n.mineextra.handlers.RedstoneRainHandler;
import org.slf4j.Logger;

@Mod(AquaMod.MOD_ID)
public class AquaMod {
    public static final String MOD_ID = "aquamod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AquaMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // Регистрация конфига
        modContainer.registerConfig(ModConfig.Type.COMMON, RedstoneRainConfig.SPEC);

        // Регистрация слушателя событий
        NeoForge.EVENT_BUS.register(RedstoneRainHandler.class);

        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::serverSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Инициализация после конфига
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // Клиент
    }

    private void serverSetup(final FMLDedicatedServerSetupEvent event) {
        // Сервер
    }
}