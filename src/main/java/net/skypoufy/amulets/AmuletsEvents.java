package net.skypoufy.amulets;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.skypoufy.amulets.cca.Slot;
import net.skypoufy.amulets.cca.SlotAttacher;
import net.skypoufy.amulets.commands.AmuletsCommand;
import net.skypoufy.amulets.util.UniqueAmuletData;

@Mod.EventBusSubscriber(modid = Amulets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AmuletsEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            SlotAttacher.attach(event);
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        event.getOriginal().reviveCaps();

        event.getEntity().getCapability(Slot.INSTANCE).ifPresent(newCap ->
                event.getOriginal().getCapability(Slot.INSTANCE).ifPresent(newCap::copyFrom)
        );

        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        Slot.register(event);
    }

    @SubscribeEvent
    public static void onClientSetup(RegisterCommandsEvent event){
        AmuletsCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {

        Player player = event.getEntity();
        UniqueAmuletData data = UniqueAmuletData.get(player.level());

        player.getCapability(Slot.INSTANCE).ifPresent(cap -> {

            SimpleContainer inv = cap.getSlot();

            ItemStack stored = data.getUnique(player.getUUID());

            if (!stored.isEmpty()) {
                inv.setItem(0, stored.copy());
            }
        });
    }
}
