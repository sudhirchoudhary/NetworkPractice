package com.example.networkpractice.solution;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.HashSet;
import java.util.Set;

public class ConnectionLiveData extends LiveData<Boolean> {
    private static final String TAG = "ConnectionLiveData";
    private final ConnectivityManager connectivityManager;
    private final ConnectivityManager.NetworkCallback networkCallback;
    private final NetworkRequest request;
    private final Set<Network> set = new HashSet<>();

    public ConnectionLiveData(Context context) {
        Log.e(TAG, "ConnectionLiveData created");
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkCallback = createNetworkCallBack();
        request = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.e(TAG, "ConnectionLiveData is in active state");

        connectivityManager.registerNetworkCallback(request, networkCallback);
    }

    private ConnectivityManager.NetworkCallback createNetworkCallBack() {
        Log.e(TAG, "NetworkCallBack created");
        return new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                Log.e("Connect" , "Available: " + network.toString());
            }
            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                Log.e("Connect", "Lost : " + network.toString());
                postValue(false);
                set.remove(network);
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                Log.e("Connect", network.toString());
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                boolean hasNetworkCapability = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                if(hasNetworkCapability) {
                    if(set.isEmpty() || !set.contains(network)) {
                        postValue(true);
                        set.add(network);
                    }
                }
            }
        };
    }

    @Override
    protected void onInactive() {
        Log.e(TAG, "ConnectionLiveData is in inactive state");
        super.onInactive();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    public void checkNetworkState() {
        try {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            postValue(networkInfo!=null && networkInfo.isConnectedOrConnecting());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
