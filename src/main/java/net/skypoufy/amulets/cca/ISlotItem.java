package net.skypoufy.amulets.cca;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

public interface ISlotItem extends INBTSerializable<CompoundTag> {

    ItemStack getItem();

    void setItem(ItemStack stack);

    SimpleContainer getSlot();

    void setSlot(SimpleContainer slot);

    void copyFrom(@NotNull ISlotItem slotItem);
}

