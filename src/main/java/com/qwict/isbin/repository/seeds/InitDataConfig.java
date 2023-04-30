package com.qwict.isbin.repository.seeds;

import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.*;
import com.qwict.isbin.repository.*;
import com.qwict.isbin.service.UserService;
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
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {
        System.out.printf("ADDING DATA TO DATABASE...%n");
        createBooks();
        createAuthors();

        // create role objects -> adds roles to role lists -> add roles to database
        addRolesToDatabase();

        // create userDto objects -> create users with roles from dto's -> add users to database
//        Somhow this creates detached entity passed to persist errors
//        addUsersToDatabase();

//        User joris = new User("jorisduyse@qwict.com", "jorisduyse", adminRoles, 10, "b1hJZPuM4LvPcjovXVlnmIrdcwK0tsra6+0b+5wDA38lNRCMHx1doeVeXOS9NaAgtjH/HJ6t2cYt6tf3r8aebI0WJfzyftoRFCfkvg2VLS841DzEWLtO+NKaGvacaxhTmxMVv0sgmUYRsu8ck6skhpayyvl3Pf53ajirkfSxKIU=", "+C+tbWUa+PF8u7iWMDW4bj4Bh9nwDCcdGUeG4yEWDHG8GlURa/HvSwcudfzc7T+JhasQON1kLl+dkCI5fOBH3w==");

    }

    private void addRolesToDatabase() {
        // create role object
        Role ownerRole = new Role("ROLE_OWNER");
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        // create role lists
//        ownerRoles.addAll(List.of(ownerRole, adminRole, userRole));
//        adminRoles.addAll(List.of(adminRole, userRole));
//        userRoles.add(userRole);

        // save roles to db
        roleRepository.saveAll(List.of(ownerRole, adminRole, userRole));
    }

    private void addUsersToDatabase() {
//        List<Role> ownerRoles = new ArrayList<>();
//        ownerRoles.addAll(roleRepository.findAll());
//        List<Role> adminRoles = new ArrayList<>();
//        adminRoles.addAll(List.of(roleRepository.findByName("ROLE_ADMIN"), roleRepository.findByName("ROLE_USER")));
//        List<Role> userRoles = new ArrayList<>();
//        userRoles.add(roleRepository.findByName("ROLE_USER"));

        User user = new User("user", "user@qwict.com", 10, "user@qwict.com");
//                , new ArrayList<>(List.of(roleRepository.findByName("ROLE_USER"))));
        userRepository.save(user);
        User admin = new User("admin", "admin@qwict.com", 20, "admin@qwict.com");
//                new ArrayList<>(
//                        List.of(
//                                roleRepository.findByName("ROLE_USER"),
//                                roleRepository.findByName("ROLE_ADMIN")
//                        )));
        userRepository.save(admin);
        User owner = new User("owner", "owner@qwict.com", 200, "owner@qwict.com");
//                new ArrayList<>(
//                        List.of(
//                                roleRepository.findByName("ROLE_USER"),
//                                roleRepository.findByName("ROLE_ADMIN"),
//                                roleRepository.findByName("ROLE_OWNER")
//                        )));
        userRepository.save(owner);

    }

    private void createBooks() {
        Book nineteenEight = new Book("9781432839680", "1984", 9.99);
        Book braveNewWorld = new Book("9781407021010", "Brave New World", 9.99);
        Book artOfWar = new Book("9780140455526", "The Art of War", 29.99);
        Book animalFarm = new Book("9781986799270", "Animal Farm", 9.99);
        Book aBriefHistoryOfTime = new Book("9780857501004", "A Brief History of Time", 9.99);
        Book willemDieMadocMaakte = new Book("9789028223035", "Willem die Madoc maakte", 27.50);
        Book aristotlesNicomacheanEthics = new Book("9780226026756", "Aristotle's Nicomachean Ethics", 29.99);
        Book assHoles = new Book("9781857886108", "Assholes", 29.99);
        Book theSubtleArt = new Book("9780062457714", "The Subtle Art of Not Giving a F*ck", 29.99);
        Book rulesForLife = new Book("9780345816023", "12 Rules for Life", 29.99);
        Book daniel = new Book("9789063064662", "Daniël", 29.99);
        Book oneHundred = new Book("9780241968581", "One Hundred Years of Solitude", 29.99);
        bookRepository.saveAll(List.of(
                nineteenEight, braveNewWorld, artOfWar, animalFarm, aBriefHistoryOfTime, willemDieMadocMaakte,
                aristotlesNicomacheanEthics, assHoles, theSubtleArt, rulesForLife, daniel, oneHundred
        ));
    }

    private void createAuthors() {
        Author sunTzu = new Author("Sun", "Tzu");
        Author aldousHuxley = new Author("Aldous", "Huxley");
        Author georgeOrwell = new Author("George", "Orwell");
        Author axelBouts = new Author("Axel", "Bouts");
        Author jordanPeterson = new Author("Jordan", "Peterson");
        Author markManson = new Author("Mark", "Manson");
        Author aaronJames = new Author("Aaron", "James");
        Author robertCBartlett = new Author("Robert C.", "Bartlett");
        Author susanDCollins = new Author("Susan D.", "Collins");
        Author nicoDros = new Author("Nico", "Dros");
        Author stephenHawking = new Author("Stephen", "Hawking");
        Author gabrielGarciaMarquez = new Author("Gabriel", "García Márquez");
        authorRepository.saveAll(List.of(
                sunTzu, aldousHuxley, georgeOrwell, axelBouts, jordanPeterson, markManson, aaronJames,
                robertCBartlett, susanDCollins, nicoDros, stephenHawking, gabrielGarciaMarquez
        ));
    }

    private void oldcreate() {
        User joris = new User("jorisduyse@qwict.com", "jorisduyse", 10, "$2a$10$3HXYsw8PiUpkawxmb1C95.mfBopUdX/HzGr4981390KkAlGStH8bS");



        // nineteenEight : 9781432839680 : George Orwell
        // Animal Farm : 9781986799270 : George Orwell
        // Brave New World : 9781407021010 : Aldous Huxley
        Author aldousHuxley = new Author("Aldous", "Huxley");
        Author georgeOrwell = new Author("George", "Orwell");

        Author sunTzu = new Author("Sun", "Tzu");
        Author axelBouts = new Author("Axel", "Bouts");
        Author jordanPeterson = new Author("Jordan", "Peterson");
        Author markManson = new Author("Mark", "Manson");
        Author aaronJames = new Author("Aaron", "James");

        Author robertCBartlett = new Author("Robert C.", "Bartlett");
        Author susanDCollins = new Author("Susan D.", "Collins");
        Author nicoDros = new Author("Nico", "Dros");
        Author stephenHawking = new Author("Stephen", "Hawking");

        Book nineteenEight = new Book("9781432839680", "1984", 9.99, new ArrayList<>(List.of(georgeOrwell)), new ArrayList<>(List.of(joris)));
        Book animalFarm = new Book("9781986799270", "Animal Farm", 9.99, new ArrayList<>(List.of(georgeOrwell)), new ArrayList<>(List.of(joris)));
        Book braveNewWorld = new Book("9781407021010", "Brave New World", 9.99, new ArrayList<>(List.of(aldousHuxley)), new ArrayList<>(List.of(joris)));
        Book aBriefHistoryOfTime = new Book("9780857501004", "A Brief History of Time", 9.99, new ArrayList<>(List.of(stephenHawking)), new ArrayList<>(List.of(joris)));
        Book willemDieMadocMaakte = new Book("9789028223035", "Willem die Madoc maakte", 27.50, new ArrayList<>(List.of(nicoDros)), new ArrayList<>(List.of(joris)));
        Book aristotlesNicomacheanEthics = new Book("9780226026756", "Aristotle's Nicomachean Ethics", 29.99, new ArrayList<>(List.of(robertCBartlett, susanDCollins)), new ArrayList<>(List.of(joris)));
        Book assHoles = new Book("9781857886108", "Assholes", 29.99, new ArrayList<>(List.of(aaronJames)), new ArrayList<>(List.of(joris)));
        Book theSubtleArt = new Book("9780062457714", "The Subtle Art of Not Giving a F*ck", 29.99, new ArrayList<>(List.of(markManson)), new ArrayList<>(List.of(joris)));
        Book artOfWar = new Book("9780140455526", "The Art of War", 29.99, new ArrayList<>(List.of(sunTzu)), new ArrayList<>(List.of(joris)));
        Book rulesForLife = new Book("9780345816023", "12 Rules for Life", 29.99, new ArrayList<>(List.of(jordanPeterson)), new ArrayList<>(List.of(joris)));
        Book daniel = new Book("9789063064662", "Daniël", 29.99, new ArrayList<>(List.of(axelBouts)), new ArrayList<>(List.of(joris)));
//
//        createBooks();
        Location location1 = new Location("Location 1", 50, 100, artOfWar);

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
        locationRepository.saveAll(
                List.of(
                        location1
                ));
    }
}
