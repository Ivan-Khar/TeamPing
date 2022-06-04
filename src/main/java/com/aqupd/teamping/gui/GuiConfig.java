package com.aqupd.teamping.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.util.Configuration.*;

public class GuiConfig extends GuiScreen {

	GuiButton buttons;
	GuiSlider sliders;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GL11.glColor4f(1, 1, 1, 1);
		mc.renderEngine.bindTexture(new ResourceLocation(MOD_ID, "textures/gui/texture.png"));

		drawCenteredString(
			Minecraft.getMinecraft().fontRendererObj,
			I18n.format("config.aqupd.position"),
			width / 2 + 75 + 2,
			height - 44 - 84 - 16,
			16777215
		);
		drawCenteredString(
			Minecraft.getMinecraft().fontRendererObj,
			I18n.format("config.aqupd.rotation"),
			width / 2 - 75 - 2,
			height - 44 - 84 - 16,
			16777215
		);
		drawCenteredString(Minecraft.getMinecraft().fontRendererObj, "TeamPing", width / 2, 10, 16777215);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		buttonList.clear();
		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
			case 0:
				change1();
				break;
		}
		super.actionPerformed(button);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		switch (keyCode) {
			case Keyboard.KEY_E:
				mc.displayGuiScreen(null);
				break;
		}
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
