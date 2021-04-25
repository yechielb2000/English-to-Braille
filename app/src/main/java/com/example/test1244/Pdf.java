package com.example.test1244;

import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;

public class Pdf extends PdfDocument{

     static double file_size;

    public static boolean createMyPDF(String name){

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();

        String pdfText = setupTextLines();
        int x = 10, y=25;

        for (String line : pdfText.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y += myPaint.descent() - myPaint.ascent();
        }

        myPdfDocument.finishPage(myPage);

        File path = Environment.getExternalStoragePublicDirectory("English to Braille");
        File file = new File(Environment.getExternalStoragePublicDirectory("English to Braille")+"/"+ name +".pdf");

       if(file.exists()){
           Toast.makeText( MainActivity.main_layout.getContext(), "File name already exists", Toast.LENGTH_LONG).show();
           return false;
        }else {
           File myFile = new File(path,name + ".pdf");
           try {
               path.mkdirs();
               myPdfDocument.writeTo(new FileOutputStream(myFile));
               file_size = myFile.length()/1000.0;
           } catch (Exception e) {
               e.printStackTrace();
               Toast.makeText(MainActivity.main_layout.getContext(), "ERROR : " + e.getMessage(), Toast.LENGTH_LONG).show();
               Log.e("TAG", "Exception", e);
           }
           myPdfDocument.close();
           return true;
       }
    }

    private static String setupTextLines() {

        String newLine = "\n";
        StringBuilder newString = new StringBuilder(newLine + "The translate of :" + newLine);
        String text = MainActivity.text + newLine + newLine +"is :" + newLine + MainActivity.finalText;

        int WidthPage = 35, textLength = text.length();

        while(textLength > 0) {
            if (textLength <= WidthPage) {
                newString.append(newLine).append(text.substring(text.length() - textLength));
                break;
            } else {
                int startOfLineIndex = 0;
                for (int endOfLineIndex = WidthPage; endOfLineIndex < text.length(); endOfLineIndex += WidthPage) {
                    if (text.charAt(endOfLineIndex) != ' ') {
                        for (int k = endOfLineIndex - 1; k > endOfLineIndex - WidthPage; k--) {
                            if (text.charAt(k) == ' ') {
                                int distanceToSpace = endOfLineIndex - k;
                                endOfLineIndex -= distanceToSpace;
                                break;
                            }
                        }
                    }
                    newString.append(newLine).append(text.substring(startOfLineIndex, endOfLineIndex));
                    startOfLineIndex = endOfLineIndex + 1;
                    textLength = text.length() - startOfLineIndex;
                }
            }
        }
        return newString.toString();
    }
}
