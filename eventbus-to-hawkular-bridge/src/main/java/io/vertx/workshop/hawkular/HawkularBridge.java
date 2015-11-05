package io.vertx.workshop.hawkular;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;
import org.hawkular.metrics.client.common.Batcher;
import org.hawkular.metrics.client.common.SingleMetric;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HawkularBridge extends AbstractVerticle {

  private HttpClient httpClient;

  private static final String URL = "/hawkular/metrics/gauges/data";
  private static final Logger LOGGER = Logger.getLogger(HawkularBridge.class.getName());

  public static void main(String[] args) {
    new Launcher().execute("run", HawkularBridge.class.getName());
  }

  /**
   * If your verticle does a simple, synchronous start-up then override this method and put your start-up
   * code in there.
   *
   * @throws Exception
   */
  @Override
  public void start() throws Exception {
    httpClient = vertx.createHttpClient(new HttpClientOptions()
        .setDefaultPort(config().getInteger("hawkular.port", 8090))
        .setDefaultHost(config().getString("hawkular.host", "192.168.99.100")));

    vertx.eventBus().consumer("metrics", msg -> {
      JsonObject json = (JsonObject) msg.body();
      String source = json.getString("source");
      double value = json.getDouble("value");
      SingleMetric simple = new SingleMetric(source, System.currentTimeMillis(), value);
      send(Collections.singletonList(simple));
    });
  }

  private void send(List<SingleMetric> metrics) {
    String json = Batcher.metricListToJson(metrics);
    Buffer buffer = Buffer.buffer(json);
    HttpClientRequest req = httpClient.post(
        URL,
        response -> {
          LOGGER.info("Metric sent " + response.statusCode() + " / " + metrics.get(0).toJson());
          if (response.statusCode() != 200) {
            response.bodyHandler(msg -> LOGGER.warning("Could not send metrics: " + response.statusCode() + " : "
                + msg.toString()));
          }
        });
    req.putHeader("Content-Length", String.valueOf(buffer.length()));
    req.putHeader("Content-Type", "application/json");
    req.putHeader("Hawkular-Tenant", "default");
    req.exceptionHandler(err -> LOGGER.severe("Could not send metrics " + err));
    req.write(buffer);
    req.end();
  }
}
