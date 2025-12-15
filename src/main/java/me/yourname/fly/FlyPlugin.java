package me.yourname.fly;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class FlyPlugin extends JavaPlugin {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 3000; // 3 seconds

    @Override
    public void onEnable() {
        getLogger().info("FlyPlugin enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("FlyPlugin disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("fly.use")) {
            player.sendMessage("§cNo permission.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§cUsage: /fly <on|off>");
            return true;
        }

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        if (cooldowns.containsKey(uuid)) {
            long last = cooldowns.get(uuid);
            long remaining = COOLDOWN_TIME - (now - last);

            if (remaining > 0) {
                player.sendMessage("§cWait " + (remaining / 1000.0) + " seconds!");
                return true;
            }
        }

        cooldowns.put(uuid, now);

        if (args[0].equalsIgnoreCase("on")) {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("§aFlight enabled!");
            return true;
        }

        if (args[0].equalsIgnoreCase("off")) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage("§cFlight disabled!");
            return true;
        }

        player.sendMessage("§cUsage: /fly <on|off>");
        return true;
    }
}
