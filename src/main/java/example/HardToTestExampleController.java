package example;

import example.person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HardToTestExampleController {
    // This class uses the @Autowired annotation to inject the PersonRepository dependency.
    // If we want to test this class, we need to load a Spring Application Context within our test class
    @Autowired
    private PersonRepository personRepository;

    // A different mapping route from the ExampleController
    // If the route was the same, Spring would not be able to map the two methods (which one should be invoked if both
    // have the same GET route?)
    @GetMapping("/hello_2/{lastName}")
    public String hello(@PathVariable final String lastName) {
        var foundPerson = personRepository.findByLastName(lastName);

        return foundPerson
                .map(person -> String.format("Hello %s %s!", person.getFirstName(), person.getLastName()))
                .orElse(String.format("Who is this '%s' you're talking about?", lastName));
    }
}
