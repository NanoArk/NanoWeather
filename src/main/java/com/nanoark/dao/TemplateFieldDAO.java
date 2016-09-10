package com.nanoark.dao;

import java.util.LinkedList;
import java.util.logging.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteConcern;
import com.nanoark.utilities.Log;
import com.nanoark.utilities.MongoDB;

/**
 * @author Vino Sugunan
 */
public class TemplateFieldDAO {
   private static final Logger log = Log.logger();
   private static DBCollection dao = MongoDB.getCollection("templateField");

   public static void updateCharSet(String template, String field, String charSet) {
      BasicDBObject query = new BasicDBObject("template", template);
      query.append("field", field);
      BasicDBObject set = new BasicDBObject("charSet", charSet);
      BasicDBObject update = new BasicDBObject("$set", set);
      dao.update(query, update, false, true);
      log.info("Updated template field charSet:" + template + "-" + field + "-" + charSet);
   }

   public static void insert(String template, String field, int x, int y, int height, int width, int highThresh,
      int lowThresh) {
      BasicDBObject query = new BasicDBObject("_id", template + "-" + field);
      query.append("template", template);
      query.append("field", field);
      query.append("x", x);
      query.append("y", y);
      query.append("height", height);
      query.append("width", width);
      query.append("highThresh", highThresh);
      query.append("lowThresh", lowThresh);
      dao.save(query, WriteConcern.JOURNALED);
      log.info("Saved provided template data for " + template + "-" + field);
   }

   public static void insert(String template, String field, int x, int y, int height, int width, int highThresh,
      int lowThresh, String charSet) {
      BasicDBObject query = new BasicDBObject("_id", template + "-" + field);
      query.append("template", template);
      query.append("field", field);
      query.append("x", x);
      query.append("y", y);
      query.append("height", height);
      query.append("width", width);
      query.append("highThresh", highThresh);
      query.append("lowThresh", lowThresh);
      query.append("charSet", charSet);
      dao.save(query, WriteConcern.JOURNALED);
      log.info("Saved provided template data (with charset) for " + template + "-" + field);
   }

   public static String getVal(String template, String field, String key) {
      BasicDBObject query = new BasicDBObject("_id", template + "-" + field);
      BasicDBObject filter = new BasicDBObject(key, 1);
      return dao.findOne(query, filter).get(key) + "";
   }

   public static void setVal(String template, String field, String key, String val) {
      BasicDBObject query = new BasicDBObject("_id", template + "-" + field);
      BasicDBObject set = new BasicDBObject(key, val);
      BasicDBObject update = new BasicDBObject("$set", set);
      dao.update(query, update, false, false, WriteConcern.JOURNALED);
      log.info("Ran setVal(template field) update: " + template + "-" + field + "-" + key + "-" + val);
   }

   public static LinkedList<String> getFields(String template) {
      BasicDBObject query = new BasicDBObject("template", template);
      BasicDBObject filter = new BasicDBObject("field", 1);
      DBCursor results = dao.find(query, filter);
      LinkedList<String> fields = new LinkedList<String>();
      while (results.hasNext()) {
         fields.addLast(results.next().get("field").toString());
      }
      results.close();
      return fields;
   }

   public static void remove(String template, String field) {
      BasicDBObject query = new BasicDBObject("_id", template + "-" + field);
      dao.remove(query, WriteConcern.JOURNALED);
      log.info("Removed template: " + template + "-" + field);
   }
}
