package com.nanoark.utilities;

import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;

/**
 * @author Vino Sugunan
 */
public class MongoDB {
   /** Logger for recording system state changes. */
   private static final Logger log   = Log.logger();
   /** Database connection (NanoWeather database) */
   public static DB            db;
   /** Mongo connection used to establish the database connection. */
   private static Mongo        mongo = null;

   static {
      try {
         MongoOptions options = new MongoOptions();
         options.autoConnectRetry = true;
         options.connectionsPerHost = 40;
         options.connectTimeout = 60000;
         options.maxAutoConnectRetryTime = 120000;
         options.maxWaitTime = 120000;
         options.socketKeepAlive = true;
         options.socketTimeout = 600000;
         // connect to the database
         mongo = new Mongo("localhost", options);
         // Use DB.
         db = mongo.getDB("nanoWeather");
      } catch (UnknownHostException e) {
         log.severe(Log.getError(e));
      }
   }

   /**
    * Creates a connection to the specified collection within the database.
    *
    * @param collectionName to establish a connection for in the database.
    * @return a connection representing this collection within the database.
    */
   public static DBCollection getCollection(String collectionName) {
      return db.getCollection(collectionName);
   }

   /**
    * Closes all connections to the database.
    */
   public static void closeConnection() {
      mongo.close();
   }
}
