package com.example.test1244;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

@SuppressLint("StaticFieldLeak")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private final String RELOAD_TRANSLATE = "reload_translate", RELOAD_EDIT_TEXT = "reload_edit_text";

    public SoundPool soundPool;
    public int id;

    private String text, finalText;
    private ScrollView scrollView;
    private LinearLayout buttons_layout, name_layout, enter_text_layout, last_ward_layout, main_layout;
    private TextView textView, tellWord, lastWord;
    private EditText editText, pdfName;
    private Button name_it;

    private final int CAMERA_REQUEST = 1888;
    private final int MY_CAMERA_PERMISSION_CODE = 100;

    private final Pdf pdf = new Pdf();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
         StrictMode.setVmPolicy(builder.build());

         name_it = findViewById(R.id.named_it);
         buttons_layout = findViewById(R.id.buttons_layout);
         name_layout = findViewById(R.id.name_layout);
         last_ward_layout = findViewById(R.id.last_word_layout);
         enter_text_layout = findViewById(R.id.enter_text);
         main_layout = findViewById(R.id.main_layout);
         scrollView = findViewById(R.id.scrollView);
         textView = findViewById(R.id.output);
         textView.setTextSize(textView.getTextSize());
         editText = findViewById(R.id.get_text);
         lastWord = findViewById(R.id.last_word);
         tellWord = findViewById(R.id.tell_word);
         pdfName = findViewById(R.id.pdfName);

        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        id = soundPool.load(this, R.raw.click, 1);
        String LAST_TRANSLATION = "com.example.test1244.LAST_TRANSLATION";
        sharedPreferences = getSharedPreferences(LAST_TRANSLATION, Context.MODE_PRIVATE);
        finalText = sharedPreferences.getString(RELOAD_TRANSLATE, "");
        text = sharedPreferences.getString(RELOAD_EDIT_TEXT, "");

        textView.setText(finalText);
        CreateLastWord();
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RELOAD_TRANSLATE, finalText);
        editor.putString(RELOAD_EDIT_TEXT, text);
        editor.apply();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_get_text:
                soundPool.play(id, 1, 1, 0, 0, 1);
                if (editText.getText().toString().length() > 250){
                    Toast.makeText(MainActivity.this, "too long", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }else {
                    text = editText.getText().toString().toLowerCase() + " ";
                    finalText = new Translator(this, text).translate();
                    editText.setText("");
                    textView.setText(finalText);
                    CreateLastWord();
                }
                break;

            case R.id.clear_text:
                soundPool.play(id, 1, 1, 0, 0, 1);
                textView.setText("");
                editText.setText("");
                finalText = "";
                lastWord.setVisibility(View.GONE);
                tellWord.setVisibility(View.GONE);
                break;

            case R.id.make_pdf:
                soundPool.play(id, 1, 1, 0, 0, 1);
                if(finalText.length() > 1) {

                    name_layout.setVisibility(View.VISIBLE);
                    buttons_layout.setVisibility(View.GONE);
                    enter_text_layout.setVisibility(View.GONE);
                    last_ward_layout.setVisibility(View.GONE);
                    scrollView.setVisibility(View.GONE);

                    nameItButton();

                }else Toast.makeText(MainActivity.this, "No words to copy", Toast.LENGTH_SHORT).show();
                break;

            case R.id.all_pdf:
                startPdfActivity(false);
                break;

            case R.id.camera_button:
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }else{
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                break;
        }
    }

    private void nameItButton() {

        name_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(id, 1, 1, 0, 0, 1);

                if (pdfName.length() <= 0) {
                    Toast.makeText(MainActivity.this, "You must name the file, Please try again", Toast.LENGTH_LONG).show();
                } else {
                        name_layout.setVisibility(View.GONE);
                        buttons_layout.setVisibility(View.VISIBLE);
                        enter_text_layout.setVisibility(View.VISIBLE);
                        last_ward_layout.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.VISIBLE);

                        if (pdf.createMyPDF(pdfName.getText().toString().replace("/", ""), text, finalText, getApplicationContext()));
                            startPdfActivity(true);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            getTextFromImage((Bitmap) data.getExtras().get("data"));
        }
    }

    public void getTextFromImage(Bitmap photo) {

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()){
            Toast.makeText(getApplicationContext(), "Could not get the Text", Toast.LENGTH_SHORT).show();
        }else{
            Frame frame = new Frame.Builder().setBitmap(photo).build ();
            SparseArray<TextBlock> items = textRecognizer.detect (frame) ;

            StringBuilder stringBuilder = new StringBuilder ();
            for (int i=0; i < items.size (); ++i) {
                TextBlock myItem = items.valueAt(i);
                stringBuilder.append(myItem.getValue());
                stringBuilder.append("\n");
            }
            editText.setText(stringBuilder.toString());
        }
    }

    private void startPdfActivity(boolean addNew){

        Intent i = new Intent(MainActivity.this, PdfList.class);
        if (addNew) i.putExtra("FILE_NAME", pdfName.getText().toString());
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void CreateLastWord(){
        if(text.length() > 1) {
            lastWord.setVisibility(View.VISIBLE);
            tellWord.setVisibility(View.VISIBLE);
            tellWord.setText(text);
        }
    }

}