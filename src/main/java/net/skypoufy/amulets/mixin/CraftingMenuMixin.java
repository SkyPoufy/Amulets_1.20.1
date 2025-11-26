package net.skypoufy.amulets.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.skypoufy.amulets.cca.Slot;
import net.skypoufy.amulets.init.AmuletsItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin {

    @Inject(method = "slotChangedCraftingGrid", at = @At("HEAD"), cancellable = true)
    private static void amulets$blockCrafting(AbstractContainerMenu menu, Level world, Player player, CraftingContainer container, ResultContainer result, CallbackInfo ci) {

        player.getCapability(Slot.INSTANCE).ifPresent(iSlotItem -> {
            SimpleContainer slots = iSlotItem.getSlot();

            List<ItemStack> stacks = new ArrayList<>();

            for (int i = 0; i < slots.getContainerSize(); i++) {
                ItemStack stack = slots.getItem(i);
                stacks.add(stack);
            }

            boolean hasItem = stacks.stream().anyMatch(itemStack -> itemStack.is(AmuletsItem.AMULET.get()));

            if (!hasItem) {
                RecipeManager manager = world.getRecipeManager();
                Optional<CraftingRecipe> recipeOpt = manager.getRecipeFor(RecipeType.CRAFTING, container, world);

                if (recipeOpt.isEmpty())
                    return;

                ResourceLocation id = recipeOpt.get().getId();

                if ("create".equals(id.getNamespace())) {

                    result.setItem(0, ItemStack.EMPTY);

                    ci.cancel();
                }
            }
        });
    }
}
