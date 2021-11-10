package com.example.test1244;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

public class PdfList extends AppCompatActivity {

    private LinearLayout pdfListLayout;
    private final Pdf pdf = new Pdf();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);

        pdfListLayout = findViewById(R.id.pdf_list_layout);
        requestFromFolder();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addViewToLayout(String pdfName, final Context context){

        TextView textView = new TextView(pdfListLayout.getContext());

        SpannableString spannableString = new SpannableString(pdfName + "\n" + Pdf.file_size  + getFileSize());
        spannableString.setSpan(new RelativeSizeSpan(3f), 0,pdfName.length(), 0);
        textView.setTextSize(10);
        textView.setText(spannableString);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        textView.setLayoutParams(lp);
        textView.setPadding(10, 10, 10, 10);
        textView.setGravity(3);
        textView.setBackground(getDrawable(R.drawable.border));
        textView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_sharp_picture_as_pdf_24), null, null, null);
        textView.setTextColor(Color.BLACK);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File file = new File(pdf.filePath()+"/"+((TextView)v).getText().toString().split("\n")[0]);

                Uri uri = Uri.fromFile(file);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                PdfList.this.startActivity(intent);
            }
        });

        textView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                File file = new File(pdf.filePath()+"/"+((TextView)v).getText().toString().split("\n")[0]);

                file.delete();
                Toast.makeText(context, file.getName() + "has been deleted.", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return true;
            }
        });

        pdfListLayout.addView(textView);
    }

    private String getFileSize() {

        String type;
        if (Pdf.file_size < 1000)
            type = "KB";
        else if(Pdf.file_size < 1000000)
            type = "MB";
        else
            type = "GB";

        return type;
    }

    private void requestFromFolder(){
        File folder_path;
        try {
             folder_path = Environment.getExternalStoragePublicDirectory("English to Braille");
        }catch (Exception ignore){
            folder_path = getApplicationContext().getExternalFilesDir("English to Braille");
        }

        File[] files = folder_path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    addViewToLayout(file.getName(), getApplicationContext());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pdfListLayout.removeAllViews();
        requestFromFolder();
    }
}