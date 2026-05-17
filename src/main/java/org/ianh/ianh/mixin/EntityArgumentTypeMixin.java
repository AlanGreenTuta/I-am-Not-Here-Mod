package org.ianh.ianh.mixin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.ianh.ianh.IanhItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 拦截直接指定实体名称的命令参数解析。
 * 覆盖命令如：/effect clear Steve, /kill Notch 等。
 */
@Mixin(EntityArgumentType.class)
public abstract class EntityArgumentTypeMixin {

    private static final SimpleCommandExceptionType PLAYER_NOT_FOUND_EXCEPTION =
            new SimpleCommandExceptionType(Text.translatable("argument.entity.notfound.entity"));

    /**
     * 在 getEntity 方法返回前检查实体是否为携带 ianh_item 的玩家，若是则抛出 实体未找到 异常。
     * 目标方法签名：public static Entity getEntity(CommandContext<ServerCommandSource> context, String name)
     */
    @Inject(method = "getEntity", at = @At("RETURN"), cancellable = true)
    private static void checkHiddenPlayer(CommandContext<ServerCommandSource> context, String name,
                                          CallbackInfoReturnable<Entity> cir) throws CommandSyntaxException {
        Entity entity = cir.getReturnValue();
        if (entity instanceof PlayerEntity player && hasHiddenItem(player)) {
            throw PLAYER_NOT_FOUND_EXCEPTION.create();
        }
    }

    /**
     * 检查玩家是否携带 ianh_item 。
     */
    private static boolean hasHiddenItem(PlayerEntity player) {
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