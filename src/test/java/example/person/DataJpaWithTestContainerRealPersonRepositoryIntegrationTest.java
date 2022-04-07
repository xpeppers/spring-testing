package example.person;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://127.0.0.1:15432/postgres",
        "spring.datasource.username=testuser",
        "spring.datasource.password=password",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@RunWith(SpringRunner.class)
public class DataJpaWithTestContainerRealPersonRepositoryIntegrationTest {
    @Rule
    public PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres")
            .withUsername("testuser")
            .withPassword("password")
            .withDatabaseName("postgres");

    @Autowired
    private PersonRepository subject;

    @After
    public void tearDown() throws Exception {
        subject.deleteAll();
    }

    @Test
    public void shouldSaveAndFetchPerson() throws Exception {
        var peter = new Person("Peter", "Pan");
        subject.save(peter);

        var maybePeter = subject.findByLastName("Pan");

        assertThat(maybePeter).isEqualTo(Optional.of(peter));
    }
}
