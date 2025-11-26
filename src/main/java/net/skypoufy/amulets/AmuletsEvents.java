package net.skypoufy.amulets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.skypoufy.amulets.cca.Slot;
import net.skypoufy.amulets.cca.SlotAttacher;
import net.skypoufy.amulets.commands.AmuletsCommand;

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
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(Slot.INSTANCE).ifPresent(slotItem -> event.getOriginal().getCapability(Slot.INSTANCE).ifPresent(slotItem1 -> slotItem1.copyFrom(slotItem)));
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        Slot.register(event);
    }

    @SubscribeEvent
    public static void onClientSetup(RegisterCommandsEvent event){
        AmuletsCommand.register(event.getDispatcher());
    }


}
