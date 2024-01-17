package org.despacito696969.tool_editor.kubejs;

public class LoadedKubeJSProxy extends KubeJSProxy {
    @Override
    public void fireEdits() {
        ToolEditorEvents.EDIT.post(new TierEditEvent());
    }
}
