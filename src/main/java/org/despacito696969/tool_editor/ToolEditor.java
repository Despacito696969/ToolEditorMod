package org.despacito696969.tool_editor;

import com.google.gson.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

public class ToolEditor implements ModInitializer {
    public static class BakedTier implements Tier {
        public int uses;
        public float speed;
        public float attackDamageBonus;
        public int level;
        public int enchantmentValue;
        public Ingredient repairIngredient;

        public BakedTier(Tier tier) {
            uses = tier.getUses();
            speed = tier.getSpeed();
            attackDamageBonus = tier.getAttackDamageBonus();
            level = tier.getLevel();
            enchantmentValue = tier.getEnchantmentValue();
            repairIngredient = tier.getRepairIngredient();
        }

        @Override
        public int getUses() {
            return uses;
        }

        @Override
        public float getSpeed() {
            return speed;
        }

        @Override
        public float getAttackDamageBonus() {
            return attackDamageBonus;
        }

        @Override
        public int getLevel() {
            return level;
        }

        @Override
        public int getEnchantmentValue() {
            return enchantmentValue;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return repairIngredient;
        }
    }

    public static class ToolEdit {
        public ResourceLocation id;
        public Optional<Integer> uses = Optional.empty();
        public Optional<Float> speed = Optional.empty();
        public Optional<Float> attackDamageBonus = Optional.empty();
        public Optional<Integer> level = Optional.empty();
        public Optional<Integer> enchantmentValue = Optional.empty();
    }

    public static final String mod_id = "tool_editor";
    public static final String CONFIG_FILE_NAME = "tool_editor.json";
    public static final Logger LOGGER = LoggerFactory.getLogger(mod_id);
    public static ArrayList<ToolEdit> edits = new ArrayList<>();

    boolean did_error_out = false;
    private void fatal_error(String str) {
        LOGGER.error(str);
        did_error_out = true;
    }

    public void inner_init() {
        Path config_path = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME);

        try (var reader = Files.newBufferedReader(config_path, StandardCharsets.UTF_8)) {
            try {
                var element = JsonParser.parseReader(reader);
                var array = element.getAsJsonArray();
                if (!element.isJsonArray()) {
                    fatal_error("Expected array in config");
                }
                else {
                    for (var elem : array) {
                        edit_tool(elem);
                    }
                }
            } catch (Exception e) {
                fatal_error(e.getMessage());
            }
        }
        catch (NoSuchFileException e) {
            LOGGER.error("No config found (./minecraft/config/" + CONFIG_FILE_NAME + ")");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onInitialize() {
        // inner_init();

        /*if (did_error_out) {
            throw new RuntimeException("Could not load Tool Editor mod because of invalid config");
        }*/
    }

    public void log_error_wrong_field(JsonObject obj, String key, String type) {
        fatal_error(obj + " should have key: [" + key + "] of type " + type);
    }

    @Nullable
    public JsonPrimitive get_field_prim(JsonObject obj, String key) {
        var field = obj.get(key);
        if (field == null) {
            return null;
        }
        obj.remove(key);
        if (!field.isJsonPrimitive()) {
            return null;
        }
        return field.getAsJsonPrimitive();
    }

    @Nullable
    public String get_field_str(JsonObject obj, String key) {
        var prim = get_field_prim(obj, key);
        if (prim == null) {
            return null;
        }
        if (!prim.isString()) {
            log_error_wrong_field(obj, key, "string");
            return null;
        }
        return prim.getAsString();
    }

    @Nullable
    public Integer get_field_int(JsonObject obj, String key) {
        var prim = get_field_prim(obj, key);
        if (prim == null) {
            return null;
        }
        if (!prim.isNumber()) {
            log_error_wrong_field(obj, key, "int");
            return null;
        }
        try {
            return prim.getAsInt();
        }
        catch (NumberFormatException e) {
            log_error_wrong_field(obj, key, "int");
            return null;
        }
    }

    @Nullable
    public Float get_field_float(JsonObject obj, String key) {
        var prim = get_field_prim(obj, key);
        if (prim == null) {
            return null;
        }
        if (!prim.isNumber()) {
            return null;
        }
        try {
            log_error_wrong_field(obj, key, "float");
            return prim.getAsFloat();
        }
        catch (NumberFormatException e) {
            log_error_wrong_field(obj, key, "float");
            return null;
        }
    }

    public void edit_tool(JsonElement elem) {
        if (!elem.isJsonObject()) {
            fatal_error("Expected an object, got: " + elem);
            return;
        }

        var obj = elem.getAsJsonObject();

        var id = get_field_str(obj, "id");

        if (id == null) {
            fatal_error("Object has to contain 'id' of type string: " + obj);
            return;
        }

        var edit = new ToolEdit();
        edit.id = new ResourceLocation(id.toString());

        edit.uses = Optional.ofNullable(get_field_int(obj, "uses"));
        edit.level = Optional.ofNullable(get_field_int(obj, "level"));
        edit.speed = Optional.ofNullable(get_field_float(obj, "speed"));
        edit.attackDamageBonus = Optional.ofNullable(get_field_float(obj, "attackDamageBonus"));
        edit.enchantmentValue = Optional.ofNullable(get_field_int(obj, "enchantmentValue"));

        edits.add(edit);

        for (var key : obj.keySet()) {
            fatal_error("Found unnecessary key [" + key + "] in: " + obj);
        }
    }
}
