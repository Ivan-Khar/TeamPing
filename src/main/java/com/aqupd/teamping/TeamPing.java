package com.aqupd.teamping;

import static com.aqupd.teamping.listeners.EventListener.ticks;

import com.aqupd.teamping.listeners.EventListener;
import com.aqupd.teamping.setup.Registrations;
import com.aqupd.teamping.util.Configuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.awt.*;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = TeamPing.MOD_ID, name = TeamPing.MOD_NAME, version = TeamPing.VERSION, clientSideOnly = true)
public class TeamPing {

	public static final String MOD_ID = "teamping";
	public static final String MOD_NAME = "TeamPing";
	public static final String VERSION = "1.0";
	public static final Logger LOGGER = LogManager.getLogger();
	public static String logprefix = "[AqUpd's " + MOD_NAME + "] ";
	private static Minecraft mc;
	public static JsonArray pings = new JsonArray();

	public static boolean guimenu = false;
	public static int timer = 0;

	private final EventListener eventListener;

	public TeamPing() throws IOException {
		Registrations.init();
		mc = Minecraft.getMinecraft();
		this.eventListener = new EventListener();
		Configuration.loadOptions();
	}

	@Mod.EventHandler
	public void onFMLInitializationEvent(FMLInitializationEvent ev) {
		MinecraftForge.EVENT_BUS.register(eventListener);
	}

	public static void pingBlock(String type, Color color){
		JsonObject data = new JsonObject();
		int distance = Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16;
		if((Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16 > 128)) distance = 128;
		BlockPos bp = mc.thePlayer.rayTrace(distance, ticks).getBlockPos();

		JsonArray blockpos = new JsonArray();
		blockpos.add(new JsonPrimitive(bp.getX()));
		blockpos.add(new JsonPrimitive(bp.getY()));
		blockpos.add(new JsonPrimitive(bp.getZ()));

		JsonArray clr = new JsonArray();
		clr.add(color.getRed());
		clr.add(color.getGreen());
		clr.add(color.getBlue());

		int faketime = 63 * 2 + 500;

		data.add("bp", blockpos);
		data.add("lifetime", new JsonPrimitive(faketime));
		data.add("type", new JsonPrimitive(type));
		data.add("color", clr);
		data.add("uuid", new JsonPrimitive(UUID.randomUUID().toString()));
		pings.add(data);
	}
}
