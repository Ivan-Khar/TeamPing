package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.client.SendData.joinParty;

import com.aqupd.teamping.util.GuiTextFieldHiddenText;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

@SuppressWarnings("FieldCanBeLocal")
public class PartyGUI extends GuiScreen {
  private GuiTextFieldHiddenText partyNameField;
  private GuiButton joinButton;
  private GuiCheckBox hidecheckbox;
  private GuiCheckBox randomcheckbox;
  private Boolean israndom = false;
  private int rwidth, rheight, menuX, menuY, posX, posY;

  @Override
  public void updateScreen() {
    hidetext = hidecheckbox.isChecked();
    israndom = randomcheckbox.isChecked();

    partyNameField.updateCursorCounter();
    partyNameField.setHideText(hidetext);
    partyNameField.setEnabled(!israndom);
    if (israndom) {
      partyName = UUID.randomUUID().toString().replace("-", "");
      partyNameField.setText(partyName);
    }
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
    buttonList.add(joinButton = new GuiButton(0, posX + 8, posY + 46, 80, 20, "Join/Create"));
    buttonList.add(hidecheckbox = new GuiCheckBox(1, rwidth/2+2, posY + 50, "", hidetext));
    buttonList.add(randomcheckbox = new GuiCheckBox(2, rwidth/2+36, posY + 50, "", hidetext));
    super.initGui();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();

    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MOD_ID, "textures/gui/pingsmenugui.png"));
    drawTexturedModalRect(posX, posY, 0, 0, menuX, menuY);

    String text = "Pings party";
    fontRendererObj.drawString("hide", rwidth/2 + 15, posY + 52, 3158064);
    fontRendererObj.drawString("random", rwidth/2 + 49, posY + 52, 3158064);
    fontRendererObj.drawString(text, (rwidth/2 - fontRendererObj.getStringWidth(text) / 2), posY+8, 3158064);
    partyNameField.drawTextBox();
    joinButton.enabled = (!partyName.equals("Your party id") && partyName.length() >= 3);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    switch (button.id) {
      case 0:
        israndom = false;
        randomcheckbox.setIsChecked(false);
        joinParty(partyName);
    }
    super.actionPerformed(button);
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    partyNameField.mouseClicked(mouseX, mouseX, mouseButton);
    if((mouseX > partyNameField.xPosition) && (mouseX < partyNameField.xPosition + partyNameField.width) &&
      (mouseY > partyNameField.yPosition) && (mouseY < partyNameField.yPosition + partyNameField.height)) partyNameField.setFocused(true);
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
