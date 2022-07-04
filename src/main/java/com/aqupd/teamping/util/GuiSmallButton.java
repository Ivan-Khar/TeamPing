package com.aqupd.teamping.util;

import static com.aqupd.teamping.TeamPing.MOD_ID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiSmallButton extends GuiButton {

  public GuiSmallButton(int id, int xPos, int yPos, String displayString) {
    super(id, xPos, yPos, displayString);
    this.width = 12;
    this.height = 12;
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    if (this.visible)
    {
      this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
      Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MOD_ID, "textures/gui/pingsmenugui.png"));
      drawTexturedModalRect(this.xPosition, this.yPosition, 0, 234, this.width, this.height);
      this.mouseDragged(mc, mouseX, mouseY);
      this.drawCenteredString(mc.fontRendererObj, displayString, this.xPosition + this.width / 2 + 1, this.yPosition + 1, 14737632);
    }
  }
}
