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
//[START gcs_imports]
import com.google.appengine.tools.cloudstorage.*;

//[END gcs_imports]
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.nio.channels.Channels;

import java.util.logging.Logger;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.auth.oauth2.*;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Storage.SignUrlOption;
import com.google.cloud.http.HttpTransportOptions.DefaultHttpTransportFactory ;

/**
 * A simple servlet that proxies reads and writes to its Google Cloud Storage bucket.
 */
@SuppressWarnings("serial")
public class GcsExampleServlet extends HttpServlet {


    private static final String jsonGcsKey = "{\n" +
               "  \"type\": \"service_account\",\n" +
               "  \"project_id\": \"polar-winter-218511\",\n" +
               "  \"private_key_id\": \"ea7a747610ebae18411fb5218ac2e079e5e0e586\",\n" +
               "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDadcGCp3BzqhUX\\nKv51yQZ00D7nLrK6UHH+jfl4RMj+9Po9yISWKg7jaeCa6XumksknZJN2FnKs23RE\\npSNp1doduuAFbywqi00dZT8H6JCKN6DvXiym9qCf07AmGwOvLRSGmtSvAF8unBrP\\nyQSQJvdM4LtIsihoAYCr0osEBvi8ZN9SJPPB86Iu3x+kgWVgHSYdtejQa4o+mCju\\nwg3PQkreRKY55YeoPMqNeqc46BYb8LbWO7RfyviPKHNymTvvtUeV+Fc++AyYbtHI\\nK5UI8i9V8GMxF1Z7EwYWaMIStK5BVFOuH+B6AWG4Zc0TCWGaVROd6dw6VX013ijq\\nqBfY6LplAgMBAAECggEADbNA1X7GsJpEBFXv61cWDpFYwIrPLpwI6hvhP0ehC0w1\\nvVYr6nbsxBpJua1W0dV2w/+st8Ukn+6LVhq9I9ADg8XKcszMCkb+KQ/GaiqNlf1z\\nHE/doQLqg9t0uo8LJhs/pElleSwey/+CNvG2VYv8Clg1ppN1sUAW36+uhCwjwTy8\\nkYafApF8S78xyjCim0f2AyEI4bWR9e4BLeUkN03kCKkJTY9YiomZd+YrBLEdBUrw\\nh7r7sTteY6wBfmYSaS7f3otq+Zo+z//cYlvwTOTOCPtt965cL7fgmG9S8p52JuJd\\nm7M32A3txCDTUqXmN31Gvl8csODtvcIM4Zlw4C5QgQKBgQDxevAmqPcpGawnhp5L\\nEVcPlRIUG2lWUuT0DXnAc3yS4VgSJusftpvSj4MXYkw/4AcYVv+NeeUWZcw0VI60\\nzZTyXoEPd0Tfr4InhKmtkz52Nba65oOVJxK+CqVUzOihADibQSPckCA8CxOwHLYi\\nTxs+O8xzIsZHGaHvzJvefjFsMQKBgQDnmHipJWmrtS8i4lBt5vfWT2UrXUe8XFE4\\n32TSoAhIVc2vsUWEmmsHGMXsl7DIgW1t/vQvG0Yz8Sca9zN2XLh/O/649FiViXVs\\ndhCAIsqWQiSLkfASq5Ki9zaySlGMHJNn0nYAeIf2lZ1j4LKeAiHmmK/32sp4V0vh\\nMJbYdYzIdQKBgDfb8GGBBGuFfMVH5qYGxNTd7uinzp4hXsT1uQxDqrVvWygXyJv1\\naeYzPGfUXT8JW5rNxR+86rlbovkX9krso1/NbJ1fQZdcygbKMVXtka1Wj4Z8fEOs\\nXjGH1Obg348etfl0AA+tZ0d8aHl8qsUU94BV5N1dVfBshYErG4NEPbqBAoGASSy4\\nuyDyEWnO+eOt6/u1RQaS/a+ccdxtYV3cOlyMIWbS9Xc6ctpjhLuEFewb1E5op/+N\\nYbDpbpH8fB2tKfHD7ZekNQYnUyA6dMmdDw9vGSjO6TZ0N0H0s/8OTikDORTyUjSC\\nSkXLF9NQBnXBmgEsevK1bse3f2x9siorDzuc/t0CgYAWpiGwKAd6tUCH9B9cX3xk\\nB2ZfciLO+lzqSKS6mg0d0Z1TjEuKPdEnVKwuh+UfWgqwkV8ColpBNcfMdyOfbwu4\\nRU3s9l51s3SXmlIRizEZhHEXumMHcNQ99ZLCZ7ojZ9augR6xlTScv6pmEVzplV2n\\ny17w33U8Vmrt7fMNiDKoQw==\\n-----END PRIVATE KEY-----\\n\",\n" +
               "  \"client_email\": \"polar-winter-218511@appspot.gserviceaccount.com\",\n" +
               "  \"client_id\": \"109656186893663132583\",\n" +
               "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
               "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
               "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
               "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/polar-winter-218511%40appspot.gserviceaccount.com\"\n" +
               "}";







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

        GcsFilename fileName = getFileName(req);
        GcsOutputChannel outputChannel;
        outputChannel = gcsService.createOrReplace(fileName, instance);
        copy(req.getInputStream(), Channels.newOutputStream(outputChannel));


        Storage storage = StorageOptions.getDefaultInstance().getService();
        String bucketName = fileName.getBucketName();
        String blobName = fileName.getObjectName();

        InputStream is = new ByteArrayInputStream(jsonGcsKey.getBytes());

        URL signedUrl = storage.signUrl(com.google.cloud.storage.BlobInfo.newBuilder(bucketName, blobName).build(),
                            14,
                            TimeUnit.SECONDS, SignUrlOption.signWith(ServiceAccountCredentials.fromStream(is)));

        resp.getWriter().println(signedUrl);
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

