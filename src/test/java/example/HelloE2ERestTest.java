package example;

import example.person.Person;
import example.person.PersonRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HelloE2ERestTest {

    @Autowired
    private PersonRepository personRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @After
    public void tearDown() throws Exception {
        personRepository.deleteAll();
    }

    @Test
    public void shouldReturnHelloWorld() throws Exception {
        when()
                .get(String.format("http://localhost:%s/hello", port))
                .then()
                .statusCode(is(200))
                .body(containsString("Hello World!"));
    }

    @Test
    public void shouldReturnGreeting() throws Exception {
        var peter = new Person("Peter", "Pan");
        personRepository.save(peter);

        when()
                .get(String.format("http://localhost:%s/hello/Pan", port))
                .then()
                .statusCode(is(200))
                .body(containsString("Hello Peter Pan!"));
    }

    @Test
    public void shouldReturnGreetingWhenUsingMockMvc() throws Exception {
        var hook = new Person("Jack", "Hook");
        personRepository.save(hook);

        mockMvc.perform(MockMvcRequestBuilders.get("/hello/Hook"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("Hello Jack Hook!"));
    }

    @Test
    public void shouldReturnGreetingWHenUsingRestTemplate() {
        var mrSmee = new Person("Mr.", "Smee");
        personRepository.save(mrSmee);

        String response = restTemplate.getForObject(String.format("http://localhost:%s/hello/Smee", port), String.class);

        assertThat(response).isEqualTo("Hello Mr. Smee!");
    }
}
