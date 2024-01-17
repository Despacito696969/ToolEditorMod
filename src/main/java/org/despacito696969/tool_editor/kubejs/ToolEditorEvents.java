package org.despacito696969.tool_editor.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface ToolEditorEvents {
    EventGroup EVENT_GROUP = EventGroup.of("ToolEditor");
    EventHandler EDIT = EVENT_GROUP.startup("edit", () -> TierEditEvent.class);
}
