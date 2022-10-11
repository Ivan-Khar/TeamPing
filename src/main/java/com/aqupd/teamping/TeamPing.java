package com.aqupd.teamping;

import java.io.IOException;

import com.aqupd.teamping.listeners.EventListener;
import com.aqupd.teamping.registrations.KeyBindings;
import com.aqupd.teamping.utils.Config;
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
	public static final String VERSION = "0.2.0";
	public static final Logger LOGGER = LogManager.getLogger("AqUpd's " + MOD_NAME);

	private final EventListener eventListener;

	public TeamPing() {
		KeyBindings.get.initialize();
		this.eventListener = new EventListener();
		Config.get.load();
	}

	@EventHandler
	public void init(FMLInitializationEvent ev) {
		MinecraftForge.EVENT_BUS.register(eventListener);
		//ClientCommandHandler.instance.registerCommand();
	}
}
