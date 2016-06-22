package com.nanoark.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Class with specific file actions which are used within NanoWeather.
 *
 * @author Vino Sugunan
 */
public class FileActions {
   private static Logger log = Log.logger();

   /**
    * Copies out a sub image from a file.
    *
    * @param from location of original file.
    * @param to location of copied sub image.
    * @param x distance from left edge of file.
    * @param y distance from top edge of file.
    * @param width horizontal size of sub image in pixels.
    * @param height vertical size of sub image in pixels.
    * @since 2016/05/18 Vino Sugunan - Extacted code by Alex in NanoWeather for generic use.
    */
   public static void getSubImage(String from, String to, int x, int y, int width, int height) {
      Boolean fail = false;
      try {
         BufferedImage fromBuf = ImageIO.read(new File(from));
         int maxWidth = fromBuf.getWidth();
         int maxHeight = fromBuf.getHeight();
         if(x > maxWidth) {
            x = maxWidth - 1;
            fail = true;
         }
         if(y > maxHeight) {
            y = maxWidth - 1;
            fail = true;
         }
         if((x + width) > maxWidth) {
            width = maxWidth - x;
            fail = true;
         }
         if((y + height) > maxHeight) {
            height = maxHeight - y;
            fail = true;
         }
         if(fail) {
            String message = "Field dimensions exceed boundary, adjusted to:";
            message += "\n\tFrom:   " + from;
            message += "\n\tTo:     " + to;
            message += "\n\tX:      " + x;
            message += "\n\tY:      " + y;
            message += "\n\tWidth:  " + width;
            message += "\n\tHeight: " + height;
            log.warning(message);
         }
         BufferedImage out = fromBuf.getSubimage(x, y, width, height);
         ImageIO.write(out, "png", new File(to));
      } catch (IOException e) {
         log.severe(Log.getError(e));
      }
   }

   public String getFileExtension(String filepath) {
      String ext = "";
      int i = filepath.lastIndexOf('.');
      if(i > 0) {
         ext = filepath.substring(i + 1);
      }
      return ext;
   }
}
