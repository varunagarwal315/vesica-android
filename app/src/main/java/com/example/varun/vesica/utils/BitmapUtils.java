package com.example.varun.vesica.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Created by varun on 20/9/16.
 */
public class BitmapUtils {

    private BitmapUtils(){

    }

    public static Bitmap ScaleImage( String value, float dimension){
        //TODO: If the scaling fails due to OOM error, re-calibrate the sample size for scaling
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmapBounds = BitmapFactory.decodeFile(value, options);

        options.inSampleSize = calculateInSampleSize(options, dimension);
        options.inJustDecodeBounds = false;

        Bitmap bitmapSampleSize = BitmapFactory.decodeFile(value, options);

        int width = bitmapSampleSize.getWidth();
        int height = bitmapSampleSize.getHeight();
        float scale;
        if (width>height){scale=dimension/width;}
        else {scale=dimension/height;}

        ExifInterface exif;
        Matrix matrix = new Matrix();
        try {
            exif = new ExifInterface(value);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);}

        }catch (IOException e){e.printStackTrace();}
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmapSampleSize, 0, 0,width, height, matrix, true);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, float dimension) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width>height){
            final int halfHeight = height / 2;
            while (halfHeight/inSampleSize > dimension){
                inSampleSize *= 2;
            }
        }
        else {
            final int halfWidth = width / 2;
            while (halfWidth/inSampleSize > dimension){
                inSampleSize *= 2;
            }
        }
        Log.d("Sample size returned ", String.valueOf(inSampleSize));
        Log.d("original dimensions ", String.valueOf(width)+" and "+String.valueOf(height));
        return inSampleSize;
    }

//    public static String convertImageToString(Bitmap bitmap){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//        byte[] byteArrayImage = baos.toByteArray();
//        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
//    }
//
//    public static Bitmap convertStringToBitmap(String data) throws UnsupportedEncodingException {
//        byte[] bytes = Base64.decode(data, Base64.DEFAULT);
//        String text = new String(bytes, "UTF-8");
//    }

}
