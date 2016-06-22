package com.nanoark.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;
import com.nanoark.utilities.MongoDB;

/**
 * Represents database collection where templates for images are stored.
 *
 * @author Vino Sugunan
 */
public class TemplateDAO {
   private static DBCollection dao = MongoDB.getCollection("template");

   public static void insert(String name, long Height, long Width) {
      BasicDBObject insert = new BasicDBObject("_id", name);
      insert.append("Height", Height);
      insert.append("Width", Width);
      dao.save(insert, WriteConcern.JOURNALED);
   }

   public static String getVal(String template, String key) {
      BasicDBObject query = new BasicDBObject("_id", template);
      BasicDBObject filter = new BasicDBObject(key, 1);
      return dao.findOne(query, filter).get(key) + "";
   }

   public static void setVal(String template, String key, String val) {
      BasicDBObject query = new BasicDBObject("_id", template);
      BasicDBObject set = new BasicDBObject(key, val);
      BasicDBObject update = new BasicDBObject("$set", set);
      dao.update(query, update, false, false, WriteConcern.JOURNALED);
   }

   public static void remove(String template) {
      BasicDBObject query = new BasicDBObject("_id", template);
      dao.remove(query, WriteConcern.JOURNALED);
   }
}
