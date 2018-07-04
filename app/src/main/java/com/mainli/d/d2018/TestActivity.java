package com.mainli.d.d2018;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mainli.d.annotations.BindView;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Mainli on 2018-3-21.
 */

public class TestActivity extends AppCompatActivity {
    @BindView(66666)
    TextView tx6666;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Observable.create(new StartOnSubscribe()).map(new Func1<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                return 222222;
//            }
//        }).subscribe(new ResultSubscriber());
        setContentView(R.layout.activity_test);
        findViewById(R.id.error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("呵呵-TestActivity崩溃了");
            }
        });
//        ImageView viewById = findViewById(R.id.imageview);
//        Glide.with(this).asGif().load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527778355588&di=0b2a90e12137814fdaf68e1bdaecbd59&imgtype=0&src=http%3A%2F%2Fs6.sinaimg.cn%2Fmw690%2F0062ywFUgy6Y2pBG8Vn65%26690")
//                .into(viewById);

    }



    public static class StartOnSubscribe implements Observable.OnSubscribe<Integer> {
        Integer aa = 66666;

        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            subscriber.onNext(aa);
        }
    }

    public static class ResultSubscriber<T> extends Subscriber<T> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(T integer) {
            System.out.println(integer);
        }
    }
}

