package net.skypoufy.amulets.util;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.skypoufy.amulets.item.AmuletBaseItem;

public class AmuletSlot extends Slot {

    public AmuletSlot(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof AmuletBaseItem;
    }
}
