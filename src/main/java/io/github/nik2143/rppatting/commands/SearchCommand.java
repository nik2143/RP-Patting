package io.github.nik2143.rppatting.commands;

import io.github.nik2143.rppatting.RpPatting;
import io.github.nik2143.rppatting.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SearchCommand implements CommandExecutor {

    private final RpPatting rpPatting;

    public SearchCommand(RpPatting rpPatting) {
       this.rpPatting = rpPatting;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player executor = (Player) sender;
        if (!executor.hasPermission("rp.search")){
            executor.sendMessage(Utils.color(rpPatting.getConfig().getString("no-perms")));
            return true;
        }
        if (args.length < 1){
            executor.sendMessage(Utils.color(rpPatting.getConfig().getString("wrong-usage-search")));
            return true;
        }
        if (Bukkit.getPlayerExact(args[0])==null){
            executor.sendMessage(Utils.color(rpPatting.getConfig().getString("player-not-online").replaceAll("%playername%",args[0])));
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target.getName().equalsIgnoreCase(executor.getName())){
            executor.sendMessage(Utils.color(rpPatting.getConfig().getString("no-frisk-yourself")));
            return true;
        }
        if (target.getLocation().distance(executor.getLocation())>rpPatting.getConfig().getDouble("max-distance",10)){
            executor.sendMessage(Utils.color(rpPatting.getConfig().getString("too-far").replaceAll("%playername%",args[0])));
            return true;
        }
        if (SearchRequestCommand.searching.containsKey(target.getUniqueId())){
            executor.sendMessage(Utils.color(rpPatting.getConfig().getString("already-frisk-request")));
            return true;
        }
        if (SearchRequestCommand.searching.containsValue(executor.getUniqueId())){
            executor.sendMessage(Utils.color(rpPatting.getConfig().getString("already-sent-frisk-request")));
            return true;
        }
        SearchRequestCommand.searching.put(target.getUniqueId(),executor.getUniqueId());
        TextComponent basemessage = new TextComponent(Utils.color(rpPatting.getConfig().getString("frisk-request").replaceAll("%playername%",executor.getName()))+" ");
        TextComponent acceptComponent = new TextComponent("[ACCEPT]");
        acceptComponent.setColor(ChatColor.GREEN.asBungee());
        acceptComponent.setBold(true);
        acceptComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/searchrequest accept"));
        TextComponent denyComponent = new TextComponent(" [DENY]");
        denyComponent.setColor(ChatColor.DARK_RED.asBungee());
        denyComponent.setBold(true);
        denyComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/searchrequest deny"));
        basemessage.addExtra(acceptComponent);
        basemessage.addExtra(denyComponent);
        target.spigot().sendMessage(basemessage);
        executor.sendMessage(Utils.color(rpPatting.getConfig().getString("frisk-request-sent").replaceAll("%playername%",target.getName())));
        Bukkit.getScheduler().scheduleSyncDelayedTask(rpPatting, () -> SearchRequestCommand.searching.remove(target.getUniqueId()),rpPatting.getConfig().getInt("cooldown-of-request")*20);
        return true;
    }
}
