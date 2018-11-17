package fr.unice.polytech.si5.cc.l5.model;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public class Utils {
    public static int canDownload(UserLevel level, long t1, long t2, long t3, long t4) {
        int timeout = 60000;
        long currentTime = System.currentTimeMillis();
        switch (level) {
            case NOOB:
                if (currentTime - t1 > timeout) {
                    return 1;
                }
                break;
            case CASUAL:
                if (currentTime - t1 > timeout) {
                    return 1;
                } else if (currentTime - t2 > timeout){
                    return 2;
                }
                break;
            case LEET:
                if (currentTime - t1 > timeout) {
                    return 1;
                } else if (currentTime - t2 > timeout) {
                    return 2;
                } else if (currentTime - t3 > timeout) {
                    return 3;
                } else if (currentTime - t4 > timeout) {
                    return 4;
                }
                break;
            default:
                return 0;
        }
        return 0;
    }

    public static void updateTimestamp(int index, Key key, Datastore datastore) {
        Entity.Builder builder = Entity.newBuilder(datastore.get(key));
        switch (index) {
            case 1:
                builder.set("downloadTimestamp1", System.currentTimeMillis());
                break;
            case 2:
                builder.set("downloadTimestamp2", System.currentTimeMillis());
                break;
            case 3:
                builder.set("downloadTimestamp3", System.currentTimeMillis());
                break;
            case 4:
                builder.set("downloadTimestamp4", System.currentTimeMillis());
                break;
            default:
                return;
        }


        Entity user = builder.build();
        datastore.update(user);
    }

    public static int getActionsMax(UserLevel rank) {
        switch (rank) {
            case NOOB:
                return 1;
            case CASUAL:
                return 2;
            case LEET:
                return 4;
            default:
                return 0;
        }
    }

    public static String getErrorActionMessage(long maxactions) {
        return "You cannot have more than " + maxactions + " download or upload requests at the same time";
    }
}
