package com.aqupd.teamping.util;

import static com.aqupd.teamping.TeamPing.LOGGER;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Configuration {

	public static boolean debug = false;

	private static final File configFile = new File("./config/AqMods/TeamPing.properties");

	public static void loadOptions() throws IOException {
		if (!configFile.exists() || configFile.length() == 0) saveOptions();

		BufferedReader bufferedreader = new BufferedReader(new FileReader(configFile));
		String s;

		while ((s = bufferedreader.readLine()) != null) {
			String[] astring = s.split(":");

			if (astring[0].equals("debug")) debug = Boolean.parseBoolean(astring[1]);
		}
	}

	public static void saveOptions() {
		try {
			Files.createDirectories(Paths.get("./config/AqMods/"));

			if (!configFile.exists()) configFile.createNewFile();
			if (configFile.exists()) {
				PrintWriter printwriter = new PrintWriter(new FileWriter(configFile));
				printwriter.println("debug:" + debug);
				printwriter.close();
			}
		} catch (Exception exception) {
			LOGGER.error("Failed to save options", exception);
		}
	}
}
