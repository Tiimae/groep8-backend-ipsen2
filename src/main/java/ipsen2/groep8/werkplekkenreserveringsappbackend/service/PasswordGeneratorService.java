package ipsen2.groep8.werkplekkenreserveringsappbackend.service;

import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class PasswordGeneratorService {

    private final String lowercase = "abcdefghijklmnopqrstuvwxyz";
    private final String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String numbers = "0123456789";
    private final String specialChar = "!@#$%&*()_+-=[]|,./?><";


    public String generate(int amount) {
        final StringBuilder password = new StringBuilder();
        Random random = new Random(System.nanoTime());

        ArrayList<String> charCategories = new ArrayList<>();
        charCategories.add(this.lowercase);
        charCategories.add(this.uppercase);
        charCategories.add(this.numbers);
        charCategories.add(this.specialChar);

        for (int i = 0; i < amount; i++) {
            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }

        return password.toString();
    }
}
