package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.client.SendData.*;
import static com.aqupd.teamping.listeners.EventListener.*;
import static com.aqupd.teamping.util.UtilMethods.isMouseOver;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

import com.aqupd.teamping.util.GuiTextFieldHiddenText;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

@SuppressWarnings("FieldCanBeLocal")
public class PartyGUI extends GuiScreen {
  private GuiTextFieldHiddenText partyNameField;
  private GuiButton joinButton;
  private GuiButton copyButton;
  private GuiButton connectButton;
  private GuiCheckBox hidecheckbox;
  private GuiCheckBox randomcheckbox;
  private Boolean israndom = false;
  private int rwidth, rheight, menuX, menuY, posX, posY, posY1;
  private boolean enableButtons = false;

  @Override
  public void updateScreen() {
    hidetext = hidecheckbox.isChecked();
    israndom = randomcheckbox.isChecked();
    randomcheckbox.enabled = !isInParty;
    randomcheckbox.visible = !isInParty;
    joinButton.displayString = isInParty?"Disconnect":"Join/Create";
    copyButton.enabled = isInParty;
    copyButton.visible = isInParty;
    connectButton.enabled = !connecting && (conattempts != 3);
    partyNameField.setEnabled(!israndom && !isInParty);
    partyNameField.setHideText(hidetext);
    if (!isInParty) {
      partyNameField.updateCursorCounter();
      if (israndom) {
        partyName = UUID.randomUUID().toString().replace("-", "");
        partyNameField.setText(partyName);
      }
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
    posY1 = (rheight + menuY) / 2;

    partyNameField = new GuiTextFieldHiddenText(1, fontRendererObj, posX + 8, posY + 24, menuX - 16, 20);
    partyNameField.setEnableBackgroundDrawing(true);
    partyNameField.setMaxStringLength(32);
    partyNameField.setText(partyName);
    partyNameField.setHideText(hidetext);
    partyNameField.setEnabled(!israndom && !isInParty);

    buttonList.add(joinButton = new GuiButton(0, posX + 8, posY + 46, 80, 20, isInParty?"Disconnect":"Join/Create"));
    buttonList.add(hidecheckbox = new GuiCheckBox(1, rwidth/2+2, posY + 50, "", hidetext));
    buttonList.add(randomcheckbox = new GuiCheckBox(2, rwidth/2+36, posY + 50, "", israndom));
    buttonList.add(copyButton = new GuiButton(3, rwidth/2+36, posY + 46, 45, 20, "Copy"));
    buttonList.add(connectButton = new GuiButton(4, posX + 67, posY1-26, 103, 20, "Connect to server"));
    randomcheckbox.enabled = !isInParty;
    randomcheckbox.visible = !isInParty;
    copyButton.enabled = isInParty;
    copyButton.visible = isInParty;
    connectButton.enabled = !connecting && (conattempts != 3);
    connectedPlayers();
    super.initGui();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();

    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MOD_ID, "textures/gui/pingsmenugui.png"));
    drawTexturedModalRect(posX, posY, 0, 0, menuX, menuY);

    String text = "Pings party";
    String text1 = "Player list";

    fontRendererObj.drawString("hide", rwidth/2 + 15, posY + 52, 3158064);
    if (!isInParty) fontRendererObj.drawString("random", rwidth/2 + 49, posY + 52, 3158064);
    fontRendererObj.drawString(text, (rwidth/2 - fontRendererObj.getStringWidth(text) / 2), posY+9, 3158064);
    partyNameField.drawTextBox();
    joinButton.enabled = (!partyName.equals("Your party id") && partyName.length() >= 3);

