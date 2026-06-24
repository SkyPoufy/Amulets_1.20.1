package net.skypoufy.amulets.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.skypoufy.amulets.Amulets;

public class AmuletEntityComponents implements EntityComponentInitializer {

    public static final ComponentKey<SlotItem> SLOT = ComponentRegistry.getOrCreate(Amulets.id("slot"), SlotItem.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(AmuletEntityComponents.SLOT, SlotItem::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
