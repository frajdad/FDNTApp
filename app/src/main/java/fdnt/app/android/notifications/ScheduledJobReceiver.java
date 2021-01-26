package fdnt.app.android.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScheduledJobReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Util.scheduleJob(context);
    }
}