package com.nanoark.dao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.nanoark.utilities.Log;
import com.nanoark.utilities.MongoDB;

/**
 * Represents database collection where image information is stored.
 *
 * @author Vino Sugunan
 */
public class ImageDAO {
   private static DBCollection dao = MongoDB.getCollection("image");
   private static Logger       log = Log.logger();

   /**
    * Adds an image to NanoWeather.
    *
    * @param name specifies an identifier for this image.
    * @param location specify the exact file location of this image.
    * @param height image height in pixels.
    * @param width image width in pixels.
    */
   public static String insert(String name, String location, int height, int width) {
      String reply = "IOException see server for error details.";
      try {
         BufferedImage imgBuf = ImageIO.read(new File(location));
         int imgH = imgBuf.getHeight();
         int imgW = imgBuf.getWidth();
         if(height != imgH || width != imgW) {
            reply = "Invalid width/height, provided: " + height + "x" + width + " measured: " + imgH + "x" + imgW;
            log.warning(reply);
         } else {
            BasicDBObject insert = new BasicDBObject("_id", name);
            insert.append("location", location);
            insert.append("height", height);
            insert.append("width", width);
            dao.save(insert, WriteConcern.JOURNALED);
            reply = "Added " + height + "x" + width + " image: " + name + " from " + location;
            log.info(reply);
         }
      } catch (IOException e) {
         log.severe(Log.getError(e));
      }
      return reply;
   }

   public static DBObject getImage(String image) {
      BasicDBObject query = new BasicDBObject("_id", image);
      query.append("image", image);
      return dao.findOne(query);
   }

   public static String getVal(String image, String key) {
      BasicDBObject query = new BasicDBObject("_id", image);
      BasicDBObject filter = new BasicDBObject(key, 1);
      return dao.findOne(query, filter).get(key) + "";
   }

   public static void setVal(String image, String key, String val) {
      BasicDBObject query = new BasicDBObject("_id", image);
      BasicDBObject set = new BasicDBObject(key, val);
      BasicDBObject update = new BasicDBObject("$set", set);
      dao.update(query, update, false, false, WriteConcern.JOURNALED);
   }

   public static void remove(String image) {
      BasicDBObject remove = new BasicDBObject("_id", image);
      dao.remove(remove);
   }
}
