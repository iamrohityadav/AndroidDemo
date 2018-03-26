package com.mainli.d.d2018;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mainli.d.annotations.BindView;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Mainli on 2018-3-21.
 */

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.bottom)
    TextView tx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.create(new StartOnSubscribe()).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return 222222;
            }
        }).subscribe(new ResultSubscriber());

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

