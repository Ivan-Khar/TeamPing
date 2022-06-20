package com.aqupd.teamping.listeners;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.setup.Registrations.keyBindings;

import com.aqupd.teamping.client.ClientListenThread;
import com.aqupd.teamping.client.ClientWriteThread;
import com.aqupd.teamping.client.RenderGUI;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventListener {
	public static float ticks;
	public static Socket socket;
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTickEvent(TickEvent.RenderTickEvent event){
		ticks = event.renderTickTime;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		if (!connected) {
			try {
				socket = new Socket("localhost", 28754);
				new ClientListenThread(socket).start();
				new ClientWriteThread(socket, event.player).start();
			} catch (UnknownHostException ex) {
				LOGGER.error("Server not found", ex);
			} catch (IOException ex) {
				LOGGER.error("Server error", ex);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTickEvent(TickEvent.ClientTickEvent event) {
		for (JsonElement je: pings) {
			JsonObject data = je.getAsJsonObject();
			int lifetime = data.get("lifetime").getAsInt() - 1;
			data.addProperty("lifetime", lifetime);
		}

		if(guimenu && timer < 15) timer++;
		else if(!guimenu && timer > 0) {
			timer--;
			cX = 0;
			cY = 0;
		}

		guimenu = keyBindings[0].isKeyDown();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiRenderEvent(RenderGameOverlayEvent.Pre event) {
		if(event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH && (guimenu || timer > 0)){
			RenderGUI.render();
		}
	}
}
