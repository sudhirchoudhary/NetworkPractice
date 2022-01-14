package com.example.networkpractice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networkpractice.adapter.MyAdapter;
import com.example.networkpractice.retrofit.Post;
import com.example.networkpractice.solution.ConnectionLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView tvNetworkResult;
    private ImageView imageView;
    private RecyclerView rvMain;
    private final List<Post> mList = new ArrayList<>();
    private MyAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectionLiveData connectionLiveData = new ConnectionLiveData(NetworkApplication.getApplication());
        connectionLiveData.observe(this, activeNetworkStateObserver);

        tvNetworkResult = findViewById(R.id.tvNetworkResult);
        imageView = findViewById(R.id.ivNoInternet);
        rvMain = findViewById(R.id.rvMain);

        rvMain.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvMain.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);
    }

    private final Observer<Boolean> activeNetworkStateObserver = new Observer<Boolean>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(Boolean isConnected) {
            if (isConnected) {
                tvNetworkResult.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                if (mList.size() == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    getPosts();
                }
                Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void getPosts() {
        Call<List<Post>> call = NetworkApplication.getJsonPlaceHolderApi().getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    mList.addAll(response.body());
                }
                adapter = new MyAdapter(mList);
                rvMain.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }
}