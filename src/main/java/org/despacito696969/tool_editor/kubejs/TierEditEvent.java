package org.despacito696969.tool_editor.kubejs;

import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.resources.ResourceLocation;
import org.despacito696969.tool_editor.ToolEditor;

import java.util.Optional;

public class TierEditEvent extends EventJS {
    private class KubeJSEdit {
        public ToolEditor.ToolEdit edit = new ToolEditor.ToolEdit();

        public KubeJSEdit uses(int x) {
            edit.uses = Optional.of(x);
            return this;
        }

        public KubeJSEdit speed(float x) {
            edit.speed = Optional.of(x);
            return this;
        }

        public KubeJSEdit attackDamageBonus(float x) {
            edit.attackDamageBonus = Optional.of(x);
            return this;
        }

        public KubeJSEdit level(int x) {
            edit.level = Optional.of(x);
            return this;
        }

        public KubeJSEdit enchantmentValue(int x) {
            edit.enchantmentValue = Optional.of(x);
            return this;
        }
    }

    public KubeJSEdit makeEdit(String id) {
        var result = new KubeJSEdit();
        result.edit.id = new ResourceLocation(id);
        ToolEditor.edits.add(result.edit);
        return result;
    }
}
