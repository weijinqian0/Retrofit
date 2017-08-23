package com.example.weijinqian.retrofit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.weijinqian.retrofit.network.Downloader;
import com.example.weijinqian.retrofit.network.rx.CallBack;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // 下载对应的熊头文件
        String fileName="file_name";
        Downloader.Builder builder = new Downloader.Builder();
        builder.url("http:api.baidu.com").targetFile(new File(FilesManager.getInstance()
                .getPrivateExternalPath(FilesManager.DIR_EXTERNAL_DOWNLOAD) + fileName));
        Downloader downloader = builder.build();
        downloader.getDownloadCall().enqueue(new CallBack<Downloader.Result>() {
            @Override
            public void onFail(int code, String message) {
//                if (Macro.IS_CATCH_LOG_ENABLE) {
//                    BDLog.printStackTrace(new Throwable(message));
//                }
            }

            @Override
            public void onSuc(Downloader.Result response) {
//                if (Macro.IS_CATCH_LOG_ENABLE) {
//                    if(null != response){
//                        BDLog.d("Sophie", response.toString());
//                    }
//                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
