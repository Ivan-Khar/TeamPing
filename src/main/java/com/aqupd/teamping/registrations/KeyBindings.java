package com.aqupd.teamping.registrations;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
  private KeyBindings() {}
  public static KeyBindings get = new KeyBindings();

  public KeyBinding[] keyBindings;
  public void initialize() {
    keyBindings = new KeyBinding[3];
    keyBindings[0] = new KeyBinding("key.aqupd.menu", Keyboard.KEY_F, "key.aqupd.categories.teamping");
    keyBindings[1] = new KeyBinding("key.aqupd.reset", Keyboard.KEY_U, "key.aqupd.categories.teamping");
    keyBindings[2] = new KeyBinding("key.aqupd.party", Keyboard.KEY_SEMICOLON, "key.aqupd.categories.teamping");
    for (KeyBinding keyBinding: keyBindings) ClientRegistry.registerKeyBinding(keyBinding);
  }
}
