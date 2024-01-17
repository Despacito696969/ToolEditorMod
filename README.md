This mod allows to configure tiers per tool. You can do this either via KubeJS or via config file

# Config

In `.minecraft/config/tool_editor.json` you can add your own config.
Example:

```json
[
  {
    "id": "minecraft:diamond_pickaxe",
    "uses": 24242,
    "speed": 42.0,
    "attackDamageBonus": 42.0,
    "level": 2,
    "enchantmentValue": 42
  },
  {
    "id": "minecraft:iron_pickaxe",
    "uses": 4242
  }
]
```

Here diamond pickaxe has all currently supported properties. You can specify edits for as many tools as you want and if you want you can edit only some settings.

# KubeJS

This mod adds one startup event `ToolEditor.edit`

Example: `.minecraft/kubejs/startup_scripts/<your script name>.js`

```js
ToolEditor.edit(e => {
    e.makeEdit("minecraft:diamond_pickaxe")
      .uses(24242)
      .speed(42.0)
      .attackDamageBonus(42.0)
      .level(2)
      .enchantmentValue(42)

    e.makeEdit("minecraft:iron_pickaxe")
      .uses(4242)
})
```
