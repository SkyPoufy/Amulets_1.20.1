package net.skypoufy.amulets.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AmuletPlaneItem extends AmuletBaseItem {

    public AmuletPlaneItem(Properties settings) {
        super(settings);
        this.setAllowedMods("firma_aircraft", "immersive_aircraft");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltips, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltips, flag);
        tooltips.add(Component.literal("For those who want to explore the world in new ways").withStyle(ChatFormatting.GREEN));
    }
}
