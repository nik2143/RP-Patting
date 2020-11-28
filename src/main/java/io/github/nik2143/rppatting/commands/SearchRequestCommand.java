package io.github.nik2143.rppatting.commands;

import io.github.nik2143.rppatting.RpPatting;
import io.github.nik2143.rppatting.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SearchRequestCommand  implements CommandExecutor {

    public static HashMap<UUID, UUID> searching = new HashMap<>();

    private final RpPatting rpPatting;

    public SearchRequestCommand(RpPatting rpPatting) {
        this.rpPatting = rpPatting;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player executor = (Player) sender;
        Player playersearching;
        if (args.length<1){
            executor.sendMessage(Utils.color(rpPatting.getConfig().getString("wrong-usage-searchacceptdeny")));
        }
        switch(args[0].toLowerCase()){
            case "accept":
                if (!searching.containsKey(executor.getUniqueId())){
                    executor.sendMessage(Utils.color(rpPatting.getConfig().getString("no-frisk-requests")));
                    return true;
                }
                if (Bukkit.getPlayer(searching.get(executor.getUniqueId()))==null){
                    executor.sendMessage(Utils.color(rpPatting.getConfig().getString("player-not-online")).replace("%playername%",Bukkit.getOfflinePlayer(searching.get(executor.getUniqueId())).getName()));
                    return true;
                }
                playersearching = Bukkit.getPlayer(searching.get(executor.getUniqueId()));
                executor.sendMessage(Utils.color(rpPatting.getConfig().getString("accepted-request-target").replace("%playername%",playersearching.getName())));
                playersearching.sendMessage(Utils.color(rpPatting.getConfig().getString("accepted-request").replace("%playername%",playersearching.getName())));
                playersearching.openInventory(executor.getInventory());
                searching.remove(executor.getUniqueId());
                break;
            case "deny":
                if (!searching.containsKey(executor.getUniqueId())){
                    executor.sendMessage(Utils.color(rpPatting.getConfig().getString("no-frisk-requests")));
                    return true;
                }
                if (Bukkit.getPlayer(searching.get(executor.getUniqueId()))!=null){
                    playersearching = Bukkit.getPlayer(searching.get(executor.getUniqueId()));
                    playersearching.sendMessage(Utils.color(rpPatting.getConfig().getString("denied-request").replace("%playername%",executor.getName())));
                }
                executor.sendMessage(Utils.color(rpPatting.getConfig().getString("denied-request-target").replace("%playername%",Bukkit.getOfflinePlayer(searching.get(executor.getUniqueId())).getName())));
                searching.remove(executor.getUniqueId());
                break;
            default:
                executor.sendMessage(Utils.color(rpPatting.getConfig().getString("wrong-usage-searchacceptdeny")));
        }
        return true;
    }
}
