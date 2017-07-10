package com.yishu.messagequeue;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * oncreate里的线程可以更新ui
 * 主线程默认创建loop和messagequeue
 * 工作线程需要调用Looper.prepare();才可以创建loo和messagequeue
 * 调用looper.loop()使消息循环处理
 * messagequeue为空的时候，线程进入休眠状态，等待下一个message的到来。
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        Log.e(TAG, "current 0 id is " + Thread.currentThread().getId());

    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Looper.prepare();
//                Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {//崩溃 工作线程的loop
                Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {//不崩溃 主线程的loop
                    @Override
                    public boolean handleMessage(Message msg) {
                        Log.e(TAG, "current 1 id is " + Thread.currentThread().getId());
                        Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                        textView.setText("test");
                        return false;
                    }
                });
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessageDelayed(msg,1000);
                Looper.loop();
            }
        });

        thread.start();
    }
}
