package fr.unice.polytech.si5.cc.l5;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.SignUrlOption;
import com.google.cloud.storage.StorageOptions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

// Stuff for signed URL
// Stuff to check file exist
// Stuff to authenticate user

@WebServlet(name = "Deleter", value = "/delete")
public class Deleter extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = req.getParameter("file");
        String bucketName = req.getParameter("bucket");
        if (filename == null || bucketName == null) {
            resp.setStatus(400);
            resp.getWriter().println("Error: missing file, username or bucket");
			return;
        }

		// Check if file exist
        GcsService fileService = GcsServiceFactory.createGcsService();
        GcsFilename file = new GcsFilename(bucketName, filename);
        boolean success = fileService.delete(file);
        if (!success) {
            resp.setStatus(400);
            resp.getWriter().println("Error: could not delete file " + filename);
            return;
        }
    }
}
