package com.nanoark;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.nanoark.dao.ImageDAO;
import com.nanoark.dao.ImageFieldDAO;
import com.nanoark.dao.TemplateDAO;
import com.nanoark.dao.TemplateFieldDAO;
import com.nanoark.utilities.Log;

/**
 * HTML interface for Java programs using the NanoWeather plug-in.
 *
 * @author Vino Sugunan
 */
@Path("/NanoWeather")
public class NanoWeather {
   private static Logger log = Log.logger();

   /**
    * Adds an image to NanoWeather.
    *
    * @param image specifies an identifier for this image.
    * @param location specify the exact file location of this image.
    * @param height image height in pixels.
    * @param width image width in pixels.
    * @return A brief string describing the image which was added.
    */
   @GET
   @Path("/addImage/{image}/{location: .*}/{height}/{width}")
   public static String addImage(@PathParam("image") String image, @PathParam("location") String location,
      @PathParam("height") int height, @PathParam("width") int width) {
      log.info("Adding image: " + image);
      return ImageDAO.insert(image, location, height, width);
   }

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
    * @return A brief string describing the field which was added.
    */
   @GET
   @Path("/addImageField/{image}/{field}/{x}/{y}/{height}/{width}/{highThresh}/{lowThresh}")
   public static String addImageField(@PathParam("image") String image, @PathParam("field") String field,
      @PathParam("x") int x, @PathParam("y") int y, @PathParam("height") int height, @PathParam("width") int width,
      @PathParam("highThresh") int highThresh, @PathParam("lowThresh") int lowThresh) {
      ImageFieldDAO.insert(image, field, x, y, height, width, highThresh, lowThresh);
      return "Added " + height + "x" + width + " field: " + field + " to image: " + image + " at (" + x + "," + y
         + ") with a high threshhold of " + highThresh + " and a low threshhold of " + lowThresh;
   }

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
    * @param charSet determines which characters are acceptable as OCR results.
    * @return A brief string describing the field which was added.
    */
   @GET
   @Path("/addImageFieldWithCharSet/{image}/{field}/{x}/{y}/{height}/{width}/{highThresh}/{lowThresh}/{charSet}")
   public static String addImageField(@PathParam("image") String image, @PathParam("field") String field,
      @PathParam("x") int x, @PathParam("y") int y, @PathParam("height") int height, @PathParam("width") int width,
      @PathParam("highThresh") int highThresh, @PathParam("lowThresh") int lowThresh,
      @PathParam("charSet") int charSet) {
      ImageFieldDAO.insert(image, field, x, y, height, width, highThresh, lowThresh, charSet);
      return "Added " + height + "x" + width + " field: " + field + " to image: " + image + " at (" + x + "," + y
         + ") with a high threshhold of " + highThresh + " and a low threshhold of " + lowThresh + " with char set: "
         + charSet;
   }

   /**
    * Adds a template to NanoWeather.
    *
    * @param template specifies an identifier for this template.
    * @param height template height in pixels.
    * @param width template width in pixels.
    * @return A brief string describing the template which was added.
    */
   @GET
   @Path("/addTemplate/{template}/{height}/{width}")
   public static String addTemplate(@PathParam("template") String template, @PathParam("height") int height,
      @PathParam("width") int width) {
      TemplateDAO.insert(template, height, width);
      return "Added " + height + "x" + width + " template: " + template;
   }

   /**
    * Adds a field to a template within NanoWeather.
    *
    * @param template identifies the template this field exists within.
    * @param field specifies an identifier for this field within this template.
    * @param x the distance in pixels of this field from the left edge.
    * @param y the distance in pixels of this field from the top edge.
    * @param height field height in pixels.
    * @param width field width in pixels.
    * @param highThresh confidence above which this field is considered high accuracy.
    * @param lowThresh confidence below which this field is considered low accuracy.
    * @return A brief string describing the template field which was added.
    */
   @GET
   @Path("/addTemplateField/{template}/{field}/{x}/{y}/{height}/{width}/{highThresh}/{lowThresh}")
   public static String addTemplateField(@PathParam("template") String template, @PathParam("field") String field,
      @PathParam("x") int x, @PathParam("y") int y, @PathParam("height") int height, @PathParam("width") int width,
      @PathParam("highThresh") int highThresh, @PathParam("lowThresh") int lowThresh) {
      TemplateFieldDAO.insert(template, field, x, y, height, width, highThresh, lowThresh);
      return "Added " + height + "x" + width + " field to template: " + template + " at (" + x + "," + y
         + ") with high threshhold " + highThresh + " and low threshhold " + lowThresh;
   }

   /**
    * Adds a field to a template within NanoWeather.
    *
    * @param template identifies the template this field exists within.
    * @param field specifies an identifier for this field within this template.
    * @param x the distance in pixels of this field from the left edge.
    * @param y the distance in pixels of this field from the top edge.
    * @param height field height in pixels.
    * @param width field width in pixels.
    * @param highThresh confidence above which this field is considered high accuracy.
    * @param lowThresh confidence below which this field is considered low accuracy.
    * @param charSet determines which characters are acceptable as OCR results.
    * @return A brief string describing the template field which was added.
    */
   @GET
   @Path("/addTemplateFieldWithCharSet/{template}/{field}/{x}/{y}/{height}/{width}/{highThresh}/{lowThresh}/{charSet}")
   public static String addTemplateField(@PathParam("template") String template, @PathParam("field") String field,
      @PathParam("x") int x, @PathParam("y") int y, @PathParam("height") int height, @PathParam("width") int width,
      @PathParam("highThresh") int highThresh, @PathParam("lowThresh") int lowThresh,
      @PathParam("charSet") String charSet) {
      TemplateFieldDAO.insert(template, field, x, y, height, width, highThresh, lowThresh, charSet);
      return "Added " + height + "x" + width + " field to template: " + template + " at (" + x + "," + y
         + ") with high threshhold " + highThresh + " and low threshhold " + lowThresh + " with char set: " + charSet;
   }

   /**
    * Gets the location of an image within NanoWeather.
    *
    * @param image specifies the image to get the location of.
    * @return location of the image.
    */
   @GET
   @Path("/getImageLocation/{image}")
   public static String getImageLocation(@PathParam("image") String image) {
      return ImageDAO.getVal(image, "location");
   }

   /**
    * Gets the height of an image within NanoWeather.
    *
    * @param image specifies the image to get the height of.
    * @return height of the image.
    */
   @GET
   @Path("/getImageHeight/{image}")
   public static String getImageHeight(@PathParam("image") String image) {
      return Integer.parseInt(ImageDAO.getVal(image, "height")) + "";
   }

   /**
    * Gets the width of an image within NanoWeather.
    *
    * @param image specifies the image to get the width of.
    * @return width of the image.
    */
   @GET
   @Path("/getImageWidth/{image}")
   public static String getImageWidth(@PathParam("image") String image) {
      return Integer.parseInt(ImageDAO.getVal(image, "width")) + "";
   }

   /**
    * Gets the distance between a field and the left edge of the image containing it.
    *
    * @param image specifies the image the field is in.
    * @param field specifies the field to get the horizontal location of.
    * @return distance in pixels between the field and the left edge of the image.
    */
   @GET
   @Path("/getImageFieldX/{image}/{field}")
   public static String getImageFieldX(@PathParam("image") String image, @PathParam("field") String field) {
      return Integer.parseInt(ImageFieldDAO.getVal(image, field, "x")) + "";
   }

   /**
    * Gets the distance between a field and the top edge of the image containing it.
    *
    * @param image specifies the image the field is in.
    * @param field specifies the field to get the vertical location of.
    * @return distance in pixels between the field and the top edge of the image.
    */
   @GET
   @Path("/getImageFieldY/{image}/{field}")
   public static String getImageFieldY(@PathParam("image") String image, @PathParam("field") String field) {
      return Integer.parseInt(ImageFieldDAO.getVal(image, field, "y")) + "";
   }

   /**
    * Gets the height of a field on an image within NanoWeather.
    *
    * @param image specifies the image the field is in.
    * @param field specifies the field to get the height of.
    * @return height in pixels of the field on the specified image.
    */
   @GET
   @Path("/getImageFieldHeight/{image}/{field}")
   public static String getImageFieldHeight(@PathParam("image") String image, @PathParam("field") String field) {
      return Integer.parseInt(ImageFieldDAO.getVal(image, field, "height")) + "";
   }

   /**
    * Gets the width of a field on an image within NanoWeather.
    *
    * @param image specifies the image the field is in.
    * @param field specifies the field to get the height of.
    * @return width in pixels of the field on the specified image.
    */
   @GET
   @Path("/getImageFieldWidth/{image}/{field}")
   public static String getImageFieldWidth(@PathParam("image") String image, @PathParam("field") String field) {
      return Integer.parseInt(ImageFieldDAO.getVal(image, field, "width")) + "";
   }

   /**
    * Gets the character set of a field within a specific image.
    *
    * @param image specifies the image the field is in.
    * @param field specifies the field to get the char set of.
    * @return character set restricting this field.
    */
   @GET
   @Path("/getImageFieldCharSet/{image}/{field}")
   public static String getImageFieldCharSet(@PathParam("image") String image, @PathParam("field") String field) {
      return ImageFieldDAO.getVal(image, field, "charSet");
   }

   /**
    * Gets the high threshold for a specified field within a image.
    *
    * @param image specifies the image the field is in.
    * @param field specifies the field to get the high threshold of.
    * @return high threshold for OCR confidence in this field.
    */
   @GET
   @Path("/getImageFieldHighThresh/{image}/{field}")
   public static String getImageFieldHighThresh(@PathParam("image") String image, @PathParam("field") String field) {
      return Integer.parseInt(ImageFieldDAO.getVal(image, field, "highThresh")) + "";
   }

   /**
    * Gets the low threshold for a specified field within a image.
    *
    * @param image specifies the image the field is in.
    * @param field specifies the field to get the low threshold of.
    * @return low threshold for OCR confidence in this field.
    */
   @GET
   @Path("/getImageFieldLowThresh/{image}/{field}")
   public static String getImageFieldLowThresh(@PathParam("image") String image, @PathParam("field") String field) {
      return Integer.parseInt(ImageFieldDAO.getVal(image, field, "lowThresh")) + "";
   }

   /**
    * Gets the OCR text value for a specified field within a image.
    *
    * @param image specifies the image the field is in.
    * @param field specifies the field to get the OCR text value of.
    * @return OCR text value for this field.
    */
   @GET
   @Path("/getImageFieldOCRVal/{image}/{field}")
   public static String getImageFieldOCRVal(@PathParam("image") String image, @PathParam("field") String field) {
      return ImageFieldDAO.getVal(image, field, "ocrVal");
   }

   /**
    * Gets the final text value for a specified field within a image.
    *
    * @param image specifies the image the field is in.
    * @param field specifies the field to get the final text value of.
    * @return final text value for this field.
    */
   @GET
   @Path("/getImageFieldFinalVal/{image}/{field}")
   public static String getImageFieldFinalVal(@PathParam("image") String image, @PathParam("field") String field) {
      return ImageFieldDAO.getVal(image, field, "finalVal");
   }

   /**
    * Gets the confidence level of the OCR job run on a specific field within an image.
    *
    * @param image specifies the image the field is in.
    * @param field specifies the field to get the confidence level of.
    * @return confidence level for OCR job on this field.
    */
   @GET
   @Path("/getImageFieldConfidence/{image}/{field}")
   public static String getImageFieldConfidence(@PathParam("image") String image, @PathParam("field") String field) {
      return ImageFieldDAO.getVal(image, field, "confidence") + "";
   }

