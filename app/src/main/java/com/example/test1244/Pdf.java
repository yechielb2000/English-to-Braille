package com.example.test1244;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;

public class Pdf extends PdfDocument{

    public final String folder_name = "English to Braille";
    static double file_size;

    public boolean createMyPDF(String name, String text, String finalText, Context context){

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();

        String pdfText = setupTextLines(text, finalText);
        int x = 10, y=25;

        for (String line : pdfText.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y += myPaint.descent() - myPaint.ascent();
        }

        myPdfDocument.finishPage(myPage);

        File path = filePath();
        File file = new File(path +"/"+ name +".pdf");

       if(file.exists()){
           Toast.makeText( context, "File name already exists", Toast.LENGTH_LONG).show();
           return false;
        }else {
           File myFile = new File(path,name + ".pdf");
           try {
               path.mkdirs();
               myPdfDocument.writeTo(new FileOutputStream(myFile));
               file_size = myFile.length()/1000.0;
           } catch (Exception e) {
               e.printStackTrace();
               Toast.makeText(context, "ERROR : " + e.getMessage(), Toast.LENGTH_LONG).show();
               Log.e("TAG", "Exception", e);
           }
           myPdfDocument.close();
           return true;
       }
    }

    public File filePath(){
        File path;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + folder_name);
        }
        else {
            path = new File(Environment.getExternalStorageDirectory() + "/" + folder_name);

            if (!path.exists()) {
                boolean success = path.mkdirs();
                if (!success) {
                    path = null;
                }
            }
        }
        return path;
    }

    private String setupTextLines(String text, String finalText) {

        String newLine = "\n";
        StringBuilder newString = new StringBuilder(newLine + "The translate of :" + newLine);
        text = text + newLine + newLine +"is :" + newLine + finalText;

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
