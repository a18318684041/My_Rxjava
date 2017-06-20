package com.example.administrator.my_rxjava;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {
    //Rxjava在哪个线程定义subscribe方法就是在哪个线程进行生产，其遵循的是线程不变的原则
    private Button btn;
    private ImageView img;
    //观察者
    private  Observable observable;
    private  Subscriber<String> subscriber;
    //触发事件
    private Observer<String> observer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //观察者，决定时间触发的时候会进行什么操作
        observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                Toast.makeText(MainActivity.this,"onNext" + s,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this,"onCompleted",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this,"onError" + e.toString(),Toast.LENGTH_LONG).show();
            }
        };
        //观察者，上面的observer在运行结束后也会转化为subscriber对象，比上面的对象多一个onstart方法
        subscriber = new Subscriber<String>() {
            @Override
            public void onStart() {
                //在事件还没发送之前调用，可以用来做数据的清零操作
                Log.d("AAA", "onStart: ");
            }

            @Override
            public void onCompleted() {
                Log.d("AAA", "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("AAA", "onError: ");
            }

            @Override
            public void onNext(String s) {
                Log.d("AAA", "onNext: " + s);
            }
        };
        //相当于调用三次ontext方法，最后调用oncomplete方法
        observable = Observable.just("Hello", "Hi", "Aloha");
        //Observable.subscribe（）也支持下面三种方法的单独回调
        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Log.d("AAA", "onNext:"+s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                Log.d("AAA", "onError");
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.d("AAA", "completed");
            }
        };
        initViews();
    }

    private void initMap() {
       /* RxJava 提供了对事件序列进行变换的支持,以下是将Integer的drawable资源转换为Drawable类型*/
        Observable.just(R.drawable.bg).map(new Func1<Integer, Drawable>() {
            @Override
            public Drawable call(Integer integer) {
                return getResources().getDrawable(integer);
            }
        }).subscribe(new Action1<Drawable>() {
            @Override
            public void call(Drawable drawable) {
                img.setImageDrawable(drawable);
            }
        });
    }

    private void initViews() {
         btn = (Button) findViewById(R.id.btn);
         img = (ImageView) findViewById(R.id.img);
         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                /* observable.subscribe(subscriber);*/
                //打印字符数组
                 final String[] names = {"xiaoming","xiaodong","xiaohei"};
                 List<String> students = new ArrayList<String>();
                 students.add("xiaoming");
                 students.add("xiaodong");
                 students.add("xiaohei");
                 Observable.from(students).subscribe(new Action1<String>() {
                     @Override
                     public void call(String s) {
                         Log.d("AAA", "call: "+s);
                     }
                 });
                 initMap();
             }
         });

    }
}
