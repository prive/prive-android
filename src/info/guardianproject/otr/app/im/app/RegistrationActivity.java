package info.guardianproject.otr.app.im.app;

import info.guardianproject.otr.app.im.provider.Imps;
import info.guardianproject.otr.app.im.service.ImServiceConstants;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockActivity;

public class RegistrationActivity extends SherlockActivity {

    protected final static String LOG_TAG = "RegistrationActivity";

    long mProviderId;

    private SignInHelper mSignInHelper;

    private ImPluginHelper helper = ImPluginHelper.getInstance(this);

    final String mUserName = "azsxdc9592@jabber.ru";
    final String pass = "qwerty123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressBar pb = new ProgressBar(this);
        setContentView(pb, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mSignInHelper = new SignInHelper(this);

        final long accountId = storeNewAccount();

        autoSignInNewAccount(accountId);

        startNewChat(accountId);

        finish();

    }

    protected void autoSignInNewAccount(long accountId) {
        final boolean isActive = true;
        mSignInHelper.signIn(pass, mProviderId, accountId, isActive);

    }

    protected void startNewChat(long accountId) {
        Intent intent = new Intent(this, NewChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ImServiceConstants.EXTRA_INTENT_ACCOUNT_ID, accountId);
        startActivity(intent);
    }

    protected long storeNewAccount() {
        long accountId = 0;
        ContentResolver cr = getContentResolver();
        mProviderId = helper.createAdditionalProvider(helper.getProviderNames().get(0));
        accountId = ImApp.insertOrUpdateAccount(cr, mProviderId, mUserName, pass);
        Log.i(LOG_TAG, "new accounId = " + accountId);
        return accountId;
    }
}
