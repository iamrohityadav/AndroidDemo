package com.mainli.d.d2018;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

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

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TextView view = new TextView(this);
        setContentView(view);
        Retrofit retrofit = new Retrofit.Builder()//
                .addConverterFactory(GsonConverterFactory.create())//
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
                .baseUrl("https://api.github.com/")//
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        service.listRepos("Android-Mainli").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(repos -> Log.d("Mainli", repos.toString()));
    }

    public interface GitHubService {
        @GET("users/{user}/repos")
        Observable<List<Repos>> listRepos(@Path("user") String user);
    }

    public static class Repos {

        private int id;
        private String node_id;
        private String name;
        private String full_name;
        private String html_url;
        private Object description;
        private boolean fork;
        private String url;
        private String forks_url;
        private String keys_url;

        @Override
        public String toString() {
            return "Repos{" + "id=" + id + ", node_id='" + node_id + '\'' + ", name='" + name + '\'' + ", full_name='" + full_name + '\'' + ", html_url='" + html_url + '\'' + ", description=" + description + ", fork=" + fork + ", url='" + url + '\'' + ", forks_url='" + forks_url + '\'' + ", keys_url='" + keys_url + '\'' + '}';
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

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
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
    }
}

