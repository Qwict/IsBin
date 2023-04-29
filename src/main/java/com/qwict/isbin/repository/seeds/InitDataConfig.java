package com.qwict.isbin.repository.seeds;

import com.qwict.isbin.model.*;
import com.qwict.isbin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        // create role set
        List<Role> adminRoles = new ArrayList<>();
        adminRoles.add(adminRole);
        adminRoles.add(userRole);
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(userRole);

//        User joris = new User("jorisduyse@qwict.com", "jorisduyse", adminRoles, 10, "b1hJZPuM4LvPcjovXVlnmIrdcwK0tsra6+0b+5wDA38lNRCMHx1doeVeXOS9NaAgtjH/HJ6t2cYt6tf3r8aebI0WJfzyftoRFCfkvg2VLS841DzEWLtO+NKaGvacaxhTmxMVv0sgmUYRsu8ck6skhpayyvl3Pf53ajirkfSxKIU=", "+C+tbWUa+PF8u7iWMDW4bj4Bh9nwDCcdGUeG4yEWDHG8GlURa/HvSwcudfzc7T+JhasQON1kLl+dkCI5fOBH3w==");
        User joris = new User("jorisduyse@qwict.com", "jorisduyse", 10, "$2a$10$3HXYsw8PiUpkawxmb1C95.mfBopUdX/HzGr4981390KkAlGStH8bS");

        // nineteenEight : 9781432839680 : George Orwell
        // Animal Farm : 9781986799270 : George Orwell
        // Brave New World : 9781407021010 : Aldous Huxley
        Author georgeOrwell = new Author("George", "Orwell");
        Author aldousHuxley = new Author("Aldous", "Huxley");

        Author sunTzu = new Author("Sun", "Tzu");
        Author axelBouts = new Author("Axel", "Bouts");
        Author jordanPeterson = new Author("Jordan", "Peterson");
        Author markManson = new Author("Mark", "Manson");
        Author aaronJames = new Author("Aaron", "James");

        Author robertCBartlett = new Author("Robert C.", "Bartlett");
        Author susanDCollins = new Author("Susan D.", "Collins");
        Author nicoDros = new Author("Nico", "Dros");
        Author stephenHawking = new Author("Stephen", "Hawking");

        Book nineteenEight = new Book("9781432839680", "1984", 9.99, new HashSet<>(List.of(georgeOrwell)), new HashSet<>(List.of(joris)));
        Book animalFarm = new Book("9781986799270", "Animal Farm", 9.99, new HashSet<>(List.of(georgeOrwell)), new HashSet<>(List.of(joris)));
        Book braveNewWorld = new Book("9781407021010", "Brave New World", 9.99, new HashSet<>(List.of(aldousHuxley)), new HashSet<>(List.of(joris)));
        Book aBriefHistoryOfTime = new Book("9780857501004", "A Brief History of Time", 9.99, new HashSet<>(List.of(stephenHawking)), new HashSet<>(List.of(joris)));
        Book willemDieMadocMaakte = new Book("9789028223035", "Willem die Madoc maakte", 27.50, new HashSet<>(List.of(nicoDros)), new HashSet<>(List.of(joris)));
        Book aristotlesNicomacheanEthics = new Book("9780226026756", "Aristotle's Nicomachean Ethics", 29.99, new HashSet<>(List.of(robertCBartlett, susanDCollins)), new HashSet<>(List.of(joris)));
        Book assHoles = new Book("9781857886108", "Assholes", 29.99, new HashSet<>(List.of(aaronJames)), new HashSet<>(List.of(joris)));
        Book theSubtleArt = new Book("9780062457714", "The Subtle Art of Not Giving a F*ck", 29.99, new HashSet<>(List.of(markManson)), new HashSet<>(List.of(joris)));
        Book artOfWar = new Book("9780140455526", "The Art of War", 29.99, new HashSet<>(List.of(sunTzu)), new HashSet<>(List.of(joris)));
        Book rulesForLife = new Book("9780345816023", "12 Rules for Life", 29.99, new HashSet<>(List.of(jordanPeterson)), new HashSet<>(List.of(joris)));
        Book daniel = new Book("9789063064662", "Daniël", 29.99, new HashSet<>(List.of(axelBouts)), new HashSet<>(List.of(joris)));

        Location location1 = new Location("Location 1", 50, 100, artOfWar);


        roleRepository.saveAll(List.of(userRole, adminRole));


        joris.setRoles(adminRoles);
        System.out.println(joris);
//        userRepository.save(joris);
//        userRepository.saveAll(List.of(joris));
        bookRepository.saveAll(
                List.of(
                        artOfWar, rulesForLife, daniel, theSubtleArt, assHoles,
                        aristotlesNicomacheanEthics, willemDieMadocMaakte,
                        nineteenEight, animalFarm, braveNewWorld, aBriefHistoryOfTime
                ));
        authorRepository.saveAll(
                List.of(
                        sunTzu, axelBouts, jordanPeterson, markManson, aaronJames,
                        robertCBartlett, susanDCollins, nicoDros
                ));

        locationRepository.save(location1);
//        locationRepository.saveAll(
//                List.of(
//                    location1
//                ));
    }
}
