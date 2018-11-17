package fr.unice.polytech.si5.cc.l5;

/*
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.appengine.api.blobstore.*;
import com.google.appengine.tools.cloudstorage.*;
import com.google.cloud.datastore.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.logging.Logger;

//[START gcs_imports]
//[END gcs_imports]

/**
 * A simple servlet that proxies reads and writes to its Google Cloud Storage bucket.
 */
@SuppressWarnings("serial")
public class GcsExampleServlet extends HttpServlet {

    public static final boolean SERVE_USING_BLOBSTORE_API = false;

    private static final Logger log = Logger.getLogger(GcsExampleServlet.class.getName());
    /**
     * This is where backoff parameters are configured. Here it is aggressively retrying with
     * backoff, up to 10 times but taking no more that 15 seconds total to do so.
     */
    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    /**Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;

    /**
     * Retrieves a file from GCS and returns it in the http response.
     * If the request path is /gcs/Foo/Bar this will be interpreted as
     * a request to read the GCS file named Bar in the bucket Foo.
     */
//[START doGet]
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GcsFilename fileName = getFileName(req);
        if (SERVE_USING_BLOBSTORE_API) {
            BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
            BlobKey blobKey = blobstoreService.createGsBlobKey(
                    "/gs/" + fileName.getBucketName() + "/" + fileName.getObjectName());
            BlobInfoFactory bif = new BlobInfoFactory();
            BlobInfo bi = bif.loadBlobInfo(blobKey);
            resp.setContentType(bi.getContentType());

            blobstoreService.serve(blobKey, resp);
            //resp.getWriter().println("Pingu");

        } else {
            GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
            GcsFileMetadata metadata = gcsService.getMetadata(fileName);
            GcsFileOptions options = metadata.getOptions();
            resp.setContentType(options.getMimeType());
            copy(Channels.newInputStream(readChannel), resp.getOutputStream());

        }
    }
//[END doGet]

    /**
     * Writes the payload of the incoming post as the contents of a file to GCS.
     * If the request path is /gcs/Foo/Bar this will be interpreted as
     * a request to create a GCS file named Bar in bucket Foo.
     */
//[START doPost]
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GcsFileOptions instance = new GcsFileOptions.Builder().acl("authenticated-read").cacheControl("no-cache").build();
        String user = req.getParameter("user");

        //Check if user parameter is present
        if (user == null) {
            resp.setStatus(403);
            resp.getWriter().println("Error: username not provided");
            return;
        }

        //Check if user exists
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<Entity> query = Query.newEntityQueryBuilder().setKind("user").setFilter(StructuredQuery.PropertyFilter.eq("name", user)).build();
        QueryResults<Entity> results = datastore.run(query);
        if (!results.hasNext()) {
            resp.setStatus(403);
            resp.getWriter().println("Error: invalid username: " + user);
            return;
        }

        Entity entity = results.next();
        GcsFilename fileName = getFileName(req);
        GcsOutputChannel outputChannel;
        outputChannel = gcsService.createOrReplace(fileName, instance);
        copy(req.getInputStream(), Channels.newOutputStream(outputChannel));
        String contentLength = req.getHeader("Content-Length");
        int size = Integer.parseInt(contentLength);
        int toadd = size/(1024*1024); // 1 point earned by MB uploaded

        double score = entity.getDouble("score");
        score += toadd;
        Entity task = Entity.newBuilder(datastore.get(entity.getKey())).set("score", score).build();
        datastore.update(task);

        KeyFactory keyFactory = datastore.newKeyFactory()
            .addAncestor(PathElement.of("user", entity.getKey().getId()))
            .setKind("upload");

        IncompleteKey key = keyFactory.newKey();
        FullEntity<IncompleteKey> aNewUser = FullEntity.newBuilder(key)
            .set("filename", fileName.getObjectName())
            .set("size", toadd).build(); //Size in MB
        datastore.add(aNewUser);


        // Download link to distribute
        //TODO: sendmail
        String url = "http://" + req.getServerName() + "/download.jsp?bucket=" + fileName.getBucketName() + "&file=" + fileName.getObjectName(); // crappy way to construct an URL but whatever
        resp.getWriter().println("<a href='" + url + "'>" + url + "</a>");
    }
//[END doPost]

    private GcsFilename getFileName(HttpServletRequest req) {
        String[] splits = req.getRequestURI().split("/", 4);
        if (!splits[0].equals("") || !splits[1].equals("gcs")) {
            throw new IllegalArgumentException("The URL is not formed as expected. " +
                    "Expecting /gcs/<bucket>/<object>");
        }
        return new GcsFilename(splits[2], splits[3]);
    }

    /**
     * Transfer the data from the inputStream to the outputStream. Then close both streams.
     */
    private void copy(InputStream input, OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}

