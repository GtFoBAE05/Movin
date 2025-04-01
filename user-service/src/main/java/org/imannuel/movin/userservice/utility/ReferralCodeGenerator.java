package org.imannuel.movin.userservice.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ReferralCodeGenerator {
    public static String generateReferralCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        ArrayList<Character> characters = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            char randomLetter = (char) (random.nextInt(26) + 'A');
            characters.add(randomLetter);
        }

        for (int i = 0; i < 4; i++) {
            char randomNumber = (char) (random.nextInt(10) + '0');
            characters.add(randomNumber);
        }

        Collections.shuffle(characters);

        for (Character c : characters) {
            code.append(c);
        }

        return code.toString();
    }
}