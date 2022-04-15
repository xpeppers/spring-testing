package example.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class WeatherClient {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String CITY = "Hamburg,de";
    private final RestTemplate restTemplate;
    private final String weatherServiceUrl;
    private final String weatherServiceApiKey;

    public WeatherClient(final RestTemplate restTemplate,
                         @Value("${weather.url}") final String weatherServiceUrl,
                         @Value("${weather.api_secret}") final String weatherServiceApiKey) {
        this.restTemplate = restTemplate;
        this.weatherServiceUrl = weatherServiceUrl;
        this.weatherServiceApiKey = weatherServiceApiKey;
    }

    public Optional<WeatherResponse> fetchWeather() {
        var url = String.format("%s/data/2.5/weather?q=%s&appid=%s", weatherServiceUrl, CITY, weatherServiceApiKey);

        try {
            return Optional.ofNullable(restTemplate.getForObject(url, WeatherResponse.class));
        } catch (RestClientException e) {
            logger.error("Couldn't invoke Weather service", e);
            return Optional.empty();
        }
    }
}
