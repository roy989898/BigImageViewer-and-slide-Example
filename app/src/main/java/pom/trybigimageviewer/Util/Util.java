package pom.trybigimageviewer.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Roy.Leung on 16/1/17.
 */

public class Util {
    public static class GetCachedImageTask extends GetImageTask {

        protected WeakReference<Context> mContext;
        protected String mImageName;
        public int position;

        public RemoteViews remoteViews;


        public GetCachedImageTask(Context context, String url) {
            super(url);

            mContext = new WeakReference<Context>(context);
            mImageName = getImageNameFromURL(url);
            mURL = url;
            position = -1;
        }

        protected Context getContext() {
            if (mContext != null) {
                return mContext.get();
            } else {
                return null;
            }
        }

        public void setPosition(int aPosition) {
            position = aPosition;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            try {

                if (getContext() != null && Cache.isImageCached(getContext(), mImageName)) {
                    bitmap = Cache.getImage(getContext(), mImageName);


                }

                if (bitmap == null) {

                    bitmap = super.doInBackground(params);
                    if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                        Cache.saveImage(getContext(), mImageName, bitmap);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();

                bitmap = null;
            }

            return bitmap;
        }

        protected String getImageNameFromURL(String url) {
            if (url == null) {
                return null;
            }

            return url.replace("http://", "").replaceAll("/", "_");
        }
    }

    public static class GetImageTask extends BaseAsyncTask<Void, Void, Bitmap> {

        public ImageView mImageView;

        public boolean isOffline;

        public int mPosition;

        public String mImageName;

        public boolean isLargeImage;

        public GetImageTask(String thumbnail) {
            setURL(thumbnail);
        }

        public void setImageView(ImageView imageView) {
            mImageView = imageView;
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        public int getPosition() {
            return mPosition;
        }

        public void setImageName(String imageName) {
            mImageName = imageName;
        }

        public String getImageName() {
            return mImageName;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            if (mURL == null || mURL.isEmpty())
                return null;

            try {


                InputStream is = openURLConnection(mURL, null).getInputStream();

                if (!isLargeImage)
                    bitmap = BitmapFactory.decodeStream(is);
                else {
                    int[] maxSize = new int[1];

                    GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSize, 0);

                    bitmap = Utility.decodeBitmapFromInputStream(is, maxSize[0], maxSize[1]);
                }

                if (is != null)
                    is.close();


            } catch (Exception e) {
                e.printStackTrace();

                return null;

            }

            return bitmap;
        }
    }

    public abstract static class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
        static final String TAG = "BaseAsyncTask";

        protected ProgressBar mProgressBar;
        protected ProgressDialog mProgressDialog;

        public Exception exception;

//    protected HttpEntity mHttpEntity;

        protected String mURL;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            if (mProgressDialog != null) {
//            mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.GONE);
            }

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }

        public void setProgressBar(ProgressBar pb) {
            mProgressBar = pb;
        }

        public void setProgressDialog(ProgressDialog dialog) {
            mProgressDialog = dialog;
        }

        public void setURL(String url) {
            mURL = url;
        }

    }

    protected static URLConnection openURLConnection(String urlStr, List<Pair<String, String>> params) throws Exception {
        URL url = new URL(urlStr);

        URLConnection conn = null;
        try {
            if (url.getProtocol().toLowerCase().equals("https")) {
                // disableCertificateValidation();
                try {
                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    trustStore.load(null, null);
                    SSLContext sc = SSLContext.getInstance("TLS");
                    // sc.init(null, MySSLSocketFactory.trustAllCerts, new
                    // SecureRandom());
                    sc.init(null, new TrustManager[]{new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }}, new SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                } catch (Exception e) {
                    return null;
                }
                HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
                httpsConn.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {

//                        boolean result = Configure.isHostTrusted(hostname);

                        return true;
                    }
                });
                conn = httpsConn;
            } else {
                conn = url.openConnection();
            }

            conn.setRequestProperty("Connection", "Close");
            if (params != null && params.size() > 0) {
                conn.setDoInput(true);
                conn.setDoOutput(true);

                try {
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(params));
                    writer.flush();
                    writer.close();
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(50000);

        } finally {

        }
        return conn;
    }

    private static String getPostDataString(List<Pair<String, String>> params) throws Exception {

        Uri.Builder builder = new Uri.Builder();

        for (Pair<String, String> p : params)
            builder.appendQueryParameter(p.first, p.second);

        String query = builder.build().getEncodedQuery();
        return query;

    }
}
