package com.example.testmod.player;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public final class KeyMappings {

    //TODO: make custom key category
    public static final KeyMapping SPELL_WHEEL_KEYMAP = registerKey(InputConstants.KEY_R, "spell_wheel", KeyMapping.CATEGORY_INTERFACE);
    public static final KeyMapping TEST_KEYMAP = registerKey(InputConstants.KEY_G, "test", KeyMapping.CATEGORY_INTERFACE);

    private static KeyMapping registerKey(int keycode, String name, String category) {
        final var key = new KeyMapping("key.testmod." + name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
