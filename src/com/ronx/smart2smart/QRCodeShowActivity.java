package com.ronx.smart2smart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class QRCodeShowActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);

        Intent intent = getIntent();
        Bitmap qrCodeBitmap = intent.getParcelableExtra("qrCodeBitmap");

        ImageView qrCodeImage = (ImageView) findViewById(R.id.qrcodeImageView);

        qrCodeImage.setImageBitmap(qrCodeBitmap);
    }
}
