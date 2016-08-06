package dependency.resolvers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by brianzhao on 8/5/16.
 */
public class CustomJestProvider implements Provider<JestClient> {
    private final JestClientFactory jestClientFactory;
    private final ElasticsearchServerInfo elasticsearchServerInfo;

    @Inject
    public CustomJestProvider(JestClientFactory jestClientFactory, ElasticsearchServerInfo elasticsearchServerInfo) {
        this.jestClientFactory = jestClientFactory;
        this.elasticsearchServerInfo = elasticsearchServerInfo;
    }

    @Override
    public JestClient get() {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial((chain, authType) -> true).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }

        // skip hostname checks
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext);
        SchemeIOSessionStrategy httpsIOSessionStrategy = new SSLIOSessionStrategy(sslContext, hostnameVerifier);

        URL urlOfElasticsearchNode = null;
        try {
            urlOfElasticsearchNode = new URL(elasticsearchServerInfo.isHttps() ? "https" : "http", elasticsearchServerInfo.getHost()
                    , elasticsearchServerInfo.getPort(), "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        if (elasticsearchServerInfo.getHost() != null && elasticsearchServerInfo.getPort() > 1) {
            HttpClientConfig.Builder builder = new HttpClientConfig.Builder(urlOfElasticsearchNode.toString())
                    .multiThreaded(true)
                    .connTimeout(300000)
                    .readTimeout(300000);
            if (elasticsearchServerInfo.isHttps()) {
                if (elasticsearchServerInfo.getUser() != null && elasticsearchServerInfo.getPassword() != null) {
                    builder.defaultCredentials(elasticsearchServerInfo.getUser(), elasticsearchServerInfo.getPassword());
                }
                builder.defaultSchemeForDiscoveredNodes("https")
                        .sslSocketFactory(sslSocketFactory) // this only affects sync calls
                        .httpsIOSessionStrategy(httpsIOSessionStrategy); // this only affects async calls
            }
            jestClientFactory.setHttpClientConfig(builder.build());
        } else {
            throw new RuntimeException("No host and port specified");
        }

        return jestClientFactory.getObject();
    }
}
