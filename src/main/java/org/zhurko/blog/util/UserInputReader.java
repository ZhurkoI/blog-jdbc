package org.zhurko.blog.util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInputReader {

    private UserInputReader() {
    }

    public static Long readNumberInput() {
        Scanner scanner = new Scanner(System.in);
        Long numberInput = -1L;
        do {
            try {
                numberInput = scanner.nextLong();
                scanner.nextLine();
            } catch (InputMismatchException exception) {
                System.out.println("Invalid selection. Numbers only please.");
                scanner.nextLine();
            }
        } while (numberInput < 0);
        return numberInput;
    }
}
