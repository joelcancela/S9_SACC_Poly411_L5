package fr.unice.polytech.si5.cc.l5;

import com.google.cloud.datastore.*;
import com.google.gson.GsonBuilder;
import fr.unice.polytech.si5.cc.l5.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Class UserServlet
 *
 * @author Joël CANCELA VAZ
 */
@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        BufferedReader reader = req.getReader();
        User userInput = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(reader, User.class);
        IncompleteKey key = datastore.newKeyFactory().setKind("user").newKey();
        FullEntity <IncompleteKey> aNewUser = FullEntity.newBuilder(key)
            .set("name", userInput.getName())
            .set("score", userInput.getScore())
            .set("email", userInput.getEmail())
            .set("downloadTimestamp1", userInput.getDownloadTimestamp1())
            .set("downloadTimestamp2", userInput.getDownloadTimestamp2())
            .set("downloadTimestamp3", userInput.getDownloadTimestamp3())
            .set("downloadTimestamp4", userInput.getDownloadTimestamp4())
            .build();
        datastore.add(aNewUser);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO: delete everything, add other entities
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query <Entity> userQuery = Query.newEntityQueryBuilder().setKind("user").build();
        QueryResults <Entity> results = datastore.run(userQuery);
        while (results.hasNext()) {
            datastore.delete(results.next().getKey());
        }
    }
}
