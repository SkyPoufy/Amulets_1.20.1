package net.skypoufy.amulets.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.skypoufy.amulets.Amulets;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AmuletBaseItem extends Item {

    protected List<String> allowedMods;

    public AmuletBaseItem(Properties settings) {
        super(settings);
    }

    public void addModToModList() {
        for (String allowedMod : this.allowedMods) {
            if (!Amulets.mods.contains(allowedMod)) {
                Amulets.mods.add(allowedMod);
            }
        }
    }

    public void setAllowedMods(String... allowedMods) {
        this.allowedMods = List.of(allowedMods);
        this.addModToModList();
    };

    public List<String> getAllowedMods() {
        return this.allowedMods;
    };

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltips, TooltipFlag flag) {
        for (String allowedMod : allowedMods) {
            tooltips.add(Component.literal("Mod : " + allowedMod).withStyle(ChatFormatting.GOLD));
        }
        super.appendHoverText(stack, world, tooltips, flag);
    }



}
