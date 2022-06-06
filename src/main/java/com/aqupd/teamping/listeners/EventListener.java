package com.aqupd.teamping.listeners;

import static com.aqupd.teamping.TeamPing.pings;

import com.aqupd.teamping.TeamPing;
import com.aqupd.teamping.client.RenderPingInWorld;
import com.aqupd.teamping.setup.Registrations;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
			//Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
		} else if (Registrations.keyBindings[1].isPressed()) {
			TeamPing.pingBlock("normal");
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onHighlight(DrawBlockHighlightEvent event){
		if(mc.theWorld != null) {
			RenderPingInWorld.renderBlock(event);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiRender(RenderGameOverlayEvent.Post event){
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		for (JsonElement je: pings) {
			JsonObject data = je.getAsJsonObject();
			int lifetime = data.get("lifetime").getAsInt() - 1;
			data.addProperty("lifetime", lifetime);
		}
	}
}
