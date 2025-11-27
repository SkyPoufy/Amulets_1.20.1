package net.skypoufy.amulets.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.skypoufy.amulets.Amulets;

public class AmuletsCreativeModTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Amulets.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_MODE_TAB = CREATIVE_MOD_TABS.register("amulets",
            () -> CreativeModeTab.builder()
                    .icon(() -> AmuletsItem.AMULET_CREATE.get().getDefaultInstance())
                    .title(Component.translatable("tabs.amulets"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(AmuletsItem.AMULET_CREATE.get());
                        output.accept(AmuletsItem.AMULET_HEART.get());
                    })
                    .build());

    public static void init(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }
}
