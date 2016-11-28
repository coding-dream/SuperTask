package net.ruoxu.supertask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.ruoxu.SuperClient;
import net.ruoxu.SuperTask;
import net.ruoxu.bean.Request;
import net.ruoxu.inter.CallBack;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void execute1(View view) {

        SuperTask superTask = new SuperTask(new CallBack() {
            @Override
            public void before() {
                Toast.makeText(MainActivity.this, "before", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void doInBackgroud() {
                try {
                    Log.d("MainActivity", "doInBackgournd");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void after() {
                Toast.makeText(MainActivity.this, "after", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cancel() {
                Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
            }
        });

        superTask.execute();

//        superTask.cancel(true);

    }


    public void execute2(View view) {
        Request.Builder builder = new Request.Builder().task(new SuperTask(new CallBack() {
            @Override
            public void before() {

            }

            @Override
            public void doInBackgroud() {

            }

            @Override
            public void after() {

            }

            @Override
            public void cancel() {

            }
        }));


        Request request = builder.build();

        SuperClient superClient = null;
        superClient.newCall(request).enqueue(new CallBack() {
            @Override
            public void before() {

            }

            @Override
            public void doInBackgroud() {

            }

            @Override
            public void after() {

            }

            @Override
            public void cancel() {

            }
        });




    }
}
