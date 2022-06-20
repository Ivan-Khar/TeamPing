package com.aqupd.teamping;

import com.aqupd.teamping.listeners.EventListener;
import com.aqupd.teamping.setup.Registrations;
import com.aqupd.teamping.util.Configuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = TeamPing.MOD_ID, name = TeamPing.MOD_NAME, version = TeamPing.VERSION, clientSideOnly = true)
public class TeamPing {

	public static final String MOD_ID = "teamping";
	public static final String MOD_NAME = "TeamPing";
	public static final String VERSION = "1.0";
	public static final Logger LOGGER = LogManager.getLogger("AqUpd's " + MOD_NAME);
	public static JsonArray pings = new JsonArray();
	public static JsonObject ping = new JsonObject();
	public static boolean init = true;
	public static boolean guimenu = false;
	public static boolean connected = false;
	public static int step = 0;
	public static int timer = 0;
	public static long lastpingtime = 0;
	public static double cX = 0;
	public static double cY = 0;
	private final EventListener eventListener;

	public TeamPing() throws IOException {
		Registrations.init();
		this.eventListener = new EventListener();
		Configuration.loadOptions();
	}

	@EventHandler
	public void init(FMLInitializationEvent ev) {
		MinecraftForge.EVENT_BUS.register(eventListener);
	}
}
