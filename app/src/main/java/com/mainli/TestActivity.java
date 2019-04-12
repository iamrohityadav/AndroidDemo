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
import retrofit2.http.Path;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
//        testNetwork(sharedP);
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
                .baseUrl("https://api.github.com/")//
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        service.listRepos("Android-Mainli").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(repos -> {
            L.d("Mainli", repos.toString());
            sharedP.encode("repos", repos.get(0));
        });
    }

    public interface GitHubService {
        @GET("users/{user}/repos")
        Observable<List<Repos>> listRepos(@Path("user") String user);
    }

    @Keep
    public static class Repos implements Parcelable {

        private int id;
        private String node_id;
        private String name;
        private String full_name;
        private String html_url;
        private boolean fork;
        private String url;
        private String forks_url;
        private String keys_url;

        @Override
        public String toString() {
            return "Repos{" + "id=" + id + ", node_id='" + node_id + '\'' + ", name='" + name + '\'' + ", full_name='" + full_name + '\'' + ", html_url='" + html_url + '\'' + ", fork=" + fork + ", url='" + url + '\'' + ", forks_url='" + forks_url + '\'' + ", keys_url='" + keys_url + '\'' + '}';
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNode_id() {
            return node_id;
        }

        public void setNode_id(String node_id) {
            this.node_id = node_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getHtml_url() {
            return html_url;
        }

        public void setHtml_url(String html_url) {
            this.html_url = html_url;
        }

        public boolean isFork() {
            return fork;
        }

        public void setFork(boolean fork) {
            this.fork = fork;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getForks_url() {
            return forks_url;
        }

        public void setForks_url(String forks_url) {
            this.forks_url = forks_url;
        }

        public String getKeys_url() {
            return keys_url;
        }

        public void setKeys_url(String keys_url) {
            this.keys_url = keys_url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.node_id);
            dest.writeString(this.name);
            dest.writeString(this.full_name);
            dest.writeString(this.html_url);
            dest.writeByte(this.fork ? (byte) 1 : (byte) 0);
            dest.writeString(this.url);
            dest.writeString(this.forks_url);
            dest.writeString(this.keys_url);
        }

        public Repos() {
        }

        protected Repos(Parcel in) {
            this.id = in.readInt();
            this.node_id = in.readString();
            this.name = in.readString();
            this.full_name = in.readString();
            this.html_url = in.readString();
            this.fork = in.readByte() != 0;
            this.url = in.readString();
            this.forks_url = in.readString();
            this.keys_url = in.readString();
        }

        public static final Parcelable.Creator<Repos> CREATOR = new Parcelable.Creator<Repos>() {
            @Override
            public Repos createFromParcel(Parcel source) {
                return new Repos(source);
            }

            @Override
            public Repos[] newArray(int size) {
                return new Repos[size];
            }
        };
    }
}

