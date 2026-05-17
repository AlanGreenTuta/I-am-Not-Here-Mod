package org.ianh.ianh.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.ianh.ianh.IanhItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 拦截 EntitySelector 获取玩家列表的方法，移除携带隐藏物品的玩家。
 * 覆盖命令如：/give @p, /tp @p, /experience add @s 等。
 */
@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin {

    /**
     * 在 getPlayers 方法返回前过滤掉携带 ianh_item 的玩家。
     * 目标方法签名：public List<ServerPlayerEntity> getPlayers(ServerCommandSource source)
     */
    @Inject(method = "getPlayers", at = @At("RETURN"), cancellable = true)
    private void filterHiddenPlayers(ServerCommandSource source, CallbackInfoReturnable<List<ServerPlayerEntity>> cir)
            throws CommandSyntaxException {
        List<ServerPlayerEntity> original = cir.getReturnValue();
        List<ServerPlayerEntity> filtered = original.stream()
                .filter(player -> !hasHiddenItem(player))
                .collect(Collectors.toList());
        cir.setReturnValue(filtered);
    }

    /**
     * 过滤 getEntities 返回的实体列表，移除携带 ianh_item 的玩家。
     * 目标方法：public List<? extends Entity> getEntities(ServerCommandSource source)
     */
    @Inject(method = "getEntities", at = @At("RETURN"), cancellable = true)
    private void filterHiddenEntities(ServerCommandSource source, CallbackInfoReturnable<List<? extends Entity>> cir)
            throws CommandSyntaxException {
        List<? extends Entity> original = cir.getReturnValue();
        List<? extends Entity> filtered = original.stream()
                .filter(entity -> {
                    if (entity instanceof PlayerEntity player) {
                        return !hasHiddenItem(player);
                    }
                    return true;
                })
                .collect(Collectors.toList());
        cir.setReturnValue(filtered);
    }

    /**
     * 检查玩家是否携带 ianh_item 。
     */
    private boolean hasHiddenItem(PlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (stack.isOf(IanhItems.INAH_ITEM)) return true;
        }
        for (ItemStack stack : player.getInventory().offHand) {
            if (stack.isOf(IanhItems.INAH_ITEM)) return true;
        }
        for (ItemStack stack : player.getInventory().armor) {
            if (stack.isOf(IanhItems.INAH_ITEM)) return true;
        }
        return false;
    }
}