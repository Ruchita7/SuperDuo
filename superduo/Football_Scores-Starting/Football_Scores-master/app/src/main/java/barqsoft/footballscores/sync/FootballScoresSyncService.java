package barqsoft.footballscores.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Sync Service
 */
public class FootballScoresSyncService extends Service {
    // Storage for an instance of the sync adapter
    private static FootballScoresSyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {

        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new FootballScoresSyncAdapter(getApplicationContext(), true);
            }
        }
    }
    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     * @param intent
     * @return
     */
   
    @Override
    public IBinder onBind(Intent intent) {
       
        return sSyncAdapter.getSyncAdapterBinder();
    }
}

