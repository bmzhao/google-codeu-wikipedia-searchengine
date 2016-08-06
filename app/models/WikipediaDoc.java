package models;

/**
 * Created by brianzhao on 8/6/16.
 */
public class WikipediaDoc {
    private String title;
    private String url;
    private String highlight;

    public WikipediaDoc(String title, String url, String highlight) {
        this.title = title;
        this.url = url;
        this.highlight = highlight;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getHighlight() {
        return highlight;
    }

    @Override
    public String toString() {
        return "WikipediaDoc{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", highlight='" + highlight + '\'' +
                '}';
    }

    public boolean titleExists() {
        return title != null && !title.isEmpty();
    }
    public boolean urlExists() {
        return url != null && !url.isEmpty();
    }
    public boolean highlightExists() {
        return highlight != null && !highlight.isEmpty();
    }
}
