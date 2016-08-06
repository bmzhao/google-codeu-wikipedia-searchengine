package dependency.resolvers;

import com.google.inject.Provider;

/**
 * Created by brianzhao on 8/5/16.
 */
public class ElasticEnvironmentParser implements Provider<ElasticsearchServerInfo>{
    private final ElasticsearchServerInfo elasticsearchServerInfo;

    public ElasticEnvironmentParser() {
        String host = System.getenv("elastic_host");
        int port = Integer.valueOf(System.getenv("elastic_port"));
        boolean https = Boolean.valueOf(System.getenv("elastic_https"));
        String user = System.getenv("elastic_user");
        String password = System.getenv("elastic_password");
        this.elasticsearchServerInfo = new ElasticsearchServerInfo(https, host, port, user, password);
    }

    @Override
    public ElasticsearchServerInfo get() {
        return elasticsearchServerInfo;
    }
}