    if (isInParty) {
      fontRendererObj.drawString(text1, (rwidth/2 - fontRendererObj.getStringWidth(text1) / 2), posY+72, 3158064);
      int i = 0;
      enableButtons = partyPlayers.toArray()[0].equals(mc.thePlayer.getName());
      for (String s: partyPlayers) {
        int xpos = posX + 7;
        int ypos = posY + 82 + 13*i;
        GlStateManager.color(255, 255, 255, 255);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MOD_ID, "textures/gui/pingsmenugui.png"));
        drawTexturedModalRect(xpos, ypos, 0, 222, 120, 12);
        if (enableButtons && !Objects.equals(s, mc.thePlayer.getName())) {
          if (isMouseOver(mouseX, mouseY, xpos + 123, ypos, 12, 12))
            drawTexturedModalRect(xpos + 123, ypos, 12, 234, 12, 12);
          else drawTexturedModalRect(xpos + 123, ypos, 0, 234, 12, 12);
          if (isMouseOver(mouseX, mouseY, xpos + 136, ypos, 12, 12))
            drawTexturedModalRect(xpos + 136, ypos, 12, 234, 12, 12);
          else drawTexturedModalRect(xpos + 136, ypos, 0, 234, 12, 12);
          if (isMouseOver(mouseX, mouseY, xpos + 149, ypos, 12, 12))
            drawTexturedModalRect(xpos + 149, ypos, 12, 234, 12, 12);
          else drawTexturedModalRect(xpos + 149, ypos, 0, 234, 12, 12);

          drawTexturedModalRect(xpos+124, ypos+1, 0, 246, 10, 10);
          drawTexturedModalRect(xpos+137, ypos+1, 10, 246, 10, 10);
          drawTexturedModalRect(xpos+150, ypos+1, 20, 246, 10, 10);
        } else {
          drawTexturedModalRect(xpos+123, ypos, 24, 234, 12, 12);
          drawTexturedModalRect(xpos+136, ypos, 24, 234, 12, 12);
          drawTexturedModalRect(xpos+149, ypos, 24, 234, 12, 12);

          drawTexturedModalRect(xpos+124, ypos+1, 30, 246, 10, 10);
          drawTexturedModalRect(xpos+137, ypos+1, 40, 246, 10, 10);
          drawTexturedModalRect(xpos+150, ypos+1, 50, 246, 10, 10);
        }

        fontRendererObj.drawString(s, xpos+3, ypos+2, 16777215, true);
        i++;
        if(isMouseOver(mouseX, mouseY, xpos + 123, ypos, 12, 12)) {
          drawHoveringText(Collections.singletonList("Ban player"), mouseX, mouseY, fontRendererObj);
        } else if(isMouseOver(mouseX, mouseY, xpos + 136, ypos, 12, 12)) {
          drawHoveringText(Collections.singletonList("Kick player"), mouseX, mouseY, fontRendererObj);
        } else if(isMouseOver(mouseX, mouseY, xpos + 149, ypos, 12, 12)) {
          drawHoveringText(Collections.singletonList("Promote player"), mouseX, mouseY, fontRendererObj);
        }
        GlStateManager.disableLighting();
      }
    }

    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MOD_ID, "textures/gui/pingsmenugui.png"));
    GlStateManager.color(172, 172, 172, 255);
    drawTexturedModalRect(posX, posY1-32, 176, 0, 64, 32);
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    if (!connecting) {
      drawTexturedModalRect(posX + 42, posY1-26, 176, 32, 10, 10);
      fontRendererObj.drawString("Con: N/A", posX + 4, posY1-15, 3158064);
    }
    else {
      drawTexturedModalRect(posX + 42, posY1 - 26, 176, 42, 10, 10);
      fontRendererObj.drawString("Con: " + playerCount, posX + 4, posY1-15, 3158064);
    }
    fontRendererObj.drawString("Status:", posX + 4, posY1-25, 3158064);
    GlStateManager.color(255, 255, 255, 255);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    switch (button.id) {
      case 0:
        if (isInParty) {
          leaveParty();
          isInParty = false;
          partyPlayers.clear();
        } else {
          israndom = false;
          randomcheckbox.setIsChecked(false);
          joinParty(partyName);
        }
        break;
      case 3:
        setClipboardString(partyName);
        break;
      case 4:
        connectedtoserver = true;
    }
    super.actionPerformed(button);
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    partyNameField.mouseClicked(mouseX, mouseX, mouseButton);
    if (isMouseOver(mouseX, mouseY, partyNameField.xPosition, partyNameField.yPosition, partyNameField.width, partyNameField.height)) partyNameField.setFocused(true);
    int i = 0;
    if (isInParty && enableButtons) {
      for (String s : partyPlayers) {
        int xpos = posX + 7;
        int ypos = posY + 82 + 13 * i;
        if (!s.equals(mc.thePlayer.getName())) {
          if (isMouseOver(mouseX, mouseY, xpos + 123, ypos, 12, 12)) {
            banFromParty(s);
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
          } else if (isMouseOver(mouseX, mouseY, xpos + 136, ypos, 12, 12)) {
            kickFromParty(s);
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
          } else if (isMouseOver(mouseX, mouseY, xpos + 149, ypos, 12, 12)) {
            promotePartyMember(s);
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
          }
        }
        i++;
      }
    }
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    super.keyTyped(typedChar, keyCode);
    if (partyNameField.isFocused()) {
      partyNameField.textboxKeyTyped(typedChar, keyCode);
      partyName = partyNameField.getText();
    }
  }
}
