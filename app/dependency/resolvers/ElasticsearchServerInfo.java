package dependency.resolvers;

/**
 * Created by brianzhao on 8/5/16.
 */
public class ElasticsearchServerInfo {
    private boolean https;
    private String host;
    private int port;
    private String user;
    private String password;

    public ElasticsearchServerInfo(boolean https, String host, int port, String user, String password) {
        this.https = https;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public boolean isHttps() {
        return https;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