   /**
    * Gets all fields for a given image.
    *
    * @param image to retrieve fields for.
    * @return all fields in this image.
    */
   @GET
   @Path("/getImageFields/{image}")
   public static String getImageFields(@PathParam("image") String image) {
      return ImageFieldDAO.getFields(image) + "";
   }

   /**
    * Gets all fields whose confidence was above their high threshold for a given image.
    *
    * @param image to retrieve fields for.
    * @return all fields whose confidence was above their high threshold for this image.
    */
   @GET
   @Path("/getImageHighConfidenceFields/{image}")
   public static String getImageHighConfidenceFields(@PathParam("image") String image) {
      return ImageFieldDAO.getHighConfidence(image) + "";
   }

   /**
    * Gets all fields whose confidence was between their high and low threshold for a given image.
    *
    * @param image to retrieve fields for.
    * @return all fields whose confidence was between their high and low threshold for this image.
    */
   @GET
   @Path("/getImageMedConfidenceFields/{image}")
   public static String getImageMedConfidenceFields(@PathParam("image") String image) {
      return ImageFieldDAO.getMedConfidence(image) + "";
   }

   /**
    * Gets all fields whose confidence was below the low threshold for a given image.
    *
    * @param image to retrieve fields for.
    * @return all fields whose confidence was below the low threshold for this image.
    */
   @GET
   @Path("/getImageLowConfidenceFields/{image}")
   public static String getImageLowConfidenceFields(@PathParam("image") String image) {
      return ImageFieldDAO.getLowConfidence(image) + "";
   }

   /**
    * Gets the height of a given template.
    *
    * @param template identifies the template.
    * @return height of the specified template.
    */
   @GET
   @Path("/getTemplateHeight/{template}")
   public static String getTemplateHeight(@PathParam("template") String template) {
      return Integer.parseInt(TemplateDAO.getVal(template, "height")) + "";
   }

   /**
    * Gets the width of a given template.
    *
    * @param template identifies the template.
    * @return width of the specified template.
    */
   @GET
   @Path("getTemplateWidth/{template}")
   public static String getTemplateWidth(@PathParam("template") String template) {
      return Integer.parseInt(TemplateDAO.getVal(template, "width")) + "";
   }

   /**
    * Gets the distance between a field and the left margin of the template.
    *
    * @param template identifies the template this field is in.
    * @param field identifies the field to retrieve the X of.
    * @return the distance between the field and the left margin of the template.
    */
   @GET
   @Path("getTemplateFieldX/{template}/{field}")
   public static String getTemplateFieldX(@PathParam("template") String template, @PathParam("field") String field) {
      return Integer.parseInt(TemplateFieldDAO.getVal(template, field, "x")) + "";
   }

   /**
    * Gets the distance between a field and the top margin of the template.
    *
    * @param template identifies the template this field is in.
    * @param field identifies the field to retrieve the Y of.
    * @return the distance between the field and the top margin of the template.
    */
   @GET
   @Path("getTemplateFieldY/{template}/{field}")
   public static String getTemplateFieldY(@PathParam("template") String template, @PathParam("field") String field) {
      return Integer.parseInt(TemplateFieldDAO.getVal(template, field, "y")) + "";
   }

   /**
    * Gets the height of a field within a template.
    *
    * @param template identifies the template this field is in.
    * @param field identifies the field to retrieve the height of.
    * @return the height of the field in the given template.
    */
   @GET
   @Path("getTemplateFieldHeight/{template}/{field}")
   public static String getTemplateFieldHeight(@PathParam("template") String template,
      @PathParam("field") String field) {
      return Integer.parseInt(TemplateFieldDAO.getVal(template, field, "height")) + "";
   }

