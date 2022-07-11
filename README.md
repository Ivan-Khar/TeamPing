# TeamPing (Beta)
### 1.8.9 and license is required

Mod that allows you to ping blocks to your team.
![text](https://i.imgur.com/tMp5lBr.png)

## Keybindings:
* F - ping blocks
* U - clear the pings
* ; - open Party Menu

## Commands:
* /teamping - shows all commands
* /teamping join \<partyid\> - join the party with an id whose length is in [3, 32] range
* /teamping leave - leave the party
* /teamping list - shows all players in the party
* /teamping kick \<playername\> - kicks a player from the party
* /teamping ban \<playername\> - bans a player from the party
* /teamping promote \<playername\> - promotes a player in a party
* /teamping reconnect - reconnects to a TeamPing server
* /teamping status - shows status of connection to the TeamPing server
* /teamping genInvText - puts invitation link to the chatbox

## For me
Need to put this thing into CLI arguments of Minecraft Client configuration
```
--tweakClass org.spongepowered.asm.launch.MixinTweaker --mixin mixins.teamping.json --username Aqua_tic
```
