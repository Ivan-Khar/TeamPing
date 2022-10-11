package com.aqupd.teamping.utils;

import com.aqupd.teamping.TeamPing;
import com.google.gson.*;

import java.io.*;

public class Config {

  private Config() {}                       //Create Instance
  public static Config get = new Config();  //Of this class

  private final File confFile = new File("./config/AqMods/teamping.json");
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  //All the data
  private boolean debug = false;


  //Config loader
  public void load() {
    if (!confFile.exists() || confFile.length() == 0) save();
    try {
      JsonObject data = gson.fromJson(new FileReader(confFile), JsonObject.class);
      JsonElement setting;
      if((setting = data.get("debug")) != null) debug = setting.getAsBoolean();
    } catch (FileNotFoundException ex) {
      TeamPing.LOGGER.trace("Couldn't load configuration file", ex);
    }
  }

  //Config saver
  public void save() {
    try {
      if (!confFile.exists()) { confFile.getParentFile().mkdirs(); confFile.createNewFile(); }

      JsonObject jo = new JsonObject();
      jo.add("debug", new JsonPrimitive(debug));

      PrintWriter printwriter = new PrintWriter(new FileWriter(confFile));
      printwriter.print(gson.toJson(jo));
      printwriter.close();
    } catch (IOException ex) {
      TeamPing.LOGGER.trace("Couldn't save configuration file", ex);
    }
  }
}
