package com.pratik.productize.utils;

public class Constants {

    public static final String TAG = "com.pratik.debug" ;
    public static final String TAG1 = "com.pratik.debu" ;
    public static final String TAG2 = "com.pratik.myapp" ;

    public static final String PREF_NAME = "com.pratik.productize";

    public static final String TASK_ID = "taskId";

    // Preferences name
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public static final String IS_TASK_ACTIVE = "IsTaskActive";
    public static final String IS_TASK_SCHEDULED = "IsTaskSchedule";
    public static final String IS_TASK_ONGOING = "IsTaskOngoing";
    public static final String ACTIVE_TAG = "activeTag";
    public static final String HOME_TIME = "homeTime";
    public static final String WORK_TIME = "workTime";
    public static final String TASK_HOURS = "taskHours";

    //intent request code
    public static final String ALARM_INTENT_RC = "alarmRequestCode";
    public static final String ALARM_INTENT_FLAG = "alarmIntentFlag";

    //Tags
    public static final int TAG_HOME = 0;
    public static final int TAG_WORK = 1;
    public static final int TAG_OTHER = -1;

    //Job ids
    public static final int JOB_ID_TASK = 69;
    public static final int JOB_ID_WIPE = 7;

    //Job intervals
    public static final long JOB_INTERVAL = 1000*60*20;
    public static final long JOB_FLEX_INTERVAL = 1000*60*15;

    //alarm flags and request codes
    public static final int FLAG_ALARM1 = 0;
    public static final int REQUEST_CODE_ALARM1 = 0;

    public static final int FLAG_ALARM2 = 1;
    public static final int REQUEST_CODE_ALARM2 = 1;

    public static final int FLAG_ALARM3 = 2;
    public static final int REQUEST_CODE_ALARM3 =2;

    //notification channel ID
    public static final String CHANNEL_ID = "Reminders";

    //notification ID
    public static final int NOTIFICATION_ID = 32;

    public static final boolean SHOW = true;
    public static final boolean HIDE = false;


}
