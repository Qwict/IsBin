package com.qwict.isbin.repository.seeds;

import com.qwict.isbin.IsBinApplication;
import com.qwict.isbin.model.*;
import com.qwict.isbin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

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
        if (Objects.equals(IsBinApplication.getEnv(), "development")) {
            System.out.println("INFO -- InitDataConfig -- ADDING DATA TO DATABASE...");
            addRolesToDatabase();
            addUsersToDatabase();
            addBooksToDatabase();
            addAuthorsToDatabase();
            addLocationsWithBooksToDatabase();

            updateAuthorsWithBooks();
            updateUsersWithRoles();
            updateUsersWithBooks();
        } else {
            System.out.println("INFO -- InitDataConfig -- SKIPPING ADDING DATA TO DATABASE...");
        }
    }

    private void addRolesToDatabase() {
        Role ownerRole = new Role("ROLE_OWNER");
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        roleRepository.saveAll(List.of(ownerRole, adminRole, userRole));
    }

    private void addUsersToDatabase() {
        User user = new User("user@qwict.com", "user", 10, "$2a$10$B4JZqTyidIeIk7uPbduuVONnNaas4eALngVXAcgVSKPwoKsBAKLry");
        user.setRoles(List.of());
        user.setBooks(List.of());
        userRepository.save(user);

        User admin = new User("admin@qwict.com", "admin", 20, "$2a$10$UvfQHnnZi/roFoYybcVia.h.hIOp0E3Jz.o/YQ4vMbm7k/DYzyoki");
        admin.setRoles(List.of());
        admin.setBooks(List.of());
        userRepository.save(admin);

        User owner = new User("owner@qwict.com", "owner", 200, "$2a$10$XUUteoPTj.cK8ntEN92Hf.eGnKiT9AjLAK6uGY.fr6.k89Y8mHKXS");
        owner.setRoles(List.of());
        owner.setBooks(List.of());
        userRepository.save(owner);
    }

    private void addBooksToDatabase() {
        // sunTzu
        Book artOfWar = new Book("9780140455526", "The Art of War", 29.99);
        // george orwell
        Book nineteenEight = new Book("9781432839680", "1984", 9.99);
        Book animalFarm = new Book("9781986799270", "Animal Farm", 9.99);
        // aldous huxley
        Book braveNewWorld = new Book("9781407021010", "Brave New World", 9.99);
        // stephen hawking
        Book aBriefHistoryOfTime = new Book("9780857501004", "A Brief History of Time", 9.99);
        // nico dros
        Book willemDieMadocMaakte = new Book("9789028223035", "Willem die Madoc maakte", 27.50);
        // Susan D Collinst
        // Robert C. Bartlett
        Book aristotlesNicomacheanEthics = new Book("9780226026756", "Aristotle's Nicomachean Ethics", 29.99);
        // aaron james
        Book assHoles = new Book("9781857886108", "Assholes", 29.99);
        // mark manson
        Book theSubtleArt = new Book("9780062457714", "The Subtle Art of Not Giving a F*ck", 29.99);
        // jordan peterson
        Book rulesForLife = new Book("9780345816023", "12 Rules for Life", 29.99);
        // axel bouts
        Book daniel = new Book("9789063064662", "Daniël", 29.99);
        // gabriel garcia marquez
        Book oneHundred = new Book("9780241968581", "One Hundred Years of Solitude", 29.99);
        // salman rushdie
        Book shame = new Book("9780394534084", "Shame", 42.42);

        // add tolkin books to database
        bookRepository.saveAll(List.of(
                nineteenEight, braveNewWorld, artOfWar, animalFarm, aBriefHistoryOfTime, willemDieMadocMaakte,
                aristotlesNicomacheanEthics, assHoles, theSubtleArt, rulesForLife, daniel, oneHundred, shame
        ));
    }

    private void addAuthorsToDatabase() {
        Author sunTzu = new Author("Sun", "Tzu");
        Author georgeOrwell = new Author("George", "Orwell");
        Author aldousHuxley = new Author("Aldous", "Huxley");
        Author axelBouts = new Author("Axel", "Bouts");
        Author jordanPeterson = new Author("Jordan", "Peterson");
        Author markManson = new Author("Mark", "Manson");
        Author aaronJames = new Author("Aaron", "James");
        Author robertCBartlett = new Author("Robert C.", "Bartlett");
        Author susanDCollins = new Author("Susan D.", "Collins");
        Author nicoDros = new Author("Nico", "Dros");
        Author stephenHawking = new Author("Stephen", "Hawking");
        Author gabrielGarciaMarquez = new Author("Gabriel", "García Márquez");
        Author salmanRushdie = new Author("Salman", "Rushdie");
        authorRepository.saveAll(List.of(
                sunTzu, aldousHuxley, georgeOrwell, axelBouts, jordanPeterson, markManson, aaronJames,
                robertCBartlett, susanDCollins, nicoDros, stephenHawking, gabrielGarciaMarquez, salmanRushdie
        ));
    }

    private void addLocationsWithBooksToDatabase() {
        Book artOfWar = bookRepository.findByIsbn("9780140455526");
        Book nineteenEight = bookRepository.findByIsbn("9781432839680");

        locationRepository.save(new Location("EA",50, 100, artOfWar));
        locationRepository.save(new Location("EA",51, 101, artOfWar));
        locationRepository.save(new Location("EA",52, 102, nineteenEight));
    }

    private void updateAuthorsWithBooks() {
        Book artOfWar = bookRepository.findByIsbn("9780140455526");
        Book nineteenEight = bookRepository.findByIsbn("9781432839680");
        Book animalFarm = bookRepository.findByIsbn("9781986799270");
        Book braveNewWorld = bookRepository.findByIsbn("9781407021010");
        Book aBriefHistoryOfTime = bookRepository.findByIsbn("9780857501004");
        Book willemDieMadocMaakte = bookRepository.findByIsbn("9789028223035");
        Book aristotlesNicomacheanEthics = bookRepository.findByIsbn("9780226026756");
        Book assHoles = bookRepository.findByIsbn("9781857886108");
        Book theSubtleArt = bookRepository.findByIsbn("9780062457714");
        Book rulesForLife = bookRepository.findByIsbn("9780345816023");
        Book daniel = bookRepository.findByIsbn("9789063064662");
        Book oneHundred = bookRepository.findByIsbn("9780241968581");
        Book shame = bookRepository.findByIsbn("9780394534084");

        Author sunTzu = authorRepository.findByFirstNameAndLastName("Sun", "Tzu");
        sunTzu.setWritten(List.of(artOfWar));
        Author georgeOrwell = authorRepository.findByFirstNameAndLastName("George", "Orwell");
        georgeOrwell.setWritten(List.of(nineteenEight, animalFarm));
        Author aldousHuxley = authorRepository.findByFirstNameAndLastName("Aldous", "Huxley");
        aldousHuxley.setWritten(List.of(braveNewWorld));
        Author axelBouts = authorRepository.findByFirstNameAndLastName("Axel", "Bouts");
        axelBouts.setWritten(List.of(daniel));
        Author jordanPeterson = authorRepository.findByFirstNameAndLastName("Jordan", "Peterson");
        jordanPeterson.setWritten(List.of(rulesForLife));
        Author markManson = authorRepository.findByFirstNameAndLastName("Mark", "Manson");
        markManson.setWritten(List.of(theSubtleArt));
        Author aaronJames = authorRepository.findByFirstNameAndLastName("Aaron", "James");
        aaronJames.setWritten(List.of(assHoles));
        Author robertCBartlett = authorRepository.findByFirstNameAndLastName("Robert C.", "Bartlett");
        robertCBartlett.setWritten(List.of(aristotlesNicomacheanEthics));
        Author susanDCollins = authorRepository.findByFirstNameAndLastName("Susan D.", "Collins");
        susanDCollins.setWritten(List.of(aristotlesNicomacheanEthics));
        Author nicoDros = authorRepository.findByFirstNameAndLastName("Nico", "Dros");
        nicoDros.setWritten(List.of(willemDieMadocMaakte));
        Author stephenHawking = authorRepository.findByFirstNameAndLastName("Stephen", "Hawking");
        stephenHawking.setWritten(List.of(aBriefHistoryOfTime));
        Author gabrielGarciaMarquez = authorRepository.findByFirstNameAndLastName("Gabriel", "García Márquez");
        gabrielGarciaMarquez.setWritten(List.of(oneHundred));
        Author shameAuthor = authorRepository.findByFirstNameAndLastName("Salman", "Rushdie");
        shameAuthor.setWritten(List.of(shame));

        authorRepository.saveAll(List.of(
                sunTzu, georgeOrwell, aldousHuxley, axelBouts, jordanPeterson, markManson, aaronJames,
                robertCBartlett, susanDCollins, nicoDros, stephenHawking, gabrielGarciaMarquez, shameAuthor
        ));

    }

    private void updateUsersWithRoles() {
        Role ownerRole = roleRepository.findByName("ROLE_OWNER");
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = roleRepository.findByName("ROLE_USER");

        User userFromDatabase = userRepository.findByEmail("user@qwict.com");
        userFromDatabase.setRoles(List.of(userRole));

        User adminFromDatabase = userRepository.findByEmail("admin@qwict.com");
        adminFromDatabase.setRoles(List.of(adminRole, userRole));

        User ownerFromDatabase = userRepository.findByEmail("owner@qwict.com");
        ownerFromDatabase.setRoles(List.of(ownerRole, adminRole, userRole));

        userRepository.saveAll(List.of(
                userFromDatabase, adminFromDatabase, ownerFromDatabase
        ));
    }

    private void updateUsersWithBooks() {
        Book artOfWar = bookRepository.findByIsbn("9780140455526");
        Book nineteenEight = bookRepository.findByIsbn("9781432839680");
        Book aBriefHistoryOfTime = bookRepository.findByIsbn("9780857501004");
        Book willemDieMadocMaakte = bookRepository.findByIsbn("9789028223035");
        Book aristotlesNicomacheanEthics = bookRepository.findByIsbn("9780226026756");
        Book rulesForLife = bookRepository.findByIsbn("9780345816023");


        User userFromDatabase = userRepository.findByEmail("user@qwict.com");
        userFromDatabase.setBooks(List.of(
                artOfWar, nineteenEight, aBriefHistoryOfTime, willemDieMadocMaakte, aristotlesNicomacheanEthics, rulesForLife
        ));
        User adminFromDatabase = userRepository.findByEmail("admin@qwict.com");
        adminFromDatabase.setBooks(List.of(
                artOfWar, nineteenEight, aBriefHistoryOfTime
        ));

        User ownerFromDatabase = userRepository.findByEmail("owner@qwict.com");
        ownerFromDatabase.setBooks(List.of(
                artOfWar, nineteenEight, aBriefHistoryOfTime, willemDieMadocMaakte, rulesForLife
        ));
        userRepository.saveAll(List.of(userFromDatabase, adminFromDatabase, ownerFromDatabase));
    }


}
