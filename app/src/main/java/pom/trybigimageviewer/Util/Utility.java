package pom.trybigimageviewer.Util;

/**
 * Created by carol.tam on 1/9/16.
 */

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;


public class Utility {
    public static String TEMP_IMGE_FOLDER = "temp_image";
    public static String SYSTEM_APP_NAME = "Kerry";


    public static void showAlert(Context context, int title, int msg, int pos, int neg,
                                 DialogInterface.OnClickListener posListener,
                                 DialogInterface.OnClickListener negListener, boolean isCancelable) {
        if (neg > 0)
            showAlert(context, title > 0 ? context.getResources().getString(title) : null, context.getResources().getString(msg), context
                            .getResources().getString(pos), context.getResources().getString(neg), posListener, negListener,
                    isCancelable);

        else
            showAlert(context, title > 0 ? context.getResources().getString(title) : null, context.getResources().getString(msg), context
                    .getResources().getString(pos), null, posListener, null, isCancelable);
    }

    public static void showAlert(Context context, String title, String message, String pos, String neg,
                                 DialogInterface.OnClickListener posListener,
                                 DialogInterface.OnClickListener negListener, boolean isCancelable) {

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);

        if (pos != null) {
            alt_bld.setPositiveButton(pos, posListener);
        }

        if (neg != null) {
            alt_bld.setNegativeButton(neg, negListener);
        }

        AlertDialog alertDialog = alt_bld.create();

        if (title != null) {
            alertDialog.setTitle(title);
        }

        if (message != null) {
            alertDialog.setMessage(message);
        }

