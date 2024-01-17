package org.despacito696969.tool_editor.mixin;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import org.despacito696969.tool_editor.ToolEditor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MappedRegistry.class)
public class RegistryFreezeMixin {
    @Inject(method = "freeze()Lnet/minecraft/core/Registry;", at = @At("HEAD"), cancellable = false)
    void freeze(CallbackInfoReturnable<Registry<?>> cir) {
        var registry = (MappedRegistry)(Object)this;
        boolean terminate = false;
        if (registry != BuiltInRegistries.ITEM) {
            return;
        }
        for (var edit : ToolEditor.edits) {
            var item = (Item)registry.get(edit.id);
            if (item instanceof TieredItem tiered_item) {
                var tier = tiered_item.getTier();
                var new_tier = new ToolEditor.BakedTier(tier);

                edit.uses.ifPresent(uses -> new_tier.uses = uses);

                edit.speed.ifPresent(speed -> new_tier.speed = speed);

                edit.attackDamageBonus.ifPresent(attackDamageBonus -> new_tier.attackDamageBonus = attackDamageBonus);

                edit.level.ifPresent(level -> new_tier.level = level);

                edit.enchantmentValue.ifPresent(enchantmentValue -> new_tier.enchantmentValue = enchantmentValue);

                tiered_item.tier = new_tier;
            }
            else {
                ToolEditor.LOGGER.error("item " + edit.id + " is not a TieredItem");
                terminate = true;
            }
        }

        if (terminate) {
            throw new RuntimeException("Some items in Tool Editor config not found");
        }
    }
}
