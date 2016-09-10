package com.nanoark.dao;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
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
    * @throws IOException
    */
   public static void insert(String name, String location) throws Exception {
      BufferedImage imgBuf = ImageIO.read(new URL(location));
      int imgH = imgBuf.getHeight();
      int imgW = imgBuf.getWidth();
      BasicDBObject insert = new BasicDBObject("_id", name);
      insert.append("location", location);
      insert.append("height", imgH);
      insert.append("width", imgW);
      dao.save(insert, WriteConcern.JOURNALED);
      log.info("Saved provided data for " + name);
   }

   public static String getFileExtension(String filepath) {
      String ext = "";
      int i = filepath.lastIndexOf('.');
      if(i > 0) {
         ext = filepath.substring(i);
      }
      return ext;
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
      log.info("Ran setVal(image) update: " + image + "-" + key + "-" + val);
   }

   public static void remove(String image) {
      BasicDBObject remove = new BasicDBObject("_id", image);
      dao.remove(remove);
      log.info("Removed: " + image);
   }
}
