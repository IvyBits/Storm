package tk.ivybits.storm.utility;

import com.google.common.io.Files;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Custom pluginLogger to save errors. Multiple-instance safe!
 *
 * @author Icyene, Xiaomao
 */

public class ErrorLogger {
    protected static final String[] sowwy = new String[]{
            "Who set us up the TNT?",
            "Everything\'s going to plan. No, really, that was supposed to happen.",
            "Uh... Did I do that?", "Oops.",
            "Why did you do that?",
            "I feel sad now :(",
            "My bad.",
            "I\'m sorry, Dave.",
            "I let you down. Sorry :(",
            "On the bright side, I bought you a teddy bear!",
            "Daisy, daisy...",
            "Oh - I know what I did wrong!",
            "Hey, that tickles! Hehehe!",
            "Don\'t be sad. I\'ll do better next time, I promise!",
            "Don\'t be sad, have a hug! <3",
            "I just don\'t know what went wrong :(",
            "Shall we play a game?",
            "Quite honestly, I wouldn\'t worry myself about that.",
            "Surprise! Haha. Well, this is awkward.",
            "Would you like a cupcake?",
            "Ooh. Shiny.", "This doesn\'t make any sense!",
            "Why is it breaking :(", "Don\'t do that.",
            "Ouch. That hurt :(",
            "You\'re mean.",
            "There are four lights!"};
    private static ErrorLogger errors;
    private String pack;
    private Plugin plugin;
    private Logger log;

    private ErrorLogger(Plugin context, String packg) {
        log = Logger.getLogger(context.getName());
        plugin = context;
        pack = packg;
        Logger log;
    }

    public static void register(Plugin context, String packag) {
        errors = new ErrorLogger(context, packag);
        System.setOut(new InterceptedStream(System.err));
    }

    public static void alert(Throwable thorn) {
        if (errors != null) {
            LogRecord screw = new LogRecord(Level.SEVERE, null);
            screw.setMessage("Bukkit did not catch this, so no additional info is available.");
            screw.setThrown(thorn);
            errors.generateErrorLog(screw);
        } else {
            thorn.printStackTrace();
        }
    }

    public void log(LogRecord logRecord) {
        if (!generateErrorLog(logRecord))
            log.log(logRecord);
    }

    private boolean alert(String traceback, Throwable thorn) {
        String name = errors.plugin.getName();

        if (!traceback.contains(pack) || (!traceback.contains(name + " has encountered an error") && traceback.contains(ErrorLogger.class.getName())))
            //Check if its not our own. If it is, return, to not cause a StackOverflow
            return true;

        System.err.println(name + " encountered an error: " + traceback);
        StringBuilder err = new StringBuilder();

        err.append("---- Minecraft Crash Report ----\n");
        err.append("// ");
        try {
            err.append(sowwy[(int) (System.nanoTime() % sowwy.length)]);
        } catch (Throwable throwable) {
            err.append("Witty comment unavailable :(");
        }
        err.append("\n\n");
        err.append("Time: ");
        err.append((new SimpleDateFormat()).format(new Date()));
        err.append("\n");
        err.append("Description: ");
        err.append(plugin.getName() + " has encountered an error! What follows is the stacktrace of the current thread: ");
        err.append("\n\n");
        err.append(traceback);
        err.append("\n\nDetails:\n\n");
        err.append("Minecraft Version: " + Bukkit.getBukkitVersion());
        err.append("Operating System: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")");
        err.append("Java Version: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
        err.append("Java VM Version: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
        try {
            File dump;
            Files.write(err.toString(), dump = new File(
                    new File(plugin.getDataFolder(), "errors").getAbsoluteFile(),
                    String.format("%s_%s.error.log",
                            thorn != null ? thorn.getClass().getSimpleName() : "Unknown",                   //Get first 6 chars of hash
                            new BigInteger(1, Arrays.copyOfRange(MessageDigest.getInstance("MD5").digest(err.toString().getBytes()), 0, 6)).toString().substring(0, 6)
                    )
            ), Charset.defaultCharset());
            System.err.println("Don't despair! This error has been saved to '.\\" +
                    plugin.getDataFolder().getName() + "\\errors\\" + dump.getName() +
                    "'. You should report it to the developers of " + plugin.getName() + ": " +
                    plugin.getDescription().getAuthors() + ".\n");
        } catch (Exception e) {
            err.append("\nErrors occured while saving to file. Not saved.");
        }
        return false;
    }

    private boolean generateErrorLog(LogRecord record) {
        Throwable thorn;
        if ((thorn = record.getThrown()) == null)
            return false;

        return alert(ExceptionUtils.getStackTrace(thorn), thorn);
    }

    static {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                alert(throwable);
            }
        });
    }

    public static class InterceptedStream extends PrintStream {
        public InterceptedStream(OutputStream out) {
            super(out);
        }

        @Override
        public void println(String str) {
            if (errors.alert(str, null)) {
                super.println(str);
            }
        }

        @Override
        public void print(String str) {
            if (errors.alert(str, null)) {
                super.print(str);
            }
        }
    }
}