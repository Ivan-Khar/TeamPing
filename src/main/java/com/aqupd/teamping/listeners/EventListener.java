package com.aqupd.teamping.listeners;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.client.PingSelector.*;
import static com.aqupd.teamping.client.SendData.*;
import static com.aqupd.teamping.setup.Registrations.keyBindings;
import static com.aqupd.teamping.util.Configuration.debug;

import com.aqupd.teamping.client.ClientThreads;
import com.aqupd.teamping.client.PingManager;
import com.aqupd.teamping.client.PingSelector;
import com.aqupd.teamping.client.PartyGUI;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventListener {
	public static boolean connectedtoserver = false;
	private boolean clearpings = false;

	public static Socket socket;
	public static boolean connecting = false;
	public static boolean guimenu = false;
	public static boolean stoppingmc = false;
	public static float ticks;
	public static Integer[] playsound = new Integer[3];
	public static int conattempts = 0;
	public static int timer = 0;

	public static long openChatTime = 0;
	public static boolean openChat = false;
	public static String openChatString = "";

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
			if (connectedtoserver) {
				connectedtoserver = false;
				if (!connecting && !stoppingmc) {
					connecting = true;
					try {
						socket = new Socket(debug ? "localhost" : "vps.theaq.one", 28754);
						new ClientThreads(socket, event.player, debug);
					} catch (IOException ex) {
						connecting = false;
						LOGGER.error("Server error", ex);
					}
					conattempts++;
				}
			}
			if (openChat && System.currentTimeMillis() - openChatTime > 50) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiChat(openChatString));
				openChatTime = 0;
				openChat = false;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTickEvent(TickEvent.ClientTickEvent event) {
		if (playsound[0] != null && playsound[1] != null && playsound[2] != null) {
			Minecraft.getMinecraft().theWorld.playSound(playsound[0], playsound[1], playsound[2],"minecraft:fireworks.blast_far", 0.5F, 1F, false);
			playsound[0] = null;
			playsound[1] = null;
			playsound[2] = null;
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

		if (keyBindings[2].isPressed()) Minecraft.getMinecraft().displayGuiScreen(new PartyGUI());
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiRenderEvent(RenderGameOverlayEvent.Pre event) {
		if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH && (guimenu || timer > 0)) {
			PingSelector.render();
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onChatMessage(ClientChatReceivedEvent event) {
		String chattext = event.message.getUnformattedTextForChat();
		Matcher matcher = Pattern.compile("teamping:.{3,32}").matcher(chattext);
		if(matcher.find()){
			String partyid = matcher.group().substring(9);
			IChatComponent component = new ChatComponentText("\nClick to join party with " + partyid + " id");
			component.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/teamping join " + partyid)).setColor(EnumChatFormatting.GRAY);
			event.message.appendSibling(component);
		}
	}
}
