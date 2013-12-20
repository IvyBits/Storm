package tk.ivybits.storm.utility;

import com.google.common.collect.Sets;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ReflectCommand {

    private HashMap<String, Set<Method>>
            everyoneCommands = new HashMap<String, Set<Method>>(),
            playerCommands = new HashMap<String, Set<Method>>(),
            consoleCommands = new HashMap<String, Set<Method>>();
    private CommandExecutor executor;
    private Plugin plugin;

    public ReflectCommand(Plugin plug) {
        plugin = plug;
        executor = new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
                Object returnValue = false;
                String commandName = command.getName();
                Object[] varargs = prepend(args, commandSender);
                try {
                    if (everyoneCommands.containsKey(commandName)) {
                        for (Method m : everyoneCommands.get(command.getName()))
                            if (varargs.length >= m.getParameterTypes().length)
                                returnValue = m.invoke(null, trim(varargs, m.getParameterTypes().length));
                    }
                    if (commandSender instanceof Player) {
                        if (playerCommands.containsKey(commandName))
                            for (Method m : playerCommands.get(commandName))
                                if (varargs.length >= m.getParameterTypes().length)
                                    returnValue = m.invoke(null, trim(varargs, m.getParameterTypes().length));
                    }
                    if (commandSender instanceof ConsoleCommandSender) {
                        if (consoleCommands.containsKey(commandName))
                            for (Method m : consoleCommands.get(commandName))
                                if (varargs.length >= m.getParameterTypes().length)
                                    returnValue = m.invoke(null, trim(varargs, m.getParameterTypes().length));
                    }
                    return (Boolean) returnValue;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
    }

    public void register(Class clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            register(m, m.getAnnotation(Command.class));
        }
    }

    public void register(Method m, Command com) {
        if (com == null)
            return;
        Registrator register = new Registrator(plugin, executor);

        String name = com.name();
        List<String> alias = new ArrayList<String>();
        alias.add(name);
        if (!ArrayUtils.isEmpty(com.alias()))
            Collections.addAll(alias, com.alias());
        register.register(name, alias.toArray(new String[alias.size()]), com.usage(), com.permission(), com.permissionMessage());
        HashMap<String, Set<Method>> hm;
        try {
            Field map = this.getClass().getDeclaredField(com.sender().name().toLowerCase() + "Commands");
            hm = (HashMap<String, Set<Method>>) map.get(this);
            if (hm.containsKey(name))
                hm.get(name).add(m);
            else
                hm.put(name, Sets.newHashSet(m));
            map.set(this, hm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Object[] prepend(Object[] arr, Object firstElement) {
        List<Object> pre = new ArrayList<Object>();
        pre.add(firstElement);
        Collections.addAll(pre, arr);
        return pre.toArray(new Object[pre.size()]);
    }

    Object[] trim(Object[] input, int newSize) {
        Object result[] = new Object[newSize];
        System.arraycopy(input, 0, result, 0, newSize);
        return result;
    }

    public class Registrator {

        protected final Plugin plugin;
        protected final CommandExecutor executor;

        public Registrator(Plugin plugin, CommandExecutor executor) {
            this.plugin = plugin;
            this.executor = executor;
        }

        public void register(String name, String[] aliases, String usage, String[] perms, String permMessage) {
            getCommandMap().register(plugin.getDescription().getName(), new DynamicCommand(aliases, name, "Error! Correct usage is: /" + aliases[0] + " " + usage.replace("<command>", name), perms, permMessage, executor, plugin, plugin));
        }

        public CommandMap getCommandMap() {
            Field map;
            try {
                map = SimplePluginManager.class.getDeclaredField("commandMap");
                map.setAccessible(true);
                return (CommandMap) map.get(plugin.getServer().getPluginManager());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class DynamicCommand extends org.bukkit.command.Command implements PluginIdentifiableCommand {

        public CommandExecutor owner;
        public Object registeredWith;
        public Plugin owningPlugin;
        public String[] permissions = new String[0];

        public DynamicCommand(String[] aliases, String name, String usage, String[] perms, String permMessage, CommandExecutor owner, Object registeredWith, Plugin plugin) {
            super(name, name, usage, Arrays.asList(aliases));
            this.owner = owner;
            this.owningPlugin = plugin;
            this.registeredWith = registeredWith;
            if (perms.length > 0)
                setPermissions(perms);
            if (!StringUtils.isEmpty(permMessage))
                setPermissionMessage(ChatColor.RED + permMessage);
        }

        @Override
        public boolean execute(CommandSender sender, String label, String[] args) {
            boolean hasPerm = false;
            for (String perm : permissions) {
                if (sender.hasPermission(perm))
                    hasPerm = true;
            }
            if (!hasPerm) {
                sender.sendMessage(getPermissionMessage());
                return false;
            }
            return owner.onCommand(sender, this, label, args);
        }

        public void setPermissions(String[] permissions) {
            this.permissions = permissions;
            super.setPermission(StringUtils.join(permissions, ";"));
        }

        @Override
        public Plugin getPlugin() {
            return owningPlugin;
        }
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Command {

        public String name();

        public String usage() default "/<command>";

        public String[] alias() default {};

        public Sender sender() default Sender.PLAYER;

        public String[] permission() default "";

        public String permissionMessage() default "You do not have the permission to execute this command!";
    }

    public enum Sender {
        CONSOLE(new Class[]{ConsoleCommandSender.class}),
        PLAYER(new Class[]{Player.class}),
        EVERYONE(new Class[]{ConsoleCommandSender.class, Player.class});

        Sender(Class[] who) {
        }
    }
}