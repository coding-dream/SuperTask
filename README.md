## SuperTask
 参考AsyncTask源码，进一步封装改造更易用的异步任务库

## 如何使用


在build.gradle 中添加

```
dependencies {
    compile 'com.github.wangli0:SuperTask:v0.3'
}

```


类似于okhttp的调用方式

```
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



```

## 如何取消任务

获取到 当前执行的 Call call

```
call.cancel();
```

如此简单，可以任意扩展，样例代码见: app下的MainActivity




