package net.aquamar1n.mineextra;

import com.mojang.logging.LogUtils;
import net.aquamar1n.mineextra.config.RedstoneRainConfig;
import net.aquamar1n.mineextra.handlers.RedstoneRainHandler;
import net.aquamar1n.mineextra.registry.ModBlocks;
import net.aquamar1n.mineextra.registry.ModItems;
import net.aquamar1n.mineextra.registry.ModCreativeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

@Mod(AquaMod.MOD_ID)
public class AquaMod {
    public static final String MOD_ID = "aquamod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AquaMod(IEventBus modEventBus, ModContainer modContainer) {
        try {
            // весь код конструктора
        } catch (Exception e) {
            LOGGER.error("Error in AquaMod constructor", e);
            throw e; // чтобы краш показал stacktrace
        }

        modContainer.registerConfig(ModConfig.Type.COMMON, RedstoneRainConfig.SPEC);

        // === Регистрация обработчиков событий ===
        NeoForge.EVENT_BUS.register(RedstoneRainHandler.class);

        // === ПРАВИЛЬНЫЙ ПОРЯДОК РЕГИСТРАЦИИ ===
        // 1. Сначала регистрируем блоки
        ModBlocks.register(modEventBus);

        // 2. Потом регистрируем предметы (включая BlockItems)
        ModItems.register(modEventBus);

        // 3. Затем креативные вкладки (если они используют блоки/предметы)
        ModCreativeTabs.register(modEventBus);

        // === Обработчики событий ===
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::serverSetup);
    }

    /** Утилита для создания ResourceLocation */
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    // === Общая инициализация ===
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Инициализация AquaMod...");
    }

    // === Клиент ===
    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Клиент AquaMod загружен!");
    }

    // === Сервер ===
    private void serverSetup(final FMLDedicatedServerSetupEvent event) {
        LOGGER.info("Сервер AquaMod загружен!");
    }
}