package com.ronx.smart2smart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.ByteMatrix;

import java.io.UnsupportedEncodingException;

import static android.graphics.Bitmap.Config.RGB_565;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class StartActivity extends Activity {
    Writer writer = new QRCodeWriter();
    Reader reader = new QRCodeReader();

    private String qrTestString = "TEST TEST TEST TEST TEST TEST TEST TEST TEST";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

            }
        }
    }

    private int width = 200;
    private int height = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button transferButton = (Button) findViewById(R.id.transferButton);
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitMatrix matrix;

                try {
                    String data = new String(qrTestString.getBytes(), "UTF8");
                    matrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);
                } catch (WriterException | UnsupportedEncodingException e) {
                    return;
                }

                int width = matrix.getWidth();
                int height = matrix.getHeight();
                int[] pixels = new int[width * height];
                // All are 0, or black, by default
                for (int y = 0; y < height; y++) {
                    int offset = y * width;
                    for (int x = 0; x < width; x++) {
                        pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
                    }
                }
                Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, RGB_565);
                qrCodeBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

/*                Bitmap qrCodeBitmap;
                qrCodeBitmap = Bitmap.createBitmap(width, height, RGB_565);
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        qrCodeBitmap.setPixel(i, j, matrix.get(i, j) ? BLACK: WHITE);
                    }
                }*/

                Intent qrCodeIntent = new Intent(StartActivity.this, QRCodeShowActivity.class);
                qrCodeIntent.putExtra("qrCodeBitmap", qrCodeBitmap);
                StartActivity.this.startActivity(qrCodeIntent);
            }
        });

        Button receiptButton = (Button) findViewById(R.id.receiptButton);
        receiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);

/*                ByteMatrix matrix;
                Bitmap imageFileBitmap = BitmapFactory.decodeFile("");
                int[] intArray = new int[imageFileBitmap.getWidth() * imageFileBitmap.getHeight()];
                imageFileBitmap.getPixels(intArray, 0, imageFileBitmap.getWidth(), 0, 0, imageFileBitmap.getWidth(), imageFileBitmap.getHeight());

                LuminanceSource source = new RGBLuminanceSource(imageFileBitmap.getWidth(), imageFileBitmap.getHeight(), intArray);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                Result result;
                try {
                    result = reader.decode(bitmap);
                } catch (ChecksumException | NotFoundException | FormatException e) {
                    return;
                }

                System.out.println(result.getText());*/
            }
        });

    }
}
