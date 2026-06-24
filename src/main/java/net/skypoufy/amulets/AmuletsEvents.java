package net.skypoufy.amulets;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.skypoufy.amulets.commands.AmuletsCommand;

@Mod.EventBusSubscriber(modid = Amulets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AmuletsEvents {

    @SubscribeEvent
    public static void onClientSetup(RegisterCommandsEvent event){
        AmuletsCommand.register(event.getDispatcher());
    }
}
