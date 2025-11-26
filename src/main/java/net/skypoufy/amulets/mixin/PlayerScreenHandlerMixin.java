package net.skypoufy.amulets.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraftforge.common.util.LazyOptional;
import net.skypoufy.amulets.cca.ISlotItem;
import net.skypoufy.amulets.cca.Slot;
import net.skypoufy.amulets.util.AmuletSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class PlayerScreenHandlerMixin extends RecipeBookMenu<TransientCraftingContainer> {

    public PlayerScreenHandlerMixin(MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void amulets$init(Inventory inventory, boolean onServer, Player owner, CallbackInfo ci) {

        LazyOptional<ISlotItem> capability = inventory.player.getCapability(Slot.INSTANCE);
        capability.ifPresent(slot -> {
            //this.addSlot(new AmuletteSlot(slot.getSlot(), 0, 77, 44));
            //this.addSlot(new AmuletteSlot(slot.getSlot(), 1, 77, 26));
            this.addSlot(new AmuletSlot(slot.getSlot(), 0, 180, 8));
            this.addSlot(new AmuletSlot(slot.getSlot(), 1, 180, 26));
            this.addSlot(new AmuletSlot(slot.getSlot(), 2, 180, 44));
            this.addSlot(new AmuletSlot(slot.getSlot(), 3, 180, 60));
        });
        //this.addSlot(new AmuletteSlot(new SimpleContainer(1), 0, 77, 26));
    }
}
