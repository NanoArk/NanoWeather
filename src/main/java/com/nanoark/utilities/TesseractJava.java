package com.nanoark.utilities;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;

public class TesseractJava {
   public static String ocrAndPrint(String location) {
      BytePointer outText;
      TessBaseAPI api = new TessBaseAPI();
      // Initialize tesseract-ocr with English, without specifying tessdata path
      if(api.Init(".", "ENG") != 0) {
         System.err.println("Could not initialize tesseract.");
         System.exit(1);
      }
      // Open input image with leptonica library
      PIX image = pixRead(location);
      api.SetImage(image);
      // Get OCR result
      outText = api.GetUTF8Text();
      String result = outText.getString();
      // Destroy used object and release memory
      api.End();
      outText.deallocate();
      pixDestroy(image);
      return result;
   }

   public static int ocrAndConf(String location) {
      TessBaseAPI api = new TessBaseAPI();
      // Initialize tesseract-ocr with English, without specifying tessdata path
      if(api.Init(".", "ENG") != 0) {
         System.err.println("Could not initialize tesseract.");
         System.exit(1);
      }
      // Open input image with leptonica library
      PIX image = pixRead(location);
      api.SetImage(image);
      // Get OCR confidence result
      int result = api.MeanTextConf();
      // Destroy used object and release memory
      api.End();
      pixDestroy(image);
      return result;
   }
}