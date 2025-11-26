package net.skypoufy.amulets.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.skypoufy.amulets.Amulets;
import net.skypoufy.amulets.item.AmuletBaseItem;
import net.skypoufy.amulets.item.AmuletCreateItem;

import java.util.function.Supplier;

public class AmuletsItem {

    public static final DeferredRegister<Item> AMULETS_ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, Amulets.MOD_ID);

    public static final RegistryObject<Item> AMULET_CREATE = create("amulet_create", () -> new AmuletCreateItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> AMULET_HEART = create("amulet_heart", () -> new AmuletBaseItem(new Item.Properties().stacksTo(1)).setAllowedMod("sz").addModToModList());

    public static <T extends Item> RegistryObject<T> create(String name, Supplier<T> item) {
        return AMULETS_ITEM.register(name, item);
    }

    public static void init(IEventBus iEventBus) {
        AMULETS_ITEM.register(iEventBus);
    }
}
