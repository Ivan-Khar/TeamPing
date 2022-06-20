package com.aqupd.teamping.util;

import static com.aqupd.teamping.TeamPing.LOGGER;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Configuration {

	public static boolean Setting1 = false;
	public static boolean Setting2 = false;
	public static boolean Setting3 = false;

	private static final File configFile = new File("./config/AqMods/TeamPing.properties");

	public static void change1() {
		Setting1 = !Setting1;
		saveOptions();
	}

	public static void change2() {
		Setting2 = !Setting2;
		saveOptions();
	}

	public static void change3() {
		Setting3 = !Setting3;
		saveOptions();
	}

	public static void loadOptions() throws IOException {
		if (!configFile.exists() || configFile.length() == 0) saveOptions();

		BufferedReader bufferedreader = new BufferedReader(new FileReader(configFile));
		String s;

		while ((s = bufferedreader.readLine()) != null) {
			String[] astring = s.split(":");

			if (astring[0].equals("setting1")) Setting1 = Boolean.parseBoolean(astring[1]);
			if (astring[0].equals("setting2")) Setting2 = Boolean.parseBoolean(astring[1]);
			if (astring[0].equals("setting3")) Setting3 = Boolean.parseBoolean(astring[1]);
		}
	}

	public static void saveOptions() {
		try {
			Files.createDirectories(Paths.get("./config/AqMods/"));

			if (!configFile.exists()) configFile.createNewFile();
			if (configFile.exists()) {
				PrintWriter printwriter = new PrintWriter(new FileWriter(configFile));
				printwriter.println("setting1:" + Setting1);
				printwriter.println("setting2:" + Setting2);
				printwriter.println("setting3:" + Setting3);
				printwriter.close();
			}
		} catch (Exception exception) {
			LOGGER.error("Failed to save options", exception);
		}
	}
}
