package com.qwict.isbin.repository.seeds;

import com.qwict.isbin.model.*;
import com.qwict.isbin.repository.AuthorRepository;
import com.qwict.isbin.repository.BookRepository;
import com.qwict.isbin.repository.LocationRepository;
import com.qwict.isbin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class InitDataConfig implements CommandLineRunner {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {
        User joris = new User("jorisduyse@qwict.com", "jorisduyse", "admin", 10, "b1hJZPuM4LvPcjovXVlnmIrdcwK0tsra6+0b+5wDA38lNRCMHx1doeVeXOS9NaAgtjH/HJ6t2cYt6tf3r8aebI0WJfzyftoRFCfkvg2VLS841DzEWLtO+NKaGvacaxhTmxMVv0sgmUYRsu8ck6skhpayyvl3Pf53ajirkfSxKIU=", "+C+tbWUa+PF8u7iWMDW4bj4Bh9nwDCcdGUeG4yEWDHG8GlURa/HvSwcudfzc7T+JhasQON1kLl+dkCI5fOBH3w==");
        Author sunTzu = new Author("Sun", "Tzu");
        Book artOfWar = new Book("9780140455526", "The Art of War", 29.99, new HashSet<>(List.of(sunTzu)), new HashSet<>(List.of(joris)));





        Location location1 = new Location("Location 1", 50, 100, artOfWar);

        userRepository.save(joris);
        bookRepository.save(artOfWar);
        authorRepository.save(sunTzu);
        locationRepository.save(location1);
    }
}
