package com.aqupd.teamping;



import static com.aqupd.teamping.listeners.EventListener.ticks;

import com.aqupd.teamping.listeners.EventListener;
import com.aqupd.teamping.setup.Registrations;
import com.aqupd.teamping.util.Configuration;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


@Mod(modid = TeamPing.MOD_ID, name = TeamPing.MOD_NAME, version = TeamPing.VERSION, clientSideOnly = true)
public class TeamPing {

	public static final String MOD_ID = "teamping";
	public static final String MOD_NAME = "TeamPing";
	public static final String VERSION = "1.0";
	public static final Logger LOGGER = LogManager.getLogger();
	public static String logprefix = "[AqUpd's " + MOD_NAME + "] ";
	public static ArrayList<BlockPos> coords = new ArrayList<>();
	private static Minecraft mc;

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

	public static void getBlock(){
		coords.add(mc.thePlayer.rayTrace(Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16, ticks).getBlockPos());
	}
}
/*
if (Setting1) {
	EntityPlayer player = event.player;
	BlockPos bp = player.rayTrace(64, 0).getBlockPos();
	AxisAlignedBB aabb = new AxisAlignedBB(bp, bp.add(1, 1, 1));
	System.out.println(aabb);

	GlStateManager.depthMask(false);
	GlStateManager.disableTexture2D();
	GlStateManager.disableLighting();
	GlStateManager.disableCull();
	GlStateManager.disableBlend();
	RenderGlobal.drawOutlinedBoundingBox(aabb, 255, 255, 255, 255);
	GlStateManager.enableTexture2D();
	GlStateManager.enableLighting();
	GlStateManager.enableCull();
	GlStateManager.disableBlend();
	GlStateManager.depthMask(true);

}
*/