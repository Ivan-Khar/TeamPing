package com.aqupd.teamping.setup;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class Registrations {

	public static KeyBinding[] keyBindings;

	public static void init() {
		keyBindings = new KeyBinding[4];
		keyBindings[0] = new KeyBinding("key.aqupd.here", Keyboard.KEY_U, "key.aqupd.categories.teamping");
		keyBindings[1] = new KeyBinding("key.aqupd.danger", Keyboard.KEY_I, "key.aqupd.categories.teamping");
		keyBindings[2] = new KeyBinding("key.aqupd.question", Keyboard.KEY_O, "key.aqupd.categories.teamping");
		keyBindings[3] = new KeyBinding("key.aqupd.no", Keyboard.KEY_P, "key.aqupd.categories.teamping");
		for (KeyBinding keyBinding: keyBindings) {
			ClientRegistry.registerKeyBinding(keyBinding);
		}
	}
}
