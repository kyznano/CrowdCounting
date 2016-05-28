package hcmut.framework;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by minh on 05/08/2015.
 */
public abstract class FrameworkActivity extends Activity {
    protected DatabaseAdapter mDatabase;
    protected BasicFlow mUI;
    protected BasicFlow mController;
    protected BasicFlow mServer;

    protected abstract void initFramework();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFramework();
    }

    public BasicFlow getUI() {
        return mUI;
    }

    public BasicFlow getController() {
        return mController;
    }

    public BasicFlow getServer() {
        return mServer;
    }

    public DatabaseAdapter getDatabase() { return mDatabase; }

    public void finishApp() {
        this.finish();
        System.exit(0);
    }
}
