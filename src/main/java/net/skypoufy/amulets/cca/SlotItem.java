package net.skypoufy.amulets.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotItem implements AutoSyncedComponent {

    private final Player player;
    private final SimpleContainer slot;

    public SlotItem(Player player) {
        slot = new SimpleContainer(4);
        this.player = player;
    }

    public SimpleContainer getSlot() {
        return this.slot;
    }

    public void setSlot(SimpleContainer container) {
        for (int i = 0; i < slot.getContainerSize(); i++) {
            slot.setItem(i, container.getItem(i).copy());
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        ListTag list = new ListTag();
        for (int i = 0; i < slot.getContainerSize(); i++) {
            list.add(slot.getItem(i).save(new CompoundTag()));
        }
        tag.put("Slots", list);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        ListTag list = tag.getList("Slots", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            this.slot.setItem(i, ItemStack.of(list.getCompound(i)));
        }
    }
}
