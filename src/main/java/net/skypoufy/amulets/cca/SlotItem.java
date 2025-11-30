package net.skypoufy.amulets.cca;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotItem implements ISlotItem {

    private ItemStack stack = ItemStack.EMPTY;
    private SimpleContainer slot = new SimpleContainer(4);

    @Override
    public ItemStack getItem() {
        return this.stack;
    }

    @Override
    public void setItem(ItemStack stack) {
        this.stack = stack.copy();
    }

    @Override
    public SimpleContainer getSlot() {
        return this.slot;
    }

    @Override
    public void setSlot(SimpleContainer container) {
        for (int i = 0; i < slot.getContainerSize(); i++) {
            slot.setItem(i, container.getItem(i).copy());
        }
    }

    @Override
    public void copyFrom(@NotNull ISlotItem slotItem) {
        this.stack = slotItem.getItem().copy();
        SimpleContainer slots = slotItem.getSlot();
        for (int i = 0; i < slots.getContainerSize(); i++) {
            this.slot.setItem(i, slots.getItem(i).copy());
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.put("Item", stack.save(new CompoundTag()));

        ListTag list = new ListTag();
        for (int i = 0; i < slot.getContainerSize(); i++) {
            list.add(slot.getItem(i).save(new CompoundTag()));
        }
        tag.put("Slots", list);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.stack = ItemStack.of(tag.getCompound("Item"));

        ListTag list = tag.getList("Slots", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            this.slot.setItem(i, ItemStack.of(list.getCompound(i)));
        }
    }
}
