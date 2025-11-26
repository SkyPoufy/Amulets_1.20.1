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
import net.skypoufy.amulets.Amulets;
import net.skypoufy.amulets.cca.Slot;
import net.skypoufy.amulets.item.AmuletBaseItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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

            AtomicBoolean hasItem = new AtomicBoolean(false);
            List<String> mods = new ArrayList<>(List.of());

            stacks.stream().forEach(item -> {
                if (item.getItem() instanceof AmuletBaseItem stack) {
                    hasItem.set(true);
                    mods.add(stack.getAllowedMod());
                }
            });


            RecipeManager manager = world.getRecipeManager();
            Optional<CraftingRecipe> recipeOpt = manager.getRecipeFor(RecipeType.CRAFTING, container, world);

            if (recipeOpt.isEmpty()) return;

            ResourceLocation id = recipeOpt.get().getId();

            if (Amulets.mods.contains(id.getNamespace()) && !mods.contains(id.getNamespace())) {
                result.setItem(0, ItemStack.EMPTY);

                ci.cancel();
            }
        });
    }
}