   /**
    * Gets the width of a field within a template.
    *
    * @param template identifies the template this field is in.
    * @param field identifies the field to retrieve the width of.
    * @return the width of the field in the given template.
    */
   @GET
   @Path("getTemplateFieldWidth/{template}/{field}")
   public static String getTemplateFieldWidth(@PathParam("template") String template,
      @PathParam("field") String field) {
      return Integer.parseInt(TemplateFieldDAO.getVal(template, field, "width")) + "";
   }

   /**
    * Gets the associated character set for a field within a template.
    *
    * @param template identifies the template this field is in.
    * @param field identifies the field to retrieve the character set for.
    * @return the character set associated with the specified field in the given template.
    */
   @GET
   @Path("getTemplateFieldCharSet/{template}/{field}")
   public static String getTemplateFieldCharSet(@PathParam("template") String template,
      @PathParam("field") String field) {
      return TemplateFieldDAO.getVal(template, field, "charSet");
   }

   /**
    * Gets the high threshold for a field within a template.
    *
    * @param template identifies the template this field is in.
    * @param field identifies the field to retrieve the high threshold of.
    * @return high threshold of the specified field within the given template.
    */
   @GET
   @Path("getTemplateFieldHighThresh/{template}/{field}")
   public static String getTemplateFieldHighThresh(@PathParam("template") String template,
      @PathParam("field") String field) {
      return Integer.parseInt(TemplateFieldDAO.getVal(template, field, "highThresh")) + "";
   }

   /**
    * Gets the low threshold for a field within a template.
    *
    * @param template identifies the template this field is in.
    * @param field identifies the field to retrieve the low threshold of.
    * @return low threshold of the specified field within the given template.
    */
   @GET
   @Path("getTemplateFieldLowThresh/{template}/{field}")
   public static String getTemplateFieldLowThresh(@PathParam("template") String template,
      @PathParam("field") String field) {
      return Integer.parseInt(TemplateFieldDAO.getVal(template, field, "lowThresh")) + "";
   }

   /**
    * Get a list of a all fields within this template.
    *
    * @param template identifies the template to retrieve fields from.
    * @return a list of all fields within the specified template.
    */
   @GET
   @Path("getTemplateFields/{template}")
   public static String getTemplateFields(@PathParam("template") String template) {
      return TemplateFieldDAO.getFields(template) + "";
   }

   /**
    * Updates the location of an image.
    *
    * @param image identifies the image to update.
    * @param location new filepath for this image.
    */
   @GET
   @Path("/addImage/{image}/{location: .*}")
   public static String setImageLocation(@PathParam("image") String image, @PathParam("location") String location) {
      ImageDAO.setVal(image, "location", location);
      return "Set location of: " + image + " to " + location;
   }

   /**
    * Updates the height of an image.
    *
    * @param image identifies the image to update.
    * @param height new height in pixels of this image.
    */
   @GET
   @Path("setImageHeight/{image}/{height}")
   public static String setImageHeight(@PathParam("image") String image, @PathParam("height") int height) {
      ImageDAO.setVal(image, "height", height + "");
      return "Set height of: " + image + " to " + height;
   }

   public static void setImageWidth(String image, int width) {
      ImageDAO.setVal(image, "width", width + "");
   }

   public static void setImageFieldX(String image, String field, int x) {
      ImageFieldDAO.setVal(image, field, "x", x + "");
   }

   public static void setImageFieldY(String image, String field, int y) {
      ImageFieldDAO.setVal(image, field, "y", y + "");
   }

   public static void setImageFieldHeight(String image, String field, int height) {
      ImageFieldDAO.setVal(image, field, "height", height + "");
   }

   public static void setImageFieldWidth(String image, String field, int width) {
      ImageFieldDAO.setVal(image, field, "width", width + "");
   }

   public static void setImageFieldCharSet(String image, String field, String charSet) {
      ImageFieldDAO.setVal(image, field, "charSet", charSet + "");
   }

   public static void setImageFieldHighThresh(String image, String field, int highThresh) {
      ImageFieldDAO.setVal(image, field, "highThresh", highThresh + "");
   }

