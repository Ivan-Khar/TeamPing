package com.aqupd.teamping;

import com.aqupd.teamping.listeners.EventListener;
import com.aqupd.teamping.setup.Registrations;
import com.aqupd.teamping.util.Configuration;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	public static final String[] pingidnames = new String[]{"here", "notice", "question", "no", "yes", "defend", "attack", "mine"};
	public static List<JsonObject> pings = new ArrayList<>();
	public static JsonObject datatosend;
	public static String partyName = "Your party id";
	public static boolean hidetext = false;
	public static boolean isInParty = false;

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
