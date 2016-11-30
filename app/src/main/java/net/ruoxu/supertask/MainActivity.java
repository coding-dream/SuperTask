package net.ruoxu.supertask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.ruoxu.task.SuperClient;
import net.ruoxu.task.SuperTask;
import net.ruoxu.task.bean.Request;
import net.ruoxu.task.bean.Response;
import net.ruoxu.task.inter.CallBack;

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
            public Response doInBackgroud(Request request) {
                try {
                    Log.d("MainActivity", "doInBackgournd");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public void after(Response response) {
                Toast.makeText(MainActivity.this, "after: response:"+response, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cancel() {
                Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
            }
        },null); //此种暂不支持Request参数形式

        superTask.execute();

//        superTask.cancel(true);

    }

    //request 和 callback的问题
    public void execute2(View view) {
        Request.Builder builder = new Request.Builder().task(SuperTask.defaultTask());

        Request request = builder.build();

        SuperClient superClient = new SuperClient();

        superClient.newCall(request).enqueue(new CallBack() {
            @Override
            public void before() {
                Toast.makeText(MainActivity.this, "----> before", Toast.LENGTH_SHORT).show();
            }

            @Override
            public Response doInBackgroud(Request request) {
                try {
                    Log.d("MainActivity", "----> doInBackgournd start");

                    Thread.sleep(5000);
                    Response response = new Response("xiaoming");


                    Log.d("MainActivity", "----> doInBackgournd end");

                    return response;

                } catch (InterruptedException e) {
                    return null;
                }

            }

            @Override
            public void after(Response response) {
                if (response != null) {
                    Toast.makeText(MainActivity.this, "----> after response:"+response.name, Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void cancel() {
                Toast.makeText(MainActivity.this, "----> cancel", Toast.LENGTH_SHORT).show();
            }
        });




    }
}
