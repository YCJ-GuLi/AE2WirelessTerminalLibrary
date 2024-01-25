package de.mari_023.ae2wtlib.curio;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import appeng.menu.locator.MenuLocator;

import de.mari_023.ae2wtlib.wut.WUTHandler;

public final class CurioHelper {
    private CurioHelper() {
    }

    private static ICuriosItemHandler inventory(Player player) {
        return CuriosApi.getCuriosInventory(player).orElseThrow();
    }

    public static boolean isStillPresent(Player player, ItemStack terminal) {
        List<SlotResult> slotResults = inventory(player).findCurios(terminal.getItem());
        for (SlotResult slotResult : slotResults) {
            if (slotResult.stack().equals(terminal)) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getItemStack(Player player, MenuLocator locator) {
        if (locator instanceof CurioLocator curioLocator)
            return curioLocator.locateItem(player);
        return ItemStack.EMPTY;
    }

    @Nullable
    public static MenuLocator findTerminal(Player player, String terminalName) {
        var slotResult = inventory(player).findFirstCurio(
                stack -> WUTHandler.hasTerminal(stack, terminalName));
        if (slotResult.isPresent() && slotResult.get().slotContext() != null) {
            return new CurioLocator(slotResult.get().slotContext());
        }
        return null;
    }

    public static void addAllCurios(List<ItemStack> items, Player player) {
        var inventory = inventory(player).findCurios((stack) -> true);
        for (var slotResult : inventory) {
            items.add(slotResult.stack());
        }
    }
}
