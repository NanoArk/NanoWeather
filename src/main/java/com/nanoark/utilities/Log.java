package com.nanoark.utilities;
import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Maintains logs to simplify debugging software. 9999-OFF - No data, not a log level. 1000-SEVERE -
 * Errors. 0900-WARNING - Gracefully bypassed errors. 0800-INFO - Program Output. 0700-CONFIG -
 * Settings (global variables). 0500-FINE - General Actions (methods). 0400-FINER - Specific Actions
 * (loops). 0300-FINEST - Line by line code information. 0000-ALL - All data, not a log level.
 *
 * @author Vino Sugunan
 */
public class Log {
   /** Location to store logs. */
   private static final String        logFolder = "logs/";
   /** Log level to print to System.out */
   private static final Level         console   = Level.ALL;
   /** Log level printed to System.err */
   private static final Level         error     = Level.OFF;
   /**
    * LogFile objects used to determine how logs are stored.
    * ALL - 200 files at 1 MB apiece list all logging output.
    * INFO - 200 files at 1 MB apiece list server requests.
    * WARNING - 20 files at 1 MB apiece list errors requiring developer attention.
    */
   private static final Log.LogFile[] logFiles  = {new LogFile(Level.ALL, 1, 3, true),
      new LogFile(Level.FINE, 1, 500, false), new LogFile(Level.INFO, 1, 500, false),
      new LogFile(Level.WARNING, 1, 5, true)};

   /**
    * Describes how log files will be generated.
    *
    * @author Vino Sugunan
    */
   private static class LogFile {
      /** Logging level which will be included in this output. */
      public final Level   level;
      /** The amount of memory allocated to each individual log file. */
      public final int     size;
      /** The number of log files to be kept before old files rotate out. */
      public final int     count;
      /** If true, then will continue old log files when software is stopped/started. */
      public final boolean append;
      //
      /** Base name for this set of log files. */
      public final String  name;

      /**
       * Determines what log data to store in files.
       *
       * @param level The level of data stored in the log.
       * @param size The size in MB of each log (accepts decimals).
       * @param count The number of logs to keep.
       * @param append Determines if old logs are continued on re-boot.
       */
      public LogFile(Level level, double size, int count, boolean append) {
         this.level = level;
         this.size = (int) (size * 1024000);
         this.count = count;
         this.append = append;
         name = "/" + level + " log %g.txt";
      }
   }

   /** This logger will be used as a parent of the loggers this class produces. */
   private static final Logger mainLogger;

   static {
      // Basic logger setup.
      LogManager.getLogManager().reset();
      mainLogger = Logger.getLogger("Main Logger");
      mainLogger.setUseParentHandlers(false);
      mainLogger.setLevel(Level.ALL);
      // Add System.out handler if one is used.
      if(console != Level.OFF) {
         StreamHandler sh = new StreamHandler(System.out, new SimpleFormatter());
         sh.setLevel(console);
         mainLogger.addHandler(sh);
      }
      // Add System.err handler if one is used.
      if(error != Level.OFF) {
         ConsoleHandler ch = new ConsoleHandler();
         ch.setLevel(error);
         mainLogger.addHandler(ch);
      }
      try {
         // Make folder for logs if none exists.
         File file = new File(logFolder);
         if( !file.exists()) {
            file.mkdir();
         }
         mainLogger.info("Logs are stored at: " + file.getAbsolutePath());
         // Add requested file handlers and find age of oldest log file.
         long oldest = new File(logFolder).lastModified();
         for (LogFile logFile : logFiles) {
            FileHandler fh = new FileHandler(file + logFile.name, logFile.size, logFile.count,
               logFile.append);
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(logFile.level);
            mainLogger.addHandler(fh);
            String loc = logFolder + "/" + logFile.level + " log ";
            File f = null;
            int i = logFile.count;
            while ( !(f = new File(loc + --i + ".txt")).exists()) {/* Find last log file. */
            }
            long age = f.lastModified();
            if(age < oldest) {
               oldest = age;
            }
         }
         // Delete any file older than the oldest log.
         deleteOld(file, oldest);
      } catch (IOException e) {
         mainLogger.severe(getError(e));
      }
   }

   /**
    * Returns a logger which will print/save logs as specified above.
    *
    * @return Logger object configured by this class.
    */
   public static Logger logger() {
      Logger logger = Logger.getLogger("Logger");
      logger.setParent(mainLogger);
      return logger;
   }

   /**
    * Converts an exception into an easily read string.
    *
    * @param e The exception to be converted.
    * @return The string representing that error.
    */
   public static String getError(Exception e) {
      String error = "";
      error += e.toString() + "\n";
      StackTraceElement[] st = e.getStackTrace();
      for (StackTraceElement pos : st) {
         error += pos.toString() + "\n";
      }
      return error;
   }

   /**
    * Deletes any files older than the specified max age.
    *
    * @param file File/directory to be checked.
    * @param maxAge Max age of file in milliseconds after 00:00:00GMT 1/1/1970
    */
   private static void deleteOld(File file, long maxAge) {
      if(file.isDirectory()) {
         for (File f : file.listFiles()) {
            deleteOld(f, maxAge);
         }
      }
      if(file.lastModified() < maxAge) {
         file.delete();
      }
   }
}
