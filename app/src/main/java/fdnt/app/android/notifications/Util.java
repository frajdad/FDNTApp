package fdnt.app.android.notifications;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

public class Util {
    public static void scheduleJob(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ComponentName serviceComponent = new ComponentName(context, NotificationStartService.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
            builder.setMinimumLatency(60 * 1000); // wait at least 1 minute
            builder.setOverrideDeadline(15 * 60 * 1000); // maximum delay 15 minutes
            builder.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
        }
    }
}
