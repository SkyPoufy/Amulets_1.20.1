package net.skypoufy.amulets.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {

    public InventoryScreenMixin(InventoryMenu p_98701_, Inventory p_98702_, Component p_98703_) {
        super(p_98701_, p_98702_, p_98703_);
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void amulets$drawSlot(GuiGraphics context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        int i = this.leftPos + 76;
        int j = this.topPos + 43;
        //context.blit(AbstractContainerScreen.INVENTORY_LOCATION, i, j, 76, 61, 18, 18);
        //context.blit(AbstractContainerScreen.INVENTORY_LOCATION, i, j - 18, 76, 61, 18, 18);
        //context.blit(AbstractContainerScreen.INVENTORY_LOCATION, 0, j, 180, 61, 18, 18);
        //context.blit(AbstractContainerScreen.INVENTORY_LOCATION, 0, j - 18, 180, 61, 18, 18);
    }
}
