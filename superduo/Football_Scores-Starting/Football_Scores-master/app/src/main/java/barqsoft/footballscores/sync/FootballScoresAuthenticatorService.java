package barqsoft.footballscores.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Authenticator service class
 */
public class FootballScoresAuthenticatorService  extends Service {

    // Instance field that stores the authenticator object
    private FootballScoresAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new FootballScoresAuthenticator(this);
    }
    
    /**
     * 
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}

