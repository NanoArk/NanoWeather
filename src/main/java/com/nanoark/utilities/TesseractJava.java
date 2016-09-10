package com.nanoark.utilities;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;

import java.util.logging.Logger;
import com.nanoark.utilities.Log;

public class TesseractJava {
	
   private static Logger       log = Log.logger();
	
   public static String ocrAndPrint(String location) {
      BytePointer outText;
      TessBaseAPI api = new TessBaseAPI();
      String error = "--OCR ERROR: COULD NOT INITIALIZE--";
      // Initialize tesseract-ocr with English, without specifying tessdata path
      if(api.Init(".", "ENG") != 0) {
    	 log.severe("Could not initialize tesseract at location: "+System.getProperty("user.dir"));
    	 api.End();
    	 api.close();
    	 return error;
         //System.err.println("Could not initialize tesseract.");
         //System.exit(1);
      }
      // Open input image with leptonica library
      PIX image = pixRead(location);
      api.SetImage(image);
      // Get OCR result
      outText = api.GetUTF8Text();
      String result = outText.getString();
      // Destroy used object and release memory
      api.End();
      api.close();
      outText.deallocate();
      pixDestroy(image);
      image.close();
      outText.close();
      return result;
   }

   public static int ocrAndConf(String location) {
      TessBaseAPI api = new TessBaseAPI();
      // Initialize tesseract-ocr with English, without specifying tessdata path
      int error=-1;
      if(api.Init(".", "ENG") != 0) {
    	 log.severe("Could not initialize tesseract.");
    	 api.End();
    	 api.close();
    	 return error;
         //System.err.println("Could not initialize tesseract.");
         //System.exit(1);
      }
      // Open input image with leptonica library
      PIX image = pixRead(location);
      api.SetImage(image);
      // Get OCR confidence result
      int result = api.MeanTextConf();
      // Destroy used object and release memory
      api.End();
      api.close();
      pixDestroy(image);
      image.close();
      return result;
   }
}