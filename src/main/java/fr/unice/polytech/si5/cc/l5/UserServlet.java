package fr.unice.polytech.si5.cc.l5;

import com.google.cloud.datastore.*;
import com.google.gson.GsonBuilder;
import fr.unice.polytech.si5.cc.l5.model.User;
import fr.unice.polytech.si5.cc.l5.model.UserLevel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class UserServlet
 *
 * @author Joël CANCELA VAZ
 */
@WebServlet(name = "UserServlet", value = "/users")
public class UserServlet extends HttpServlet {

	//Scoreboard TODO: orderby score
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("user").build();
		QueryResults<Entity> results = datastore.run(query);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html><body><h1>Scoreboard</h1><table>");
		out.print("<tr>\n" +
				"    <th>Username</th>\n" +
				"    <th>Points</th>\n" +
				"    <th>Rank</th>\n" +
				"  </tr>");
		while (results.hasNext()) {
			Entity entity = results.next();
			out.format("<tr>\n" +
							"    <th>%s</th>\n" +
							"    <th>%s</th>\n" +
							"    <th>%s</th>\n" +
							"  </tr>", entity.getString("name"), entity.getDouble("score"),
					UserLevel.pointsToRank(entity.getDouble("score")));
		}
		out.println("</table></body></html>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		BufferedReader reader = req.getReader();
		User userInput = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(reader, User.class);
		IncompleteKey key = datastore.newKeyFactory().setKind("user").newKey();
		FullEntity<IncompleteKey> aNewUser = FullEntity.newBuilder(key)
				.set("name", userInput.getName())
				.set("score", 0.0)
                .set("downloadTimestamp1", 0)
                .set("downloadTimestamp2", 0)
                .set("downloadTimestamp3", 0)
                .set("downloadTimestamp4", 0)
				.set("email", userInput.getEmail()).build();
		datastore.add(aNewUser);
	}
}
