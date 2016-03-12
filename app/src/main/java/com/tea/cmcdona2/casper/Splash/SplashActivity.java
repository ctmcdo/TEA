package com.tea.cmcdona2.casper.Splash;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.tea.cmcdona2.casper.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Paddy on 18/11/2015.
 */
public class SplashActivity extends Activity {
    private Thread downloadThread[] = new Thread[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        final String eventsUrl = "http://clontarfguitarlessons.com/getEvents.php";
        final String infoUrl = "http://clontarfguitarlessons.com/getInfo.php";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //new DownloadWebpageTask().execute(stringUrl);
            doDownload(eventsUrl, "getEvents.php",0);
            doDownload(infoUrl,"getInfo.php",1);
        } else {
            // display error
        }

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1250);
                    downloadThread[0].join();
                    downloadThread[1].join();
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    finish();
                }
            }

        };
        timerThread.start();
    }
    protected void doDownload(final String urlLink, final String fileName, final int threadNo) {
        downloadThread[threadNo] = new Thread() {
            public void run() {
                File file= new File(getApplicationContext().getApplicationContext().getFilesDir(),fileName);
                try {
                    URL url = new URL(urlLink);
                    //Log.i("FILE_NAME", "File name is "+fileName);
                    //Log.i("FILE_URLLINK", "File URL is "+url);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // this will be useful so that you can show a typical 0-100% progress bar
                    int fileLength = connection.getContentLength();

                    // download the file
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(file);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Download", "ERROR IS" + e);
                }
            }
        };
        downloadThread[threadNo].start();
    }
}
