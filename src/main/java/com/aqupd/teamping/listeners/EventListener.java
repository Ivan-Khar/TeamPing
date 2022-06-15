package com.aqupd.teamping.listeners;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.setup.Registrations.keyBindings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
	public void onClientTick(TickEvent.ClientTickEvent event) {
		for (JsonElement je: pings) {
			JsonObject data = je.getAsJsonObject();
			int lifetime = data.get("lifetime").getAsInt() - 1;
			data.addProperty("lifetime", lifetime);
		}
		guimenu = keyBindings[0].isKeyDown();
	}
}
