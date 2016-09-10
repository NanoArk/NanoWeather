package com.nanoark.dao;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.nanoark.utilities.Log;
import com.nanoark.utilities.MongoDB;
import com.nanoark.utilities.RunOCR;

/**
 * Represents database collection where image fields are stored.
 *
 * @author Vino Sugunan
 */
public class ImageFieldDAO {
   /** Data access object connecting to the ImageField collection. */
   private static DBCollection dao = MongoDB.getCollection("imageField");
   private static Logger       log = Log.logger();

   /**
    * Adds a field to an image within NanoWeather.
    *
    * @param image identifies the image this field exists within.
    * @param field specifies an identifier for this field within this image.
    * @param x the distance in pixels of this field from the left edge.
    * @param y the distance in pixels of this field from the top edge.
    * @param height field height in pixels.
    * @param width field width in pixels.
    * @param highThresh confidence above which this field is considered high accuracy.
    * @param lowThresh confidence below which this field is considered low accuracy.
    * @throws IOException
    */
   public static void insert(String image, String field, int x, int y, int height, int width, int highThresh,
      int lowThresh) throws IOException {
      String location = ImageDAO.getVal(image, "location");
      BasicDBObject query = new BasicDBObject("_id", image + "-" + field);
      query.append("image", image);
      query.append("field", field);
      query.append("x", x);
      query.append("y", y);
      query.append("height", height);
      query.append("width", width);
      query.append("highThresh", highThresh);
      query.append("lowThresh", lowThresh);
      dao.save(query, WriteConcern.JOURNALED);
      log.info("Saved provided data for " + image + "-" + field);
      new RunOCR(image, field, location, x, y, width, height).start();
      log.info("Kicked OCR thread for " + image + "-" + field);
   }

   public static void insert(String image, String field, int x, int y, int height, int width, int highThresh,
      int lowThresh, int charSet) {
      String location = ImageDAO.getVal(image, "location");
      BasicDBObject query = new BasicDBObject("_id", image + "-" + field);
      query.append("image", image);
      query.append("field", field);
      query.append("x", x);
      query.append("y", y);
      query.append("height", height);
      query.append("width", width);
      query.append("highThresh", highThresh);
      query.append("lowThresh", lowThresh);
      query.append("charSet", charSet);
      dao.save(query, WriteConcern.JOURNALED);
      log.info("Saved provided data (with charset) for " + image + "-" + field);
      new RunOCR(image, field, location, x, y, width, height).start();
      log.info("Kicked OCR thread (with charset) for " + image + "-" + field);
   }

   public static DBObject getImageField(String image, String field) {
      BasicDBObject query = new BasicDBObject("_id", image + "-" + field);
      query.append("image", image);
      query.append("field", field);
      return dao.findOne(query);
   }

   public static String getVal(String image, String field, String key) {
      BasicDBObject query = new BasicDBObject("_id", image + "-" + field);
      BasicDBObject filter = new BasicDBObject(key, 1);
      return dao.findOne(query, filter).get(key) + "";
   }

   public static void setVal(String image, String field, String key, String val) {
      BasicDBObject query = new BasicDBObject("_id", image + "-" + field);
      BasicDBObject set = new BasicDBObject(key, val);
      BasicDBObject update = new BasicDBObject("$set", set);
      dao.update(query, update, false, false, WriteConcern.JOURNALED);
      log.info("Ran setVal(field) update: " + image + "-" + field + "-" + key + "-" + val);
   }

   public static LinkedList<String> getFields(String image) {
      BasicDBObject query = new BasicDBObject("image", image);
      BasicDBObject filter = new BasicDBObject("field", 1);
      DBCursor results = dao.find(query, filter);
      LinkedList<String> fields = new LinkedList<String>();
      while (results.hasNext()) {
         fields.addLast(results.next().get("field").toString());
      }
      results.close();
      return fields;
   }

   public static LinkedList<String> getHighConfidence(String image) {
      BasicDBObject query = new BasicDBObject("image", image);
      BasicDBObject filter = new BasicDBObject("field", 1);
      DBCursor results = dao.find(query, filter);
      LinkedList<String> fields = new LinkedList<String>();
      while (results.hasNext()) {
         DBObject next = results.next();
         int confidence = Integer.parseInt(next.get("confidence") + "");
         int highThresh = Integer.parseInt(next.get("highThresh") + "");
         if(confidence > highThresh) {
            fields.addLast(results.next().get("field").toString());
         }
      }
      results.close();
      return fields;
   }

   public static LinkedList<String> getMedConfidence(String image) {
      BasicDBObject query = new BasicDBObject("image", image);
      BasicDBObject filter = new BasicDBObject("field", 1);
      DBCursor results = dao.find(query, filter);
      LinkedList<String> fields = new LinkedList<String>();
      while (results.hasNext()) {
         DBObject next = results.next();
         int confidence = Integer.parseInt(next.get("confidence") + "");
         int highThresh = Integer.parseInt(next.get("highThresh") + "");
         int lowThresh = Integer.parseInt(next.get("lowThresh") + "");
         if(highThresh > confidence && confidence > lowThresh) {
            fields.addLast(results.next().get("field").toString());
         }
      }
      results.close();
      return fields;
   }

   public static LinkedList<String> getLowConfidence(String image) {
      BasicDBObject query = new BasicDBObject("image", image);
      BasicDBObject filter = new BasicDBObject("field", 1);
      DBCursor results = dao.find(query, filter);
      LinkedList<String> fields = new LinkedList<String>();
      while (results.hasNext()) {
         DBObject next = results.next();
         int confidence = Integer.parseInt(next.get("confidence") + "");
         int lowThresh = Integer.parseInt(next.get("lowThresh") + "");
         if(lowThresh > confidence) {
            fields.addLast(results.next().get("field").toString());
         }
      }
      results.close();
      return fields;
   }

   public static void remove(String image, String field) {
      BasicDBObject query = new BasicDBObject("_id", image + "-" + field);
      dao.remove(query, WriteConcern.JOURNALED);
      log.info("Removed: " + image + "-" + field);
   }

   public static void applyTemplate(String template) {
      // TODO Auto-generated method stub
   }

   public static void recenterField(String image, String template, int x, int y) {
      // TODO Auto-generated method stub
   }

   public static void recenterTemplate(String image, String template, int x, int y) {
      // TODO Auto-generated method stub
   }

   public static String isOCRed(String image) {
      BasicDBObject query = new BasicDBObject("image", image);
      BasicDBObject isNull = new BasicDBObject("ocrVal", new BasicDBObject("$exists", false));
      BasicDBObject isEmpty = new BasicDBObject("ocrVal", "");
      BasicDBList or = new BasicDBList();
      or.add(isNull);
      or.add(isEmpty);
      long total = dao.count(query);
      query.append("$or", or);
      long notOCRed = dao.count(query);
      long done = 0;
      if(total > 0) {
         done = (total - notOCRed) / total;
      }
      return done + "";
   }

   public static String isOCRed(String image, String field) {
      BasicDBObject query = new BasicDBObject("_id", image + "-" + field);
      BasicDBObject isNull = new BasicDBObject("ocrVal", new BasicDBObject("$exists", false));
      BasicDBObject isEmpty = new BasicDBObject("ocrVal", "");
      BasicDBList or = new BasicDBList();
      or.add(isNull);
      or.add(isEmpty);
      long total = dao.count(query);
      query.append("$or", or);
      long notOCRed = dao.count(query);
      long done = 0;
      if(total > 0) {
         done = (total - notOCRed) / total;
      }
      return done + "";
   }
}
