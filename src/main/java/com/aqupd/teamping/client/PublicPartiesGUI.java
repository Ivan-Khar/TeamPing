package com.aqupd.teamping.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

import static com.aqupd.teamping.TeamPing.MOD_ID;
import static com.aqupd.teamping.util.UtilMethods.isMouseOver;

@SuppressWarnings("DuplicatedCode")
public class PublicPartiesGUI extends GuiScreen {
  private int rwidth, rheight, menuX, menuY, posX, posY, posY1;

  @Override
  public void updateScreen() {

  }

  @Override
  public void initGui() {
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    rwidth = sr.getScaledWidth();
    rheight = sr.getScaledHeight();
    menuX = 176;
    menuY = 222;
    posX = (rwidth - menuX) / 2;
    posY = (rheight - menuY) / 2;
    posY1 = (rheight + menuY) / 2;

    super.initGui();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();

    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MOD_ID, "textures/gui/pingsmenugui.png"));
    drawTexturedModalRect(posX, posY, 0, 0, menuX, menuY); //MainGUI

    String menu1 = "Your party";
    String menu2 = "Party list";

    drawTexturedModalRect(posX + 88, posY - 16, 0, 237, 84, 19); //Menu1 Button
    if (isMouseOver(mouseX, mouseY, posX + 3, posY - 16, 84, 19)) {
      drawTexturedModalRect(posX + 3, posY - 16, 168, 237, 84, 19); //Menu2 Button
      fontRendererObj.drawStringWithShadow(menu1, posX + 45 - (float) fontRendererObj.getStringWidth(menu1) / 2, posY - 10, 16777120);
    } else {
      drawTexturedModalRect(posX + 3, posY - 16, 84, 237, 84, 19); //Menu2 Button
      fontRendererObj.drawStringWithShadow(menu1, posX + 45 - (float) fontRendererObj.getStringWidth(menu1) / 2, posY - 10, 14737632);
    }
    fontRendererObj.drawString(menu2, posX + 130 - fontRendererObj.getStringWidth(menu2) / 2, posY - 10, 3158064);

    GlStateManager.color(255, 255, 255, 255);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {

    super.actionPerformed(button);
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    if (isMouseOver(mouseX, mouseY, posX + 3, posY - 16, 84, 19)) {
      mc.displayGuiScreen(new PartyGUI());
      mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    super.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    super.keyTyped(typedChar, keyCode);
  }
}
