package com.weebinatidi.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.weebinatidi.R;
import com.weebinatidi.ui.weebi2.Parametres;

import java.util.Random;

/**
 * Created by birantesy on 11/02/2018.
 *
 * Cette classe contient toutes
 * les notifications.
 *
 */

public class Notification {


    public static void showSalesSuccess(final Context context) {
        int notificationId = new Random().nextInt();
        Intent intent = new Intent(context, Parametres.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder notification = NotificationHelper.createNotificationBuider(context,
                "Weebi", "Vente effectuée !", R.drawable.weebilogo, pIntent)
                .setColor(context.getResources().getColor(R.color.couleurlogo));
        notification.setPriority(android.app.Notification.PRIORITY_HIGH).setVibrate(new long[0]);
        NotificationHelper.showNotification(context, notificationId, notification.build());
    }



    public static void showAlertConfigureShop(final Context context) {
        int notificationId = new Random().nextInt();
        Intent intent = new Intent(context, Parametres.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder notification = NotificationHelper.createNotificationBuider(context,
                "Compte Weebi", "Veuillez configurer votre compte weebi", R.drawable.weebilogo, pIntent)
                .setColor(context.getResources().getColor(R.color.couleurlogo));
        notification.setPriority(android.app.Notification.PRIORITY_HIGH).setVibrate(new long[0]);
        NotificationHelper.showNotification(context, notificationId, notification.build());
    }


}
