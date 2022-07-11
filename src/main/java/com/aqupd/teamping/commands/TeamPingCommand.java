package com.aqupd.teamping.commands;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.client.SendData.*;
import static com.aqupd.teamping.listeners.EventListener.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.*;

public class TeamPingCommand extends CommandBase {
  @Override
  public int getRequiredPermissionLevel()
  {
    return 0;
  }

  @Override
  public String getCommandName() {
    return "teamping";
  }

  @Override
  public String getCommandUsage(ICommandSender sender) {
    return null;
  }

  @Override
  public void processCommand(ICommandSender sender, String[] args) {
    IChatComponent message = new ChatComponentText("");
    if (args.length == 0) {
      message.appendSibling(new ChatComponentText("--Showing help for TeamPing mod--").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GREEN)));
      message.appendSibling(new ChatComponentText("\n/teamping").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
      message.appendSibling(new ChatComponentText("\n/teamping join <partyid>").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
      message.appendSibling(new ChatComponentText("\n/teamping leave").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
      message.appendSibling(new ChatComponentText("\n/teamping status").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
      message.appendSibling(new ChatComponentText("\n/teamping reconnect").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
      sender.addChatMessage(message);
      return;
    }
    switch(args[0]) {
      case "join":
        if (!connecting) {
          message.appendSibling(new ChatComponentText("Not connected to the server").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
          break;
        }
        StringBuilder tempname = new StringBuilder();
        if (args.length >= 2) {
          for(int i=0;i<args.length-1;i++) {
            tempname.append(args[i + 1]).append(" ");
          }
          tempname.deleteCharAt(tempname.length()-1);
        }
        if (args.length >= 2 && tempname.length() >= 3 && tempname.length() <= 32) {
          leaveParty();
          joinParty(tempname.toString());
          message.appendSibling(new ChatComponentText("Joined the party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
        } else {
          message.appendSibling(new ChatComponentText("PartyID length must be in [3, 32] range").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        }
        break;
      case "leave":
        if (!connecting) {
          message.appendSibling(new ChatComponentText("Not connected to the server").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
          break;
        } else if (partyPlayers.size() == 0) {
          message.appendSibling(new ChatComponentText("You are not in a party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
          break;
        }
        leaveParty();
        message.appendSibling(new ChatComponentText("Left from the party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
        break;
      case "list":
        if (!connecting) {
          message.appendSibling(new ChatComponentText("Not connected to the server").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
          break;
        }
        message.appendSibling(new ChatComponentText("--List of players in the current party--").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GREEN)));
        if (partyPlayers.size() != 0) partyPlayers.forEach(name -> {
          message.appendSibling(new ChatComponentText("\n" + name).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
          if (partyPlayers.get(0).equals(name)) message.appendSibling(new ChatComponentText(" Leader").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD).setBold(true)));
        });
        else message.appendSibling(new ChatComponentText("\nYou are not in a party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
        break;
      case "kick":
        if (partyPlayers.size() == 0) {
        message.appendSibling(new ChatComponentText("You are not in a party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else if (args.length != 2) {
          message.appendSibling(new ChatComponentText("You need to type the player name").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else if (!partyPlayers.get(0).equals(sender.getName())) {
          message.appendSibling(new ChatComponentText("You are not the leader of the party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else if (!partyPlayers.contains(args[1])) {
          message.appendSibling(new ChatComponentText("There's no player in a party with that name").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else {
          kickFromParty(args[1]);
          message.appendSibling(new ChatComponentText("Player got kicked from the party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
        }
        break;
      case "ban":
        if (partyPlayers.size() == 0) {
          message.appendSibling(new ChatComponentText("You are not in a party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else if (args.length != 2) {
          message.appendSibling(new ChatComponentText("You need to type the player name").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else if (!partyPlayers.get(0).equals(sender.getName())) {
          message.appendSibling(new ChatComponentText("You are not the leader of the party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else if (!partyPlayers.contains(args[1])) {
          message.appendSibling(new ChatComponentText("There's no player in a party with that name").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else {
          banFromParty(args[1]);
          message.appendSibling(new ChatComponentText("Player got banned from the party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
        }
        break;
      case "promote":
        if (partyPlayers.size() == 0) {
          message.appendSibling(new ChatComponentText("You are not in a party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else if (args.length != 2) {
          message.appendSibling(new ChatComponentText("You need to type the player name").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else if (!partyPlayers.get(0).equals(sender.getName())) {
          message.appendSibling(new ChatComponentText("You are not the leader of the party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else if (!partyPlayers.contains(args[1])) {
          message.appendSibling(new ChatComponentText("There's no player in a party with that name").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else {
          promotePartyMember(args[1]);
          message.appendSibling(new ChatComponentText("Player was promoted to the party leader").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
        }
        break;
      case "reconnect":
        if (!connecting) {
          connectedtoserver = true;
          message.appendSibling(new ChatComponentText("Reconnecting...").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
        } else {
          message.appendSibling(new ChatComponentText("You already connected to the server").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        }
        break;
      case "status":
        if (connecting) connectedPlayers();
        message.appendSibling(new ChatComponentText("--Connection status--").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GREEN)));
        message.appendSibling(new ChatComponentText("\n" + (connecting ? "Connected" : "Not connected") + " to the server").setChatStyle(new ChatStyle().setColor(connecting ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)));
        message.appendSibling(new ChatComponentText("\n" + "Clients connected: " + (connecting ? playerCount : "N/A")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
        break;
      case "genInvText":
        if (isInParty) {
          message.appendSibling(new ChatComponentText("Invitation text was put in your message box").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
          openChat = true;
          openChatTime = System.currentTimeMillis();
          openChatString = "teamping:" + partyName;
        } else {
          message.appendSibling(new ChatComponentText("You are not in a party").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        }
        break;
      default:
        message.appendSibling(new ChatComponentText("Unknown command. Try /teamping for a list of commands").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
    }
    sender.addChatMessage(message);
  }

  @Override
  public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    System.out.println(Arrays.toString(args));
    List<String> tabcomp = new ArrayList<>();
    if (args.length == 1) {
      tabcomp.add("join");
      tabcomp.add("list");
      tabcomp.add("kick");
      tabcomp.add("ban");
      tabcomp.add("promote");
      tabcomp.add("leave");
      tabcomp.add("reconnect");
      tabcomp.add("status");
      tabcomp.add("genInvText");
    }
    if (args.length == 2 && (args[0].equals("kick") || args[0].equals("ban") || args[0].equals("promote"))) {
      if (partyPlayers.size() != 0) {
        List<String> players = getListOfStringsMatchingLastWord(args, partyPlayers);
        players.remove(sender.getName());
        tabcomp.addAll(players);
      } else {
        tabcomp.add("You are not in the party");
      }
    }
    return tabcomp;
  }
}
