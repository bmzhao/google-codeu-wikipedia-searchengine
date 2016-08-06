package dependency.resolvers;

import com.google.inject.Provider;
import com.typesafe.config.ConfigFactory;

/**
 * Created by brianzhao on 8/5/16.
 */
public class ElasticConfigParser implements Provider<ElasticsearchServerInfo> {
    private final ElasticsearchServerInfo elasticsearchServerInfo;

    public ElasticConfigParser() {
        String host = ConfigFactory.load().getString("elastic_host");
        int port = ConfigFactory.load().getInt("elastic_port");
        boolean https = ConfigFactory.load().getBoolean("elastic_https");
        String user = ConfigFactory.load().getString("elastic_user");
        String password = ConfigFactory.load().getString("elastic_password");
        this.elasticsearchServerInfo = new ElasticsearchServerInfo(https, host, port, user, password);
    }

    @Override
    public ElasticsearchServerInfo get() {
        return elasticsearchServerInfo;
    }
}
