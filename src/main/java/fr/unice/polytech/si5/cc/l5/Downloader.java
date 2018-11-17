package fr.unice.polytech.si5.cc.l5;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Stuff for signed URL
import java.net.URL;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.google.auth.oauth2.*;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Storage.SignUrlOption;

// Stuff to check file exist
import com.google.appengine.tools.cloudstorage.*;

// Stuff to authenticate user
import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import fr.unice.polytech.si5.cc.l5.model.UserLevel;

@WebServlet(name = "Downloader", value = "/download")
public class Downloader extends HttpServlet {

    // Key for signed URL
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

    private static final String bucketName = "polar-winter-218511";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = req.getParameter("file");
        String username = req.getParameter("username");
        String bucketName = req.getParameter("bucket");
        if (filename == null || username == null || bucketName == null) {
            resp.setStatus(400);
            resp.getWriter().println("Error: missing file, username or bucket");
            return;
        }

        //Check if user exists and didn't hit its download limit
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query <Entity> query = Query.newEntityQueryBuilder().setKind("user").setFilter(PropertyFilter.eq("name", username)).build();
        QueryResults <Entity> results = datastore.run(query);
        if (! results.hasNext()) {
            resp.setStatus(403);
            resp.getWriter().println("Error: invalid username: " + username);
            return;
        }
        Entity entity = results.next();
        long downloadTimestamp1 = entity.getLong("downloadTimestamp1");
        long downloadTimestamp2 = entity.getLong("downloadTimestamp2");
        long downloadTimestamp3 = entity.getLong("downloadTimestamp3");
        long downloadTimestamp4 = entity.getLong("downloadTimestamp4");
        long nbActiveDownloads = Stream.of(downloadTimestamp1, downloadTimestamp2, downloadTimestamp3, downloadTimestamp4)
            .filter(t -> t != 0L)
            .count();
        UserLevel rank = UserLevel.pointsToRank(entity.getLong("score"));
        if (! canDownload(rank,
            downloadTimestamp1,
            downloadTimestamp2,
            downloadTimestamp3,
            downloadTimestamp4)
        ) {
            resp.setStatus(403);
            resp.getWriter().println("You cannot have more than " + nbActiveDownloads + "download requests");
            return;
        } else {
            Long[] longs = addDownload(System.currentTimeMillis(), rank, downloadTimestamp1, downloadTimestamp2, downloadTimestamp3, downloadTimestamp4);
            Entity task = Entity.newBuilder(datastore.get(entity.getKey()))
                .set("downloadTimestamp1", longs[0])
                .set("downloadTimestamp2", longs[1])
                .set("downloadTimestamp3", longs[2])
                .set("downloadTimestamp4", longs[3])
                .build();
            datastore.update(task);

        }

        // Check if file exist
        GcsService fileService = GcsServiceFactory.createGcsService();
        GcsFilename file = new GcsFilename(bucketName, filename);
        if (file == null) {
            resp.setStatus(404);
            resp.getWriter().println("Error: file not found: " + filename);
            return;
        }

        resp.getWriter().println("User: " + entity.getString("name"));
        resp.getWriter().println("<br />");
        resp.getWriter().println("Score: " + entity.getDouble("score"));
        resp.getWriter().println("<br />");
        resp.getWriter().println("<br />");

        // Generate signedURL
        Storage storage = StorageOptions.getDefaultInstance().getService();
        InputStream is = new ByteArrayInputStream(jsonGcsKey.getBytes());

        URL signedUrl = storage.signUrl(com.google.cloud.storage.BlobInfo.newBuilder(bucketName, filename).build(),
            14,
            TimeUnit.SECONDS, SignUrlOption.signWith(ServiceAccountCredentials.fromStream(is)));

        // Add point to uploader
        Query <Entity> getFileInfoQuery = Query.newEntityQueryBuilder().setKind("upload").setFilter(PropertyFilter.eq("filename", filename)).build();
        QueryResults <Entity> resultsFileInfo = datastore.run(getFileInfoQuery);
        if (resultsFileInfo.hasNext()) {
            Entity entityFileInfo = resultsFileInfo.next();
            PathElement uploaderPE = entityFileInfo.getKey().getAncestors().get(0);
            resp.getWriter().println("Querying uploader id : " + uploaderPE.getId() + "<br/>");

            Key uploaderKey = datastore.newKeyFactory().setKind("user").newKey(uploaderPE.getId());
            Query <Entity> queryUploader = Query.newEntityQueryBuilder().setKind("user").setFilter(StructuredQuery.PropertyFilter.eq("__key__", uploaderKey)).build();
            QueryResults <Entity> uploaders = datastore.run(queryUploader);
            if (uploaders.hasNext()) {
                Entity uploader = uploaders.next();
                double score = uploader.getDouble("score");
                long filesize = entityFileInfo.getLong("size");

                score += (filesize / 10.0); // Uploader earns 0.1 / MB on each download
                Entity uploaderUpdated = Entity.newBuilder(datastore.get(uploader.getKey())).set("score", score).build();
                datastore.update(uploaderUpdated);
                resp.getWriter().println("Uploader :" + uploader.getString("name"));
                resp.getWriter().println("Score to add :" + score);
            }
        }

        //TODO: send mail instead of printing url in body?
        resp.getWriter().println("<a href='" + signedUrl + "'>" + signedUrl + "</a>");

    }

    private boolean canDownload(UserLevel level, long t1, long t2, long t3, long t4) {
        int timeout = 60000;
        if (level.equals(UserLevel.NOOB)) {
            return Stream.of(t1).allMatch(t -> System.currentTimeMillis() - t <= timeout);
        }
        if (level.equals(UserLevel.CASUAL)) {
            return Stream.of(t1, t2).allMatch(t -> System.currentTimeMillis() - t <= timeout);
        }
        if (level.equals(UserLevel.LEET)) {
            return Stream.of(t1, t2, t3, t4).allMatch(t -> System.currentTimeMillis() - t <= timeout);
        }
        return false; //TODO: handle this properly
    }

    private Long[] addDownload(long timestamp, UserLevel level, long t1, long t2, long t3, long t4) {
        if (level.equals(UserLevel.NOOB)) {
            return new Long[]{timestamp, t2, t3, t4};
        }
        if (level.equals(UserLevel.CASUAL)) {
            return t1 < t2 ? new Long[]{timestamp, t2, t3, t4} : new Long[]{t1, timestamp, t3, t4};
        }
        if (level.equals(UserLevel.LEET)) {
            List <Long> values = Arrays.asList(t1, t2, t3, t4);
            values.stream().sorted().min(Comparator.naturalOrder()).ifPresent(values::remove);
            values.add(timestamp);
            Long[] vals = new Long[values.size()];
            return values.toArray(vals);
        }
        return new Long[]{t1, t2, t3, t4};
    }
}
