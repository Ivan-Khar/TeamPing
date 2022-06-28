package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.MOD_ID;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

public class TeamPingGUI extends GuiScreen {
  private GuiTextField partyNameField;
  private GuiButton buttons;
  private int rwidth, rheight, menuX, menuY, posX, posY;
  private String partyName = "Your party name";

  public void updateScreen() {
    partyNameField.updateCursorCounter();
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

    partyNameField = new GuiTextField(1, fontRendererObj, posX + 8, posY + 24, menuX - 16, 20);
    partyNameField.setEnableBackgroundDrawing(true);
    partyNameField.setMaxStringLength(32);
    partyNameField.setText(this.partyName);

    buttonList.add(buttons = new GuiButton(0, rwidth/2 - 98/2, posY + 46, 98, 20, "Join/Create"));
    super.initGui();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    Tessellator tes = Tessellator.getInstance();
    WorldRenderer wr = tes.getWorldRenderer();

    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MOD_ID, "textures/gui/pingsmenugui.png"));
    drawTexturedModalRect(posX, posY, 0, 0, menuX, menuY);

    String text = "Pings party";
    fontRendererObj.drawString(text, (rwidth/2 - fontRendererObj.getStringWidth(text) / 2), posY+8, 3158064);
    partyNameField.drawTextBox();
    /*
    GlStateManager.enableBlend();
    GlStateManager.disableTexture2D();
    wr.setTranslation(width/2, height/2, 0);
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(0, 0, 0).color(255, 0, 0, 127).endVertex();
    wr.pos(0, 150, 0).color(255, 0, 0, 127).endVertex();
    wr.pos(150, 150, 0).color(255, 0, 0, 127).endVertex();
    wr.pos(150, 0, 0).color(255, 0, 0, 127).endVertex();
    tes.draw();

    wr.setTranslation(0, 0, 0);
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
    */
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    partyNameField.mouseClicked(mouseX, mouseX, mouseButton);
    if(((mouseX > posX + 8) && (mouseX < posX + 8 + menuX - 16)) && ((mouseY > posY + 24) && (mouseY < posY + 44))) partyNameField.setFocused(true);
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException
  {
    super.keyTyped(typedChar, keyCode);
    if (partyNameField.isFocused()) {
      partyNameField.textboxKeyTyped(typedChar, keyCode);
      partyName = partyNameField.getText();
    }
  }
}
