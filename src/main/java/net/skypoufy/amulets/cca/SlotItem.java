package net.skypoufy.amulets.cca;

import net.minecraft.nbt.CompoundTag;
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
        this.stack = stack;
    }

    @Override
    public SimpleContainer getSlot() {
        return this.slot;
    }

    @Override
    public void setSlot(SimpleContainer slot) {
        this.slot = slot;

    }

    @Override
    public void copyFrom(@NotNull ISlotItem slotItem) {
        this.stack = slotItem.getItem();
        this.slot = slotItem.getSlot();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        stack.save(tag);
        tag.put("slot1", this.slot.getItem(0).save(new CompoundTag()));
        tag.put("slot2", this.slot.getItem(1).save(new CompoundTag()));
        tag.put("slot3", this.slot.getItem(2).save(new CompoundTag()));
        tag.put("slot4", this.slot.getItem(3).save(new CompoundTag()));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        stack = ItemStack.of(tag);
        this.slot.setItem(0, ItemStack.of(tag.getCompound("slot1")));
        this.slot.setItem(1, ItemStack.of(tag.getCompound("slot2")));
        this.slot.setItem(2, ItemStack.of(tag.getCompound("slot3")));
        this.slot.setItem(3, ItemStack.of(tag.getCompound("slot4")));
    }
}
