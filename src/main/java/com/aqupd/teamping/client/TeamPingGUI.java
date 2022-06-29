package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;

import com.aqupd.teamping.util.GuiTextFieldHiddenText;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

@SuppressWarnings("FieldCanBeLocal")
public class TeamPingGUI extends GuiScreen {
  private GuiTextFieldHiddenText partyNameField;
  private GuiButton joinButton;
  private GuiCheckBox checkmark;
  private int rwidth, rheight, menuX, menuY, posX, posY;

  @Override
  public void updateScreen() {
    partyNameField.updateCursorCounter();
    partyNameField.setHideText(hidetext);
    hidetext = checkmark.isChecked();
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

    partyNameField = new GuiTextFieldHiddenText(1, fontRendererObj, posX + 8, posY + 24, menuX - 16, 20);
    partyNameField.setEnableBackgroundDrawing(true);
    partyNameField.setMaxStringLength(32);
    partyNameField.setText(partyName);
    partyNameField.setHideText(hidetext);
    buttonList.add(joinButton = new GuiButton(0, posX + 8, posY + 46, 98, 20, "Join/Create"));
    buttonList.add(checkmark = new GuiCheckBox(1, rwidth/2 + 28, posY + 50, "", hidetext));
    super.initGui();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();

    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MOD_ID, "textures/gui/pingsmenugui.png"));
    drawTexturedModalRect(posX, posY, 0, 0, menuX, menuY);

    String text = "Pings party";
    fontRendererObj.drawString("hide id", rwidth/2 + 42, posY + 52, 3158064);
    fontRendererObj.drawString(text, (rwidth/2 - fontRendererObj.getStringWidth(text) / 2), posY+8, 3158064);
    partyNameField.drawTextBox();
    joinButton.enabled = !partyName.equals("Your party id") && partyName.length() >= 3;
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
