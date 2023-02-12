package com.example.chatlogs.bootstrap;

import com.example.chatlogs.models.User;
import com.example.chatlogs.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AddInitialData implements CommandLineRunner {

    public AddInitialData(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User mohit = new User(1L,"Mohit","4234567891", LocalDate.of(1992,4,3));
        User rohit = new User(2L,"Rohit","5234567891", LocalDate.of(1993,5,7));
        User mohan = new User(3L,"Mohan","2234567891", LocalDate.of(1994,6,4));
        User sohan = new User(4L,"Sohan","1234567891", LocalDate.of(1995,7,8));

        userRepository.save(mohit);
        userRepository.save(rohit);
        userRepository.save(mohan);
        userRepository.save(sohan);

    }
}
