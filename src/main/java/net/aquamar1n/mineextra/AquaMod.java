package net.aquamar1n.mineextra;

import com.mojang.logging.LogUtils;
import net.aquamar1n.mineextra.config.RedstoneRainConfig;
import net.aquamar1n.mineextra.handlers.RedstoneRainHandler;
import net.aquamar1n.mineextra.registry.ModBlocks;
import net.aquamar1n.mineextra.registry.ModItems;
import net.aquamar1n.mineextra.registry.ModCreativeTabs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
    }

    private void serverSetup(final FMLDedicatedServerSetupEvent event) {
        LOGGER.info("Сервер AquaMod загружен!");
    }
}