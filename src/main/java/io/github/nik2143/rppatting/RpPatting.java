package io.github.nik2143.rppatting;

import io.github.nik2143.rppatting.commands.SearchCommand;
import io.github.nik2143.rppatting.commands.SearchRequestCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class RpPatting extends JavaPlugin {
    
    @Override
    public void onEnable() {
        Bukkit.getLogger().log(Level.INFO,"[RpPatting] Plugin Enabled");
        saveDefaultConfig();
        this.getCommand("searchrequest").setExecutor(new SearchRequestCommand(this));
        this.getCommand("search").setExecutor(new SearchCommand(this));
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().log(Level.INFO,"[RpPatting] Plugin Disabled");
    }
}
