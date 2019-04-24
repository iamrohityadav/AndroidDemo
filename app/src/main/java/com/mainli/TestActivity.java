package com.mainli;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.protobuf.ByteString;
import com.mainli.log.L;
import com.mainli.proto.LoginInfo;
import com.seekting.demo_lib.Demo;
import com.tencent.mmkv.MMKV;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Mainli on 2018-3-21.
 */
@Demo(title = "测试")
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TextView view = new TextView(this);
        setContentView(view);
        testNetwork();
        testGoogleProtocolBuffer(view);
    }

    private void testGoogleProtocolBuffer(TextView view) {
        LoginInfo.Login loginInfo = LoginInfo.Login.newBuilder().setAccount("12345678911--").setPassword("789890").build();
        ByteString bytes1 = loginInfo.toByteString();
        try {
            LoginInfo.Login login = LoginInfo.Login.parseFrom(bytes1);
            view.setText(login.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testNetwork() {
        final MMKV sharedP = MMKV.mmkvWithID("TestActivity", MMKV.SINGLE_PROCESS_MODE, "qwqweqwe");
        Retrofit retrofit = new Retrofit.Builder()//
                .addConverterFactory(GsonConverterFactory.create())//
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
                .baseUrl("https://www.wanandroid.com/")//
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        service.getCoupon().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(coupons -> {
            L.d("Mainli", coupons.toString());
            sharedP.encode("coupon", coupons.get(0));
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                L.e("Mainli", throwable);
            }
        });
    }

    public interface GitHubService {
        @GET("tools/mockapi/2385/coupon")
        Observable<List<Coupon>> getCoupon();
    }

    @Keep
    public static class Coupon implements Parcelable {

        /**
         * invalidTime : 2018-08-30
         * obtainTime : 2018-08-15
         * code : KBRC332GYBWX
         * discountedPrice : $30
         * useAddress : cmgo73.myshopify.com
         */

        private String invalidTime;
        private String obtainTime;
        private String code;
        private String discountedPrice;
        private String useAddress;

        public String getInvalidTime() {
            return invalidTime;
        }

        public void setInvalidTime(String invalidTime) {
            this.invalidTime = invalidTime;
        }

        public String getObtainTime() {
            return obtainTime;
        }

        public void setObtainTime(String obtainTime) {
            this.obtainTime = obtainTime;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDiscountedPrice() {
            return discountedPrice;
        }

        public void setDiscountedPrice(String discountedPrice) {
            this.discountedPrice = discountedPrice;
        }

        public String getUseAddress() {
            return useAddress;
        }

        public void setUseAddress(String useAddress) {
            this.useAddress = useAddress;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.invalidTime);
            dest.writeString(this.obtainTime);
            dest.writeString(this.code);
            dest.writeString(this.discountedPrice);
            dest.writeString(this.useAddress);
        }

        public Coupon() {
        }

        protected Coupon(Parcel in) {
            this.invalidTime = in.readString();
            this.obtainTime = in.readString();
            this.code = in.readString();
            this.discountedPrice = in.readString();
            this.useAddress = in.readString();
        }

        public static final Parcelable.Creator<Coupon> CREATOR = new Parcelable.Creator<Coupon>() {
            @Override
            public Coupon createFromParcel(Parcel source) {
                return new Coupon(source);
            }

            @Override
            public Coupon[] newArray(int size) {
                return new Coupon[size];
            }
        };

        @Override
        public String toString() {
            return "Coupon{" + "invalidTime='" + invalidTime + '\'' + ", obtainTime='" + obtainTime + '\'' + ", code='" + code + '\'' + ", discountedPrice='" + discountedPrice + '\'' + ", useAddress='" + useAddress + '\'' + '}';
        }
    }
}

