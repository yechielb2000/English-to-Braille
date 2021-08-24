package com.example.test1244;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
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

import static com.example.test1244.Pdf.filePath;
import static com.example.test1244.Pdf.folder_name;

public class PdfList extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static LinearLayout pdf_list_layout;

    @Override  @SuppressLint("UseCompatLoadingForDrawables")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);

        pdf_list_layout = findViewById(R.id.pdf_list_layout);
        requestFromFolder();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void addViewToLayout(String pdfName){

        TextView textView = new TextView(pdf_list_layout.getContext());
        String type = getFileSize();

        String text = pdfName + "\n" + Pdf.file_size  + type;

        SpannableString ss1 = new SpannableString(text);
        ss1.setSpan(new RelativeSizeSpan(3f), 0,pdfName.length(), 0);
        textView.setTextSize(10);
        textView.setText(ss1);
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
//                Environment.getExternalStoragePublicDirectory(folder_name)
                File file = new File(filePath(getApplicationContext())+"/"+((TextView)v).getText().toString().split("\n")[0]);

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
//                Environment.getExternalStoragePublicDirectory(folder_name)
                File file = new File(filePath(getApplicationContext())+"/"+((TextView)v).getText().toString().split("\n")[0]);

                file.delete();
                Toast.makeText(MainActivity.main_layout.getContext(), file.getName() + "has been deleted.", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return true;
            }
        });

        pdf_list_layout.addView(textView);
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

        File folder_path = Environment.getExternalStoragePublicDirectory("English to Braille");
        File[] files = folder_path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    addViewToLayout(file.getName());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pdf_list_layout.removeAllViews();
        requestFromFolder();
    }
}