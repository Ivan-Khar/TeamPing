package com.aqupd.teamping.listeners;

import static com.aqupd.teamping.TeamPing.coords;
import static com.aqupd.teamping.util.Configuration.change1;

import com.aqupd.teamping.TeamPing;
import com.aqupd.teamping.client.RenderPing;
import com.aqupd.teamping.gui.GuiConfig;
import com.aqupd.teamping.setup.Registrations;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;

public class EventListener {
	private final Minecraft mc = Minecraft.getMinecraft();
	EntityPlayer entity = Minecraft.getMinecraft().thePlayer;
	public static float ticks;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTickEvent(TickEvent.RenderTickEvent event){
		ticks = event.renderTickTime;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyPressed(KeyInputEvent event) {
		if (Registrations.keyBindings[0].isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
		} else if (Registrations.keyBindings[1].isPressed()) {
			TeamPing.getBlock();
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onHighlight(DrawBlockHighlightEvent event){
		if(mc.theWorld != null) RenderPing.render(event);
	}
}
