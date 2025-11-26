package net.skypoufy.amulets.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.skypoufy.amulets.cca.Slot;

public class AmuletsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("amulets").
                then(Commands.literal("trust").
                        then(Commands.argument("target", EntityArgument.player()).
                                executes(commandContext -> trustCommand(commandContext.getSource(), commandContext.getArgument("target", Player.class)))
                        )
                ).
                then(Commands.literal("dare").
                        then(Commands.argument("target", EntityArgument.player()).
                                executes(commandContext -> dareCommand(commandContext.getSource(), commandContext.getArgument("target", Player.class)))
                        )
                ).
                requires(player -> player.hasPermission(2)).
                then(Commands.literal("add").
                        then(Commands.argument("target", EntityArgument.player()).
                                then(Commands.argument("mod", StringArgumentType.string()).
                                        executes(commandContext -> 0)
                                )
                        )
                ).
                then(Commands.literal("remove").
                        then(Commands.argument("target", EntityArgument.player()).
                                then(Commands.argument("mod", StringArgumentType.string()).
                                        executes(commandContext -> 0)
                                )
                        )
                )
        );
    }

    public static int trustCommand(CommandSourceStack source, Player trusted) {
        Player truster = source.getPlayer();
        truster.getCapability(Slot.INSTANCE).ifPresent(iSlotItemTruster -> {
            trusted.getCapability(Slot.INSTANCE).ifPresent(iSlotItemTrusted -> {
                ItemStack stack = iSlotItemTruster.getSlot().getItem(1);
                iSlotItemTrusted.getSlot().addItem(stack);
            });
        });
        truster.displayClientMessage(Component.translatable("command.trust.success", trusted.getName()), true);
        trusted.displayClientMessage(Component.translatable("command.trusted.success", truster.getName()), true);
        return 1;
    };

    public static int dareCommand(CommandSourceStack source, Player dared) {
        Player darer = source.getPlayer();
        darer.getCapability(Slot.INSTANCE).ifPresent(iSlotItemTruster -> {
            dared.getCapability(Slot.INSTANCE).ifPresent(iSlotItemTrusted -> {
                ItemStack stack = iSlotItemTruster.getSlot().getItem(1);
                iSlotItemTrusted.getSlot().removeItemType(stack.getItem(), 1);
            });
        });
        darer.displayClientMessage(Component.translatable("command.dare.success", dared.getName()), true);
        dared.displayClientMessage(Component.translatable("command.dared.success", darer.getName()), true);
        return 1;
    };
}
