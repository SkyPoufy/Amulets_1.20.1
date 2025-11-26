package net.skypoufy.amulets.cca;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.skypoufy.amulets.Amulets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlotAttacher {

    private static class SlotProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation ID = new ResourceLocation(Amulets.MOD_ID, "slot");

        private final ISlotItem backend = new SlotItem();
        private final LazyOptional<ISlotItem> optionalData = LazyOptional.of(() -> backend);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
            return Slot.INSTANCE.orEmpty(capability, this.optionalData);
        }

        void invalidate() {
            this.optionalData.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        SlotProvider provider = new SlotProvider();
        event.addCapability(SlotProvider.ID, provider);
    }

    private SlotAttacher() {
    }
}
