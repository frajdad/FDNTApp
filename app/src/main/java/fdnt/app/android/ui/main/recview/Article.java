package fdnt.app.android.ui.main.recview;

public class Article {
    protected String header;
    protected String content;
    protected boolean expanded = false;
    public Article(String header, String content) {
        this.header = header;
        this.content = content;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isExpanded() { return expanded; }
    public void setExpanded(boolean expanded) { this.expanded = expanded; }
}
