package net.aquamar1n.mineextra.registry;

import net.aquamar1n.mineextra.AquaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AquaMod.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MINE_EXTRA_TAB =
            CREATIVE_TABS.register("mineextra_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mineextra_tab"))
                    .icon(() -> new ItemStack(ModItems.SILVER_INGOT.get()))
                    .displayItems((parameters, output) -> {
                        // Предметы
                        output.accept(ModItems.SILVER_INGOT.get());
                        output.accept(ModItems.SILVER_NUGGET.get());

                        // Блоки как предметы (используем BlockItems)
                        output.accept(ModItems.SILVER_ORE_ITEM.get());
                        output.accept(ModItems.DEEPSLATE_SILVER_ORE_ITEM.get());
                        output.accept(ModItems.SILVER_BLOCK_ITEM.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}