package net.skypoufy.amulets.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UniqueAmuletData extends SavedData {

    private static final String NAME = "unique_amulets";

    private final Map<UUID, ItemStack> data = new HashMap<>();

    public static UniqueAmuletData get(Level level) {
        if (!(level instanceof ServerLevel serverLevel))
            throw new IllegalStateException("Client cannot access world data");

        return serverLevel.getDataStorage().computeIfAbsent(
                UniqueAmuletData::load,
                UniqueAmuletData::new,
                NAME
        );
    }

    public UniqueAmuletData() {}

    public static UniqueAmuletData load(CompoundTag tag) {
        UniqueAmuletData save = new UniqueAmuletData();

        ListTag list = tag.getList("Amulets", Tag.TAG_COMPOUND);

        for (Tag t : list) {
            CompoundTag entry = (CompoundTag)t;

            UUID uuid = entry.getUUID("UUID");
            ItemStack stack = ItemStack.of(entry.getCompound("Item"));

            save.data.put(uuid, stack);
        }

        return save;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();

        for (var entry : data.entrySet()) {
            CompoundTag p = new CompoundTag();
            p.putUUID("UUID", entry.getKey());
            p.put("Item", entry.getValue().save(new CompoundTag()));
            list.add(p);
        }

        tag.put("Amulets", list);
        return tag;
    }

    // --- API ---

    public ItemStack getUnique(UUID uuid) {
        return data.getOrDefault(uuid, ItemStack.EMPTY);
    }

    public void setUnique(UUID uuid, ItemStack stack) {
        data.put(uuid, stack.copy());
        setDirty();
    }
}