package net.skypoufy.amulets.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.skypoufy.amulets.cca.Slot;

public class AmuletsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("amulets")
                .then(Commands.literal("trust")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> trustCommand(
                                        ctx.getSource(),
                                        EntityArgument.getPlayer(ctx, "target")
                                ))
                        )
                )
                .then(Commands.literal("dare")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> dareCommand(
                                        ctx.getSource(),
                                        EntityArgument.getPlayer(ctx, "target")
                                ))
                        )
                )
                /*.then(Commands.literal("add")
                        .requires(src -> src.hasPermission(2))   // admin only
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("mod", StringArgumentType.string())
                                        .executes(ctx -> addCommand(
                                                ctx.getSource(),
                                                EntityArgument.getPlayer(ctx, "target"),
                                                StringArgumentType.getString(ctx, "mod")
                                        ))
                                ))
                )*/
                .then(Commands.literal("remove")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.argument("target", EntityArgument.player())

                                // remove ALL
                                .then(Commands.literal("all")
                                        .executes(ctx -> removeCommand(
                                                ctx.getSource(),
                                                EntityArgument.getPlayer(ctx, "target"),
                                                true,   // removeAll = true
                                                -1      // slot ignored
                                        ))
                                )

                                // remove specific slot
                                .then(Commands.argument("slot", IntegerArgumentType.integer(0, 3))
                                        .executes(ctx -> removeCommand(
                                                ctx.getSource(),
                                                EntityArgument.getPlayer(ctx, "target"),
                                                false,  // removeAll = false
                                                IntegerArgumentType.getInteger(ctx, "slot")
                                        ))
                                )
                        )
                )/*
                .then(Commands.literal("list")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> listCommand(
                                        ctx.getSource(),
                                        EntityArgument.getPlayer(ctx, "target")
                                ))
                        )
                )
                */
        );
    }

    /*
    public static int addCommand(CommandSourceStack source, Player target, String modName) {

        ItemStack newUnique = new ItemStack(
                new AmuletBaseItem(new Item.Properties().stacksTo(1), modName)
        );

        UniqueAmuletData data = UniqueAmuletData.get(target.level());
        data.setUnique(target.getUUID(), newUnique);  // SAVE IT

        target.getCapability(Slot.INSTANCE).ifPresent(cap -> {
            SimpleContainer container = cap.getSlot();

            // overwrite slot 0
            container.setItem(0, newUnique.copy());
        });

        source.sendSuccess(() ->
                Component.literal("Set unique amulet for " + target.getName().getString()
                        + " to mod '" + modName + "'"), false);

        return 1;
    }

     */

    public static int removeCommand(CommandSourceStack source, Player target, boolean removeAll, int slot) {

        target.getCapability(Slot.INSTANCE).ifPresent(cap -> {
            SimpleContainer container = cap.getSlot();

            if (removeAll) {
                // Remove ALL
                for (int i = 0; i < container.getContainerSize(); i++) {
                    container.setItem(i, ItemStack.EMPTY);
                }

                source.sendSuccess(
                        () -> Component.translatable("command.removed_all", target.getName().getString()).withStyle(ChatFormatting.GOLD),
                        true
                );
                return;
            }

            // Remove specific slot
            if (slot < 0 || slot >= container.getContainerSize()) {
                source.sendFailure(Component.translatable("command.invalid", slot).withStyle(ChatFormatting.RED));
                return;
            }
            if (container.getItem(slot).isEmpty()) {
                source.sendFailure(Component.translatable("command.slot_empty", slot).withStyle(ChatFormatting.RED));
            }

            container.setItem(slot, ItemStack.EMPTY);

            source.sendSuccess(
                    () -> Component.translatable("command.removed_slot", slot, target.getName().getString()).withStyle(ChatFormatting.GOLD),
                    true
            );
        });

        return 1;
    }

    /*
    public static int removeCommand(CommandSourceStack source, Player target, String modName) {

        UniqueAmuletData data = UniqueAmuletData.get(target.level());
        ItemStack stored = data.getUnique(target.getUUID());

        if (stored.isEmpty() ||
                !(stored.getItem() instanceof AmuletBaseItem amulet) ||
                !amulet.getAllowedMod().equals(modName)) {

            source.sendFailure(Component.literal("Player has no unique amulet for mod '" + modName + "'"));
            return 0;
        }

        // clear saved data
        data.setUnique(target.getUUID(), ItemStack.EMPTY);

        // clear capability slot
        target.getCapability(Slot.INSTANCE).ifPresent(cap -> {
            cap.getSlot().setItem(0, ItemStack.EMPTY);
        });

        source.sendSuccess(() ->
                Component.literal("Removed unique amulet '" + modName + "' from " + target.getName().getString()), false);

        return 1;
    }

     */

    public static int trustCommand(CommandSourceStack source, Player trusted) {
        Player truster = source.getPlayer();

        truster.getCapability(Slot.INSTANCE).ifPresent(trusterCap -> {
            SimpleContainer from = trusterCap.getSlot();
            ItemStack unique = from.getItem(0);

            if (unique.isEmpty()) {
                truster.displayClientMessage(Component.translatable("command.no_amulets").withStyle(ChatFormatting.RED), true);
                return;
            }

            trusted.getCapability(Slot.INSTANCE).ifPresent(trustedCap -> {
                SimpleContainer to = trustedCap.getSlot();

                for (int i = 1; i < to.getContainerSize(); i++) {
                    ItemStack stack = to.getItem(i);
                    if (stack.is(unique.getItem())) {
                        truster.displayClientMessage(Component.translatable("command.amulet_possessed").withStyle(ChatFormatting.RED), true);
                        return;
                    }
                    if (stack.isEmpty()) {
                        to.setItem(i, unique.copy());

                        truster.displayClientMessage(
                                Component.translatable("command.trust.success", trusted.getName()).withStyle(ChatFormatting.GOLD), true);
                        trusted.displayClientMessage(
                                Component.translatable("command.trusted.success", truster.getName()).withStyle(ChatFormatting.GOLD), true);
                        return;
                    }
                }

                truster.displayClientMessage(Component.literal("command.no_slots").withStyle(ChatFormatting.RED), true);
            });
        });

        return 1;
    }

    public static int dareCommand(CommandSourceStack source, Player dared) {
        Player darer = source.getPlayer();

        darer.getCapability(Slot.INSTANCE).ifPresent(darerCap -> {
            SimpleContainer from = darerCap.getSlot();
            ItemStack unique = from.getItem(0);

            if (unique.isEmpty()) {
                darer.displayClientMessage(Component.translatable("command.no_amulets").withStyle(ChatFormatting.RED), true);
                return;
            }

            dared.getCapability(Slot.INSTANCE).ifPresent(daredCap -> {
                SimpleContainer to = daredCap.getSlot();

                for (int i = 1; i < to.getContainerSize(); i++) {
                    if (ItemStack.isSameItemSameTags(to.getItem(i), unique)) {
                        to.removeItem(i, 1);

                        darer.displayClientMessage(
                                Component.translatable("command.dare.success", dared.getName()).withStyle(ChatFormatting.GOLD), true);
                        dared.displayClientMessage(
                                Component.translatable("command.dared.success", darer.getName()).withStyle(ChatFormatting.GOLD), true);
                        return;
                    }
                }

                darer.displayClientMessage(Component.translatable("command.amulet_not_possessed").withStyle(ChatFormatting.RED), true);
            });
        });

        return 1;
    }

    /*
    public static int listCommand(CommandSourceStack source, Player target) {

        return target.getCapability(Slot.INSTANCE).map(cap -> {

            SimpleContainer container = cap.getSlot();

            source.sendSuccess(() ->
                    Component.literal("Amulets of " + target.getName().getString() + ":"), false);

            for (int i = 0; i < container.getContainerSize(); i++) {

                ItemStack stack = container.getItem(i);

                int finalI = i;
                if (stack.isEmpty()) {
                    source.sendSuccess(() ->
                            Component.literal(" - Slot " + finalI + ": [empty]"), false);
                }
                else if (stack.getItem() instanceof AmuletBaseItem amulet) {
                    source.sendSuccess(() ->
                            Component.literal(" - Slot " + finalI + ": " + amulet.getAllowedMod()), false);
                }
                else {
                    source.sendSuccess(() ->
                            Component.literal(" - Slot " + finalI + ": [invalid item]"), false);
                }
            }

            return 1;
        }).orElse(0);
    }

     */
}
