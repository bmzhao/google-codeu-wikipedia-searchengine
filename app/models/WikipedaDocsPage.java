package models;

import java.util.List;

/**
 * Created by brianzhao on 8/9/16.
 */
public class WikipedaDocsPage {
    private final List<WikipediaDoc> docs;
    private final int from;
    private final int currentPageNumber;
    private final int maxPossiblePageNumber;
    private int startPageNumber;
    private int lastPageNumber;
    private int totalResults;
    private String displayingHTML;
    private String leftDisabled;
    private String rightDisabled;
    private String paginationHTML;
    private String query;

    // from: 0-based index of the search results
    public WikipedaDocsPage(List<WikipediaDoc> docs, int from, int totalResults, String query) {
        this.docs = docs;
        this.from = from;
        this.query = query;
        this.totalResults = totalResults;
        this.maxPossiblePageNumber = totalResults / 10 + 1;
        this.currentPageNumber = from / 10 + 1;
        this.startPageNumber = ((currentPageNumber - 4) <= 1) ? 1 : currentPageNumber - 4;
        this.lastPageNumber = ((currentPageNumber + 4) > maxPossiblePageNumber) ? maxPossiblePageNumber : currentPageNumber + 4;

        StringBuilder displayingHTMLStringBuilder = new StringBuilder();
        displayingHTMLStringBuilder.append("<div class=\"displayHTML\">");
        displayingHTMLStringBuilder.append("Displaying ");
        displayingHTMLStringBuilder.append(from + 1);
        displayingHTMLStringBuilder.append("-");
        displayingHTMLStringBuilder.append(Math.min(from + 10, totalResults));
        displayingHTMLStringBuilder.append(" of ");
        displayingHTMLStringBuilder.append(totalResults);
        displayingHTMLStringBuilder.append(" results</div>");
        displayingHTML = displayingHTMLStringBuilder.toString();

        // !!!!!!!!!

        if (currentPageNumber == 1) {
            leftDisabled = "disabled";
        } else {
            leftDisabled = "waves-effect";
        }

        if (currentPageNumber == lastPageNumber) {
            rightDisabled = "disabled";
        } else {
            rightDisabled = "waves-effect";
        }

        StringBuilder pagination = new StringBuilder();
        // pagination.append("<center>");
        pagination.append("<div id=\"pageNumber\">");
        pagination.append("<ul class=\"paging\">").append('\n');
        pagination.append(" <li class=\"").append(leftDisabled)
                .append("\"><a href=\"")
                .append("/search?from=");
        int previousFrom = (currentPageNumber - 2) * 10;
        pagination.append(previousFrom);
        pagination.append("&query=")
                .append(query)
                .append("\"><i class=\"material-icons\">chevron_left</i></a></li>");
        for (int i = startPageNumber; i <= lastPageNumber; i++) {
            pagination.append("<li class=\"");
            if (currentPageNumber == i) {
                pagination.append("active");
            } else {
                pagination.append("waves-effect");
            }
            pagination.append("\"><a href=\"");
            pagination.append("/search?from=");
            int newFrom = (i - 1) * 10;
            pagination.append(newFrom);
            pagination.append("&query=");
            pagination.append(query);
            pagination.append("\">");
            pagination.append(i);
            pagination.append("</a></li>");
        }
        pagination.append("<li class=\"").append(rightDisabled).append("\"><a href=\"")
                .append("/search?from=");

        int afterFrom = (currentPageNumber) * 10;
        pagination.append(afterFrom);
        pagination.append("&query=").append(query)
                .append("\"><i class=\"material-icons\">chevron_right</i></a></li>\n</ul></div>\n");
        // pagination.append("</center>");
        this.paginationHTML = pagination.toString();
    }

    public List<WikipediaDoc> getDocs() {
        return docs;
    }

    public String getDisplayingHTML() {
        return displayingHTML;
    }

    public String getQuery() {
        return query;
    }

    public String getPaginationHTML() {
        return paginationHTML;
    }
}
