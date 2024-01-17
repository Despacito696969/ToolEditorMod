package org.despacito696969.tool_editor.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;

public class ToolEditorKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        ToolEditorEvents.EVENT_GROUP.register();
    }

    @Override
    public void initStartup() {
        KubeJSProxy.instance = new LoadedKubeJSProxy();
        KubeJSProxy.instance.fireEdits();
    }
}
