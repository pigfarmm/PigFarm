package com.example.admin.pigfarm.BodyAnalyze;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.pigfarm.LoginActivity;
import com.example.admin.pigfarm.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PictureActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button re_button,submit_button;
    private TextView path,responseText;
    private Executor executor = Executors.newSingleThreadExecutor();
    private ProgressDialog progressbar;


    String postUrl= "http://10.199.2.12:8080/bcs";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        String getpath = getIntent().getExtras().getString("path");

        imageView = findViewById(R.id.img);
        re_button = findViewById(R.id.re_button);
        submit_button = findViewById(R.id.submit_button);
//        path = findViewById(R.id.path);
//        responseText = findViewById(R.id.responseText);

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(Camera2BasicFragment.capturedImage, 1600, 1200, true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

       //Bitmap rotatedBitmap = Bitmap.createBitmap(Camera2BasicFragment.capturedImage, 0, 0, Camera2BasicFragment.capturedImage.getWidth(), Camera2BasicFragment.capturedImage.getHeight(), matrix, true);

//        Bitmap b = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);


        imageView.setImageBitmap(rotatedBitmap);

        re_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressbar = new ProgressDialog(PictureActivity.this);
                progressbar.setMessage("กำลังประมวลผลรูปภาพ...");
                progressbar.setCancelable(false);
                progressbar.setIndeterminate(true);

                progressbar.show();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;

                Bitmap bitmap = BitmapFactory.decodeFile(getpath, options);
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                byte[] byteArray = stream.toByteArray();

                RequestBody postBodyImage = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", "001.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                        .build();

                postRequest(postUrl, postBodyImage);


            }

        });


    }


    private void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                progressbar.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PictureActivity.this, "ไม่สามารถเชื่อมต่อ Server ได้ โปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressbar.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Intent intent = new Intent(PictureActivity.this,ResultActivity.class);
                            intent.putExtra("score1",response.body().string());
                            startActivity(intent);
                            finish();

//                            responseText.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
