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

public class AmuletBaseItem extends Item {

    protected String allowedMod;

    public AmuletBaseItem(Properties settings) {
        super(settings);
        addModToModList();
    }

    public AmuletBaseItem addModToModList() {
        if (!Amulets.mods.contains(allowedMod)) {
            Amulets.mods.add(allowedMod);
        }
        return this;
    }

    public AmuletBaseItem setAllowedMod(String allowedMod) {
        this.allowedMod = allowedMod;
        return this;
    };

    public String getAllowedMod() {
        return this.allowedMod;
    };

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltips, TooltipFlag flag) {
        tooltips.add(Component.literal("Mod : " + allowedMod).withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, world, tooltips, flag);
    }



}