   public static void setImageFieldLowThresh(String image, String field, int lowThresh) {
      ImageFieldDAO.setVal(image, field, "lowThresh", lowThresh + "");
   }

   public static void setImageFieldFinalVal(String image, String field, String finalVal) {
      ImageFieldDAO.setVal(image, field, "finalVal", finalVal + "");
   }

   public static void setTemplateHeight(String template, int height) {
      TemplateDAO.setVal(template, "height", height + "");
   }

   public static void setTemplateWidth(String template, int width) {
      TemplateDAO.setVal(template, "width", width + "");
   }

   public static void setTemplateFieldX(String template, String field, int x) {
      TemplateFieldDAO.setVal(template, field, "x", x + "");
   }

   public static void setTemplateFieldY(String template, String field, int y) {
      TemplateFieldDAO.setVal(template, field, "y", y + "");
   }

   public static void setTemplateFieldHeight(String template, String field, int height) {
      TemplateFieldDAO.setVal(template, field, "height", height + "");
   }

   public static void setTemplateFieldWidth(String template, String field, int width) {
      TemplateFieldDAO.setVal(template, field, "width", width + "");
   }

   public static void setTemplateFieldCharSet(String template, String field, String charSet) {
      TemplateFieldDAO.setVal(template, field, "charSet", charSet);
   }

   public static void setTemplateFieldHighThresh(String template, String field, int highThresh) {
      TemplateFieldDAO.setVal(template, field, "highThresh", highThresh + "");
   }

   public static void setTemplateFieldLowThresh(String template, String field, int lowThresh) {
      TemplateFieldDAO.setVal(template, field, "lowThresh", lowThresh + "");
   }

   /**
    * Remove an image from NanoWeather.
    *
    * @param image specifies the identifier of this image.
    */
   public static void removeImage(String image) {
      ImageDAO.remove(image);
   }

   /**
    * Remove a field from an image within NanoWeather.
    *
    * @param image identifies the image this field exists within.
    * @param field specifies an identifier for this field within this image.
    */
   public static void removeImageField(String image, String field) {
      java.nio.file.Path path = FileSystems.getDefault().getPath(image);
      try {
         Files.delete(path);
      } catch (NoSuchFileException x) {
         System.err.format("%s: no such" + " file or directory%n", path);
      } catch (DirectoryNotEmptyException x) {
         System.err.format("%s not empty%n", path);
      } catch (IOException x) {
         // File permission problems are caught here.
         System.err.println(x);
      }
      ImageFieldDAO.remove(image, field);
   }

   public static void removeTemplate(String template) {
      TemplateDAO.remove(template);
   }

   /**
    * Removes a field from a template so that it will no longer be added to images.
    *
    * @param template template of the template from which this field will be removed.
    * @param field name of the field which will be removed.
    */
   public static void removeTemplateField(String template, String field) {
      TemplateFieldDAO.remove(template, field);
   }

   /**
    * Adds all fields from a template to an image.
    *
    * @param image identifier of the image which will be updated .
    * @param template identifier of the template which will be applied to the image.
    */
   public static void applyTemplateToImage(String image, String template) {
      ImageFieldDAO.applyTemplate(template);
   }

   /**
    * Re-centers a template to account for drift between images of similar forms.
    *
    * @param image The image which is to be adjusted for.
    * @param x horizontal adjustment of template (+ left, - right).
    * @param y vertical adjustment of template (+ up, - down).
    */
   public static void recenterTemplateOnImage(String image, String template, int x, int y) {
      ImageFieldDAO.recenterTemplate(image, template, x, y);
   }

   /**
    * Realigns a field to match the location of the field on the image.
    *
    * @param image The image which is to be adjusted.
    * @param field The field which will be realigned.
    * @param x horizontal adjustment of template (+ left, - right).
    * @param y vertical adjustment of template (+ up, - down).
    */
   public static void recenterFieldOnImage(String image, String template, int x, int y) {
      ImageFieldDAO.recenterField(image, template, x, y);
   }
}
