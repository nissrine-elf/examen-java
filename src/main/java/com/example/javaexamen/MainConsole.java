package com.example.javaexamen;

import java.util.Scanner;

public class MainConsole { public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int choix;
    do {
        // Affichage du menu principal
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. ");
        System.out.println("2. ");
        System.out.println("3. ");
        System.out.println("4. ");
        System.out.println("5. ");
        System.out.println("6. ");
        System.out.println("7.");
        System.out.println("8. ");
        System.out.println("9.  ");
        System.out.println("10.  ");
        System.out.println("11. Quitter");
        System.out.print("Entrez votre choix: ");


        choix = scanner.nextInt();
        scanner.nextLine();
        switch (choix) {
            case 1:

                break;
            case 2:


                break;
            case 3:


                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;
            case 9:

                break;
            case 10:

                break;
            case 11:
                System.out.println("Au revoir!");
                break;
            default:
                System.out.println("Choix invalide, veuillez réessayer.");
        }
    } while (choix != 11);


}
}
