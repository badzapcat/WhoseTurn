package jmoghadam.whoseturn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.widget.Toast;


/**
 * Created by joe on 1/24/16.
 */
public class SyncReceiver extends BroadcastReceiver {

    private static final String PARTNER1_MAC = "ea:50:8b:5a:c4:ef";
    private static final String PARTNER2_MAC = "ae:cf:85:6b:ba:fd";

    private WifiP2pManager mManager;
    private Channel mChannel;
    private SyncActivity mActivity;
    private PeerListListener myPeerListListener;

    public SyncReceiver(WifiP2pManager manager, Channel channel,
                                       SyncActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
        this.myPeerListListener = new SyncPeerListener();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
            } else {
                // Wi-Fi P2P is not enabled
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (mManager != null) {
                mManager.requestPeers(mChannel, myPeerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }

    private class SyncPeerListener implements PeerListListener {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {
            for (WifiP2pDevice device: peers.getDeviceList()) {
                if (device.deviceAddress.equals(PARTNER1_MAC)) {
                    mActivity.connectToPartner(device);
                } else if (device.deviceAddress.equals((PARTNER2_MAC))) {
                    mActivity.connectToPartner(device);
                }
            }
        }
    }
}