        alertDialog.setCancelable(isCancelable);
        alertDialog.show();
    }

    public static final String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + "_" + model;
        }
    }

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        NetworkInfo.State netState = null;
        if (nInfo != null) {
            netState = nInfo.getState();
        }

        if (netState != null && netState == NetworkInfo.State.CONNECTED)
            return true;
        else
            return false;
    }

    public static Date convertStringToDate(String targetDate, String dateFormat) {
        SimpleDateFormat df1 = new SimpleDateFormat(dateFormat);
        try {
            Date parsedDate = df1.parse(targetDate);
            return parsedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertDateFormat1To2(String targetDate, String dateFormat1, String dateFormat2) {
        SimpleDateFormat df1 = new SimpleDateFormat(dateFormat1);
        try {
            Date parsedDate = df1.parse(targetDate);
            return convertDateToStringENG(parsedDate, dateFormat2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertDateToString(Date targetDate, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String dateString = sdf.format(targetDate);
        return dateString;
    }

    public static String convertDateToStringENG(Date targetDate, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        String dateString = sdf.format(targetDate);
        return dateString;
    }


    public static String convertMillisToDate(long milliSeconds, String dateFormat) {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String convertMillisToDateEN(long milliSeconds, String dateFormat) {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static long getTimeDifferent(String start, String end, String format) {
        long startTime = 0;
        long endTime = 0;

        SimpleDateFormat df = new SimpleDateFormat(format);

        try {
            Date parsedDate1 = df.parse(start);
            Date parsedDate2 = df.parse(end);

            startTime = parsedDate1.getTime();
            endTime = parsedDate2.getTime();
        } catch (ParseException e) {

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return endTime - startTime;
    }


//    public static String getUDID(Context context)
//    {
//        String udid=null;
//        try{
//
//            udid=Preferences.getUDID(context);
//            if(udid==null || udid.isEmpty())
//            {
//                udid  = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//                Preferences.setUDID(context,udid);
//            }
////
////            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
////            String m_szWLANMAC  = wm.getConnectionInfo().getMacAddress();
////            m_szWLANMAC = m_szWLANMAC.replaceAll(":", "");
////            Utility.Log("GCM", "m_szWLANMAC: " + m_szWLANMAC);
//
//            Utility.Log("GetUDID", "UDID: " + udid);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return udid;
//
//    }

    public static Bitmap getBitmapFromUriAndRotateWithExif(Context context, Uri imageUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);

        ExifInterface exif = null;

        File imageFile = new File(imageUri.getPath());
        String imagePath = imageFile.getAbsolutePath();
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);


        return rotateBitmap(bitmap, orientation);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Bitmap getBitmapFromUri(Context context, Uri imageUri) throws IOException {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static Bitmap resizeWeixinBmp(Bitmap bmp, int dWidth) {
        if (bmp == null)
            return null;

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float ratio = (float) height / width;
        int desiredWidth = dWidth;
        if (ratio > 2)
            desiredWidth = (int) (dWidth * 2 / ratio);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, desiredWidth, (int) (desiredWidth * ratio), false);
        return resizedBitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = (int) Math.ceil((float) height / (float) reqHeight);
            } else {
                inSampleSize = (int) Math.ceil((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }


    public static int calculateInSampleSizeForMaxWidthHeight(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            int inSampleSize1 = (int) Math.ceil((float) height / (float) reqHeight);

            int inSampleSize2 = (int) Math.ceil((float) width / (float) reqWidth);

            inSampleSize = inSampleSize1 > inSampleSize2 ? inSampleSize1 : inSampleSize2;

        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromUri(ContentResolver cr, Uri uri,
                                                    int reqWidth, int reqHeight) {


        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(cr.openInputStream(uri), null, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(cr.openInputStream(uri), null, options);
        } catch (Exception e) {
            return null;
        }
    }


    public static Bitmap decodeBitmapFromInputStream(InputStream originalIS,
                                                     int reqWidth, int reqHeight) {

        Bitmap result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = originalIS.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
        InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(is1, null, options);
            int width = options.outWidth;
            int height = options.outHeight;

            if (reqWidth < options.outWidth || reqHeight < options.outHeight) {
                options.inSampleSize = calculateInSampleSizeForMaxWidthHeight(options, reqWidth, reqHeight);

            }
            int size = options.inSampleSize;


            options.inJustDecodeBounds = false;

            Bitmap b = BitmapFactory.decodeStream(is2, null, options);


            if (size != 1) {


                if (width * reqHeight < height * reqWidth) {
                    result = Bitmap.createScaledBitmap(b, reqHeight * width / height, (int) (reqHeight), false);

                } else {
                    result = Bitmap.createScaledBitmap(b, reqWidth, (int) (height * reqWidth / width), false);
                }


                b.recycle();
            } else {
                result = b;
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        } finally {
            try {

                if (is1 != null) {
                    is1.close();
                    is1 = null;
                }

                if (is2 != null) {
                    is2.close();
                    is2 = null;
                }

                baos = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return result;

    }

    public static Intent getUrlIntentAction(Context context, String url) {


        Intent intent = null;

        if (url.startsWith("market:")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } else if (url.startsWith("sms:")) {
            String[] tempStringArray = url.split("\\:");
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (tempStringArray.length > 2) {
                intent.putExtra("address", tempStringArray[1]);
                intent.putExtra("sms_body", tempStringArray[2]);
            } else if (tempStringArray.length > 1) {
                intent.putExtra("address", tempStringArray[1]);
            }
            intent.setType("vnd.android-dir/mms-sms");

        } else if (url.startsWith("tel:")) {
            intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } else if (url.startsWith("mailto:")) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            int colonIndex = url.indexOf(":") + 1;
            String mailString = url.substring(colonIndex);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailString});

        } else if (url.toLowerCase().startsWith("video:")) {
            int colonIndex = url.indexOf(":") + 1;
            String linkString = url.substring(colonIndex);

            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse(linkString), "video/*");

        } else if (url.toLowerCase().startsWith("activity:")) {
            int colonIndex = url.indexOf(":") + 1;
            String linkString = url.substring(colonIndex);

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkString));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } else if (url.startsWith("https://play.google.com") || url.startsWith("http://play.google.com")) {
            String appId = getGooglePlayAppId(url);

            if (appId != null) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appId));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

        } else if (url.startsWith("fb:")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } else if (url.startsWith("vnd.youtube:")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } else if (url.contains("youtube.com")) {
            String videoId = getYoutubeVideoId(url);

            if (videoId != null) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                intent.putExtra("VIDEO_ID", videoId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }

        if (intent != null && !isIntentCanHandle(context, intent)) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        return intent;

    }

    public static String getYoutubeVideoId(String youtubeUrl) {
        // android.util.Log.d("getYoutubeVideoId", "youtubeUrl: " + youtubeUrl);
        String video_id = null;
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http")) {

            String expression = "^.*((youtu.be" + "\\/)"
                    + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {

                // for (int i = 0; i <= matcher.groupCount(); i++) {
                // android.util.Log.d("getYoutubeVideoId", i + " : " +
                // matcher.group(i));
                // }

                String groupIndex1 = matcher.group(matcher.groupCount());
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

    public static String getGooglePlayAppId(String googlePlayUrl) {
        String appId = null;
        if (googlePlayUrl != null && googlePlayUrl.trim().length() > 0 && googlePlayUrl.startsWith("http")) {

            // int index = googlePlayUrl.indexOf("details?id=") + 11;
            // appId = googlePlayUrl.substring(index);

            String expression = "^.*((play.google.com" + "\\/)" + "|(details\\?))\\??id?=?([^#\\&\\?]*).*";

            CharSequence input = googlePlayUrl;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {

                // for (int i = 0; i <= matcher.groupCount(); i++) {
                // android.util.Log.d("getGooglePlayAppId", i + " : " +
                // matcher.group(i));
                // }

                appId = matcher.group(matcher.groupCount());
            }
        }
        return appId;
    }

    static boolean isIntentCanHandle(Context context, Intent intent) {
        boolean canHandle = false;

        try {
            PackageManager manager = context.getPackageManager();
            List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
            if (infos.size() > 0) {
                canHandle = true;
            } else {
                canHandle = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return canHandle;
    }


    public static float convertDipToPx(Context context, float dipValue) {
        float px = 0;

        if (context != null) {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources()
                    .getDisplayMetrics());
        }

        return px;
    }


    public static boolean isValidMobile(String mobile) {
        String regexStr = "^\\d{8}$";
        if (mobile.matches(regexStr)) {
            return true;
        }
        return false;
    }


    static public boolean isValidTicketPeople(String numOfPeople, String maxPeople) {
        //String regexStr = "^[0-9]$";
        try {
            int i = Integer.parseInt(numOfPeople);
            int j = Integer.parseInt(maxPeople);
            if (i > 0 && i <= j) {
                return true;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return false;
    }


    public static boolean saveImageToInternalCache(Context context, String imageName, Bitmap bitmap) {
        boolean success = false;
        FileOutputStream fos = null;

        File mydir = context.getDir(TEMP_IMGE_FOLDER, MODE_PRIVATE); //Creating an internal dir;
        if (!mydir.exists())
            mydir.mkdirs();

        File fileWithinMyDir = new File(mydir, imageName); //Getting a file within the dir.


        try {
            if (bitmap != null) {
                fos = new FileOutputStream(fileWithinMyDir);
                bitmap.compress(CompressFormat.PNG, 100, fos);

                fos.flush();
                fos.close();

                success = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }


    public static boolean saveImageToFile(Context context, File file, Bitmap bitmap) {
        boolean success = false;
        FileOutputStream fos = null;

        file.mkdirs();

        if (file.exists())
            file.delete();


        try {
            if (bitmap != null) {
                fos = new FileOutputStream(file);
                bitmap.compress(CompressFormat.PNG, 100, fos);

                fos.flush();
                fos.close();

                success = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }

    public static void deleteAllInternalTempImages(Context context) throws IOException {

//        Context context = AppsApplication.getAppContext();

        File imageFile;

        File internalFileDir = context.getDir(TEMP_IMGE_FOLDER, MODE_PRIVATE);

        for (String intFileName : internalFileDir.list()) {
            imageFile = new File(internalFileDir, intFileName);

            if (imageFile.exists() && imageFile.toString().contains(".png")) {
                imageFile.delete();
            }
        }

    }


    public static Bitmap getImageFromCache(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fis = null;

        try {
            fis = context.getApplicationContext().openFileInput(imageName);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    public static Point getTheScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;

    }

    public static void setAViewSize(View v, int width, int height) {


        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
        v.setLayoutParams(parms);

    }

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public static void clearAllTheBackStack(FragmentActivity fActivity) {
        FragmentManager fm = fActivity.getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    public static void setSpIsComeFromLoginFragment(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(StaticString.SP_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(StaticString.SP_IS_FROM_PROPERTIES_LOFIN_FRAGMENT, true);
        editor.commit();
    }

    static public boolean getSpIsComeFromLoginFragment(Context context) {
        SharedPreferences sp = context.getSharedPreferences(StaticString.SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        boolean answer = sp.getBoolean(StaticString.SP_IS_FROM_PROPERTIES_LOFIN_FRAGMENT, false);
        editor.putBoolean(StaticString.SP_IS_FROM_PROPERTIES_LOFIN_FRAGMENT, false);
        editor.commit();

        return answer;

    }

    static public String getVersionName(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        return version;

    }


    public static Bitmap resizeBitmapCentralInside(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax < 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, false);
            return image;
        } else {
            return image;
        }
    }


    public static String getDeviceID(Context context) {
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getDeviceId();
    }

    public static boolean validateEmailAddressPattern(String email) {

        Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%\\-+]{1,256}" + "@"
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static void showTowButtonDialog(Context context, String title, String message, String leftButton, String rightButton,
                                           DialogInterface.OnClickListener rightOnClickListener,
                                           DialogInterface.OnClickListener leftOnClickListener) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title);
// Add the buttons
        builder.setPositiveButton(rightButton, rightOnClickListener);
        builder.setNegativeButton(leftButton, leftOnClickListener);

        builder.setCancelable(false);
// Create the AlertDialog
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void showOneButtonDialog(Context context, String title, String message, String rightButton,
                                           DialogInterface.OnClickListener rightOnClickListener) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title);
// Add the buttons
        builder.setPositiveButton(rightButton, rightOnClickListener);

        builder.setCancelable(false);
// Create the AlertDialog
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static String getYoutubeID(String url) {/*if it is not a youtube link,return null*/
        Uri youtube = Uri.parse(url);
        String youtubeID = youtube.getQueryParameter("v");
        if (youtubeID != null)
            return youtubeID;
        else {
            /*https://youtu.be/vHqr0JqGTLs*/
            Uri youtubeUri = Uri.parse(url);
            String authority = youtubeUri.getAuthority();
            List<String> segments = youtubeUri.getPathSegments();
            if (authority.equals("youtu.be") && segments.size() > 0) {
                youtubeID = segments.get(0);
            }
            return youtubeID;
        }

    }


}
