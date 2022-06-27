package com.aqupd.teamping.listeners;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.setup.Registrations.*;
import static com.aqupd.teamping.util.Configuration.*;

import com.aqupd.teamping.client.ClientThreads;
import com.aqupd.teamping.client.PingManager;
import com.aqupd.teamping.client.RenderGUI;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventListener {
	public static float ticks;
	public static Socket socket;
	private boolean connectedtoserver = false;
	private boolean clearpings = false;
	public static boolean connecting = false;
	public static boolean playsound = false;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTickEvent(TickEvent.RenderTickEvent event) {
		ticks = event.renderTickTime;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		if (!event.isLocal || debug) {
			if (!connecting) connectedtoserver = true;
		}
	}

	private long lastjoineventusage = 0;
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void someEvent(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayerSP && (System.currentTimeMillis() - lastjoineventusage) > 250) {
			clearpings = true;
			lastjoineventusage = System.currentTimeMillis();
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		if (event.player instanceof EntityPlayerSP) {
			if (time == 0) time = System.currentTimeMillis();
			if (connectedtoserver) {
				connectedtoserver = false;
				if (!connecting && !stoppingmc) {
					connecting = true;
					try {
						String serverip = (debug ? "localhost" : Minecraft.getMinecraft().getCurrentServerData().serverIP);
						socket = new Socket(debug ? "localhost" : "vps.theaq.one", 28754);
						new ClientThreads(socket, event.player, serverip, debug);
					} catch (IOException ex) {
						connecting = false;
						LOGGER.error("Server error", ex);
					}
					time = System.currentTimeMillis();
					conattempts++;
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTickEvent(TickEvent.ClientTickEvent event) {
		if (playsound) {
			Minecraft.getMinecraft().thePlayer.playSound("minecraft:fireworks.blast_far", 0.5F, 1F);
			playsound = !playsound;
		}

		Iterator<JsonObject> pingsIter = pings.iterator();
		while (pingsIter.hasNext()) {
			JsonObject data = pingsIter.next();
			long time = data.get("time").getAsLong();
			if ((System.currentTimeMillis() - time) > 15000) {
				pingsIter.remove();
			}
		}

		if (guimenu && timer < 15) timer++;
		else if (!guimenu && timer > 0) {
			timer--;
			cX = 0;
			cY = 0;
		}

		guimenu = keyBindings[0].isKeyDown();
		if (keyBindings[1].isPressed() || clearpings) {
			PingManager.clear();
			clearpings = false;
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiRenderEvent(RenderGameOverlayEvent.Pre event) {
		if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH && (guimenu || timer > 0)) {
			RenderGUI.render();
		}
	}
}
