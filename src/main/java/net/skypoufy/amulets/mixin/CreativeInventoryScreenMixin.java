package net.skypoufy.amulets.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.skypoufy.amulets.util.AmuletSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Shadow private static CreativeModeTab selectedTab;

    public CreativeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu screenHandler, Inventory playerInventory, Component text) {
        super(screenHandler, playerInventory, text);
    }

    @WrapOperation(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;add(Ljava/lang/Object;)Z", ordinal = 2))
    private boolean arsenal$moveSlots(NonNullList instance, Object o, Operation<Boolean> original) {
        if (o instanceof CreativeModeInventoryScreen.SlotWrapper newSlot) {
            Slot slot = ((CreativeSlotAccessor) newSlot).getSlot();
            if (slot instanceof AmuletSlot) {
                return original.call(instance, new CreativeModeInventoryScreen.SlotWrapper(slot, slot.index, (slot.index * 16) - 610, 20));
            }
        }
        return original.call(instance, o);
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void amulets$drawSlots(GuiGraphics context, float p_282504_, int p_282089_, int p_282249_, CallbackInfo ci) {
        if (selectedTab.equals(BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.INVENTORY))) {
            int i = this.leftPos + 126;
            int j = this.topPos + 19;
            context.blit(AbstractContainerScreen.INVENTORY_LOCATION, i, j, 76, 61, 18, 18);
            context.blit(AbstractContainerScreen.INVENTORY_LOCATION, i + 16, j, 76, 61, 18, 18);
            context.blit(AbstractContainerScreen.INVENTORY_LOCATION, i + 32, j, 76, 61, 18, 18);
            context.blit(AbstractContainerScreen.INVENTORY_LOCATION, i + 48, j, 76, 61, 18, 18);
        }

    }
}
