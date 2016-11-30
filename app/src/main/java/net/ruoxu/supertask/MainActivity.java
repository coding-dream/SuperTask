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
import net.ruoxu.task.bean.TestBean;
import net.ruoxu.task.inter.Call;
import net.ruoxu.task.inter.CallBack;

public class MainActivity extends AppCompatActivity {
    SuperTask currentTask ; //用于演示取消 execute1
    Call currentCall;       //用于演示取消 execute2

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

        currentTask = superTask;

    }

    public void execute2(View view) {
        SuperTask superTask = SuperTask.create();
        TestBean testBean = new TestBean(" sister","20"); //这里可以是任意请求参数对象
        Request request = new Request.Builder().task(superTask).params(testBean).build();

        SuperClient superClient = SuperClient.getInstance();
        Call call = superClient.newCall(request);

        call.enqueue(new CallBack() {
            @Override
            public void before() {
                Toast.makeText(MainActivity.this, "----> before", Toast.LENGTH_SHORT).show();
            }

            @Override
            public Response doInBackgroud(Request request) {
                try {

                    Log.d("MainActivity", "----> doInBackgournd start");

                    Thread.sleep(3000);
                    TestBean bean = request.params(TestBean.class);

                    Response response = new Response();
                    response.params("I love you "+bean.getName());

                    Log.d("MainActivity", "----> doInBackgournd end");

                    return response;

                } catch (InterruptedException e) {
                    return null;
                }

            }

            @Override
            public void after(Response response) {
                if (response != null) {
                    String msg = response.params(String.class);

                    Toast.makeText(MainActivity.this, "----> after response:"+msg, Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void cancel() {
                Toast.makeText(MainActivity.this, "----> cancel", Toast.LENGTH_SHORT).show();
            }
        });

        currentCall = call;


    }


    public void cancel(View view) {
        switch (view.getId()) {
            case R.id.cancel_action1:
                if (currentTask != null) {
                    currentTask.cancel(true);
                }
                break;
            case R.id.cancel_action2:
                if (currentCall != null) {
                    currentCall.cancel();

                }
                break;
        }
    }
}
