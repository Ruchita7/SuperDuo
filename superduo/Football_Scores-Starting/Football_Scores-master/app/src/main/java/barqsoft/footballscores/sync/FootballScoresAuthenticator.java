package barqsoft.footballscores.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

/**
 * Simple authenticator class
 */
public class FootballScoresAuthenticator extends AbstractAccountAuthenticator {
   
	/**
	 * 
	 * @param context
	 */
	public FootballScoresAuthenticator(Context context) {
        super(context);
    }
   
	/**
	 * 
	 * @param r
	 * @param s
	 * @return
	 */
    @Override
    public Bundle editProperties(
            AccountAuthenticatorResponse r, String s) {
        throw new UnsupportedOperationException();
    }
   
    /**
     * 
     * @param r
     * @param s
     * @param s2
     * @param strings
     * @param bundle
     * @return
     * @throws NetworkErrorException
     */
   @Override
    public Bundle addAccount(
            AccountAuthenticatorResponse r,
            String s,
            String s2,
            String[] strings,
            Bundle bundle) throws NetworkErrorException {
        return null;
    }

   /**
    * 
    * @param r
    * @param account
    * @param bundle
    * @return
    * @throws NetworkErrorException
    */
    @Override
    public Bundle confirmCredentials(
            AccountAuthenticatorResponse r,
            Account account,
            Bundle bundle) throws NetworkErrorException {
        return null;
    }

    /**
     * 
     * @param r
     * @param account
     * @param s
     * @param bundle
     * @return
     * @throws NetworkErrorException
     */
    @Override
    public Bundle getAuthToken(
            AccountAuthenticatorResponse r,
            Account account,
            String s,
            Bundle bundle) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * @param s
     * @return
     */
    @Override
    public String getAuthTokenLabel(String s) {
        throw new UnsupportedOperationException();
    }


    /**
     * 
     * @param r
     * @param account
     * @param s
     * @param bundle
     * @return
     * @throws NetworkErrorException
     */
    @Override
    public Bundle updateCredentials(
            AccountAuthenticatorResponse r,
            Account account,
            String s, Bundle bundle) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * @param r
     * @param account
     * @param strings
     * @return
     * @throws NetworkErrorException
     */
    @Override
    public Bundle hasFeatures(
            AccountAuthenticatorResponse r,
            Account account, String[] strings) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }
}
