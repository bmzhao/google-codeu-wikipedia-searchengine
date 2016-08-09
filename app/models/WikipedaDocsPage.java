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
    private String leftDisabled;
    private String rightDisabled;
    private String paginationHTML;

    public WikipedaDocsPage(List<WikipediaDoc> docs, int from, int totalResults, String query) {
        this.docs = docs;
        this.from = from;
        this.totalResults = totalResults;
        this.maxPossiblePageNumber = totalResults / 10 + 1;
        this.currentPageNumber = from / 10 + 1;
        this.startPageNumber = ((currentPageNumber - 5) < 1) ? 1 : currentPageNumber - 5;
        this.lastPageNumber = ((currentPageNumber + 5) > maxPossiblePageNumber) ? maxPossiblePageNumber : currentPageNumber + 5;


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
        pagination.append("<ul class=\"pagination\">").append('\n');
        pagination.append(" <li class=\"").append(leftDisabled)
                .append("\"><a href=\"")
                .append("/search?from=");
        int previousFrom = (currentPageNumber - 2) * 10;
        pagination.append(previousFrom);
        pagination.append("&query=")
                .append(query)
                .append("\"><i class=\"material-icons\">chevron_left</i></a></li>");
        for (int i = startPageNumber; i < lastPageNumber; i++) {
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
                .append("\"><i class=\"material-icons\">chevron_right</i></a></li>\n</ul>\n");
        this.paginationHTML = pagination.toString();

    }

    public List<WikipediaDoc> getDocs() {
        return docs;
    }

    public String getPaginationHTML() {
        return paginationHTML;
    }
}