package com.darwin.aigus.darwin.AndroidUltils;

import java.util.Random;

public class GenerateId {
    public static String generateId(){
        String letras = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvYyWwXxZz";

        Random random = new Random();

        StringBuilder armazenaChaves = new StringBuilder();
        int index;
        for( int i = 0; i < 9; i++ ) {
            index = random.nextInt( letras.length() );
            armazenaChaves.append(letras.substring(index, index + 1));
        }
        return armazenaChaves.toString();
    }
}
