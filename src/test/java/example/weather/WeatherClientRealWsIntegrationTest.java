package example.weather;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        locations = {"classpath:.env.test"},
        properties = {
                "weather.url=https://api.openweathermap.org",
                "weather.api_secret=${WEATHER_API_KEY}"})
@Ignore
public class WeatherClientRealWsIntegrationTest {
    // TODO This is just an example to show an IT with a real Service
    //  If you want to launch the test you need to
    // - create a .env.test file inside test/resources folder
    // - put the key/value pair WEATHER_API_KEY=your_key
    //   "your_key" is the same Weather service secret key used to connect to the service, see the README file

    @Autowired
    private WeatherClient subject;

    @Test
    public void shouldCallWeatherService() {
        var weatherResponse = subject.fetchWeather();

        var expectedResponse = Optional.of(new WeatherResponse("raining", "a light drizzle"));
        assertThat(weatherResponse, is(expectedResponse));
    }
}
