package fr.unice.polytech.si5.cc.l5;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.apphosting.api.ApiProxy;
import com.google.cloud.storage.*;

import java.util.Collections;

public class BucketManager {
    private static final String DEFAULT_BUCKET_NAME = "polar-winter-218511";

    private Storage storage;
    private static Storage.PredefinedAcl DEFAULT_ACL = Storage.PredefinedAcl.PUBLIC_READ;

    /* PredefinedAcl
    ALL_AUTHENTICATED_USERS
    AUTHENTICATED_READ
    BUCKET_OWNER_FULL_CONTROL
    BUCKET_OWNER_READ
    PRIVATE
    PROJECT_PRIVATE
    PUBLIC_READ
    PUBLIC_READ_WRITE
    */

    BucketManager() {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public void createBucket(String bucketName, Storage.PredefinedAcl pacl/*, Iterable<BucketInfo.LifecycleRule> lifecycleRules*/) {
        Bucket bucket = storage.create(BucketInfo.newBuilder(bucketName)/*.setLifecycleRules(lifecycleRules)*/.build(), Storage.BucketTargetOption.predefinedAcl(pacl));
    }

    public void createDefaultBuckets() {
        createBucket(getDefaultBucketName(), DEFAULT_ACL);
    }

    public String getDefaultBucketName() {
        return DEFAULT_BUCKET_NAME;
    }

    public void scheduleDeletion(GcsFilename gcsFilename, long countdown) {
        Queue queue = QueueFactory.getQueue("delete-file-queue");
        queue.add(TaskOptions.Builder.withUrl("/delete")
                    .param("bucket", gcsFilename.getBucketName())
                    .param("file", gcsFilename.getObjectName())
                    .countdownMillis(countdown));

    }
}
