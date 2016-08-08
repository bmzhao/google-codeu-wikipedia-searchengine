package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import models.WikipediaDoc;
import org.apache.commons.lang3.StringEscapeUtils;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    @Inject
    JestClient jestClient;

    private final org.slf4j.Logger logger = Logger.of(HomeController.class).underlying();


    private static final String matchAllQuery = "{\n \"_source\": \"title\", \n \"query\": {\n \"match_all\": {}\n }\n}";

    private static final String multiMatchQuery =
                    "{\n"+
                    " \"from\": %s, \n"+
                    " \"_source\": \"title\", \n"+
                    " \"query\": { \n"+
                    " \"multi_match\" : {\n"+
                    " \"query\" : \"%s\",\n"+
                    " \"fields\" : [ \"title^3\", \"content^2\", \"url\" ] \n"+
                    " }\n"+
                    " },\n"+
                    " \"highlight\" : {\n"+
                    " \"fields\" : {\n"+
                    " \"content\" : {}\n"+
                    " }\n"+
                    " }\n"+
                    "}";

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("CodeU Wiki Search"));
    }


    public Result search() {
        String from = request().getQueryString("from");
        try {
            int actualFrom = Integer.parseInt(from);
        } catch (NumberFormatException | NullPointerException e) {
            from = "0";
        }

        String user_query = request().getQueryString("query");
        user_query = StringEscapeUtils.escapeJava(user_query);

        String searchQuery = null;

        //if query is null perform match all query
        if (user_query == null) {
            searchQuery = matchAllQuery;
        } else {
            searchQuery = String.format(multiMatchQuery, from, user_query);
        }

        Search search = new Search.Builder(searchQuery)
                // multiple index or types can be added.
                .addIndex("wikipedia")
                .addType("doc")
                .build();

        try {
            SearchResult searchResult = jestClient.execute(search);

            int totalResults = searchResult.getTotal();

            JsonObject result = searchResult.getJsonObject();
            List<WikipediaDoc> docs = new ArrayList<>();

            JsonArray current_hits = result.getAsJsonObject("hits").getAsJsonArray("hits");
            for (JsonElement jsonElement : current_hits) {
                JsonObject hit = jsonElement.getAsJsonObject();

                String url = hit.get("_id").getAsString();
                String title = null;
                String highlight = null;

                JsonObject source = hit.get("_source").getAsJsonObject();
                if (source.has("title")) {
                    title = source.get("title").getAsString();
                }

                if (hit.has("highlight")) {
                    JsonObject highlightJson = hit.get("highlight").getAsJsonObject();
                    if (highlightJson.has("content")) {
                        JsonArray highlightContentJson = highlightJson.getAsJsonArray("content");
                        StringBuilder highlightedMarkup = new StringBuilder();
                        for (JsonElement element : highlightContentJson) {
                            highlightedMarkup.append(element.getAsString()).append("...").append('\n');
                        }
                        highlight = highlightedMarkup.toString();
                    }
                }
                WikipediaDoc wikipediaDoc = new WikipediaDoc(title, url, highlight);
                docs.add(wikipediaDoc);
            }

            return ok(views.html.search.render(docs));
        } catch (IOException e) {
            e.printStackTrace();
            return internalServerError();
        }
    }

}
