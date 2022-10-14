package org.zhurko.blog.view;

import org.zhurko.blog.controller.LabelController;
import org.zhurko.blog.model.Label;
import org.zhurko.blog.util.UserInputReader;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LabelView {

    private static final String[] LABEL_MENU = {
            "0 - .. (Back to parent menu)",
            "1 - Create label",
            "2 - Get all labels",
            "3 - Get label by name",
            "4 - Get label by ID",
            "5 - Edit label",
            "6 - Delete label by ID"
    };
    private final Scanner scanner = new Scanner(System.in);
    private final LabelController labelController = new LabelController();

    public void runMenu() {
        while (true) {
            int choice = getChoice(LABEL_MENU);

            switch (choice) {
                case 0:
                    return;
                case 1:
                    createLabel();
                    break;
                case 2:
                    showAllLabels();
                    break;
                case 3:
                    showLabelByName();
                    break;
                case 4:
                    showLabelById();
                    break;
                case 5:
                    editLabel();
                    break;
                case 6:
                    removeLabel();
                    break;
            }
        }
    }

    private void createLabel() {
        System.out.print("Enter name of the label: ");
        String stringInput = scanner.nextLine();
        Label createdLabel = labelController.save(stringInput);

        if (createdLabel != null) {
            System.out.println("Label has been created:");
            System.out.printf("ID=%d | NAME: %s%n", createdLabel.getId(), createdLabel.getName());
        } else {
            System.out.printf("Label '%s' has not been created.%n", stringInput);
        }
    }

    private void showAllLabels() {
        List<Label> allLabels = labelController.getAll();
        if (!allLabels.isEmpty()) {
            System.out.println("List of available labels:");
            allLabels.forEach(n -> System.out.printf("ID=%d | NAME: %s%n", n.getId(), n.getName()));
        } else {
            System.out.println("No labels exist.");
        }
    }

    private void showLabelByName() {
        System.out.print("Enter name of the label: ");
        String stringInput = scanner.nextLine();
        Label label = labelController.findLabelByName(stringInput);
        if (label != null) {
            System.out.printf("ID=%d | NAME: %s%n", label.getId(), label.getName());
        } else {
            System.out.printf("Label '%s' doesn't exist.%n", stringInput);
        }
    }

    private void showLabelById() {
        System.out.print("Enter label ID: ");
        Long numberInput = UserInputReader.readNumberInput();
        Label label = labelController.getLabelById(numberInput);
        if (label != null) {
            System.out.printf("ID=%d | NAME: %s%n", label.getId(), label.getName());
        } else {
            System.out.printf("Label with ID=%d doesn't exist.%n", numberInput);
        }
    }

    private void editLabel() {
        System.out.print("Enter name of the label you want to edit: ");
        String stringInput = scanner.nextLine();
        Label label = labelController.findLabelByName(stringInput);
        if (label == null) {
            System.out.printf("Label '%s' doesn't exist.%n", stringInput);
            return;
        }
        System.out.print("Enter new name of the label: ");
        String stringInput2 = scanner.nextLine();
        label = labelController.updateLabel(stringInput, stringInput2);

        if (label != null) {
            System.out.println("Label has been renamed:");
            System.out.printf("ID=%d | NAME: %s%n", label.getId(), label.getName());
        } else {
            System.out.printf("Label '%s' wasn't updated.%n", stringInput);
        }

    }

    private void removeLabel() {
        System.out.print("Enter ID of the label you want to remove: ");
        Long numberInput = UserInputReader.readNumberInput();
        labelController.deleteLabelById(numberInput);
        System.out.println("Label has been deleted.");
    }

    private int getChoice(String[] menuEntries) {
        Long choice = -1L;
        do {
            System.out.println();
            System.out.println("Label menu:");
            Arrays.stream(menuEntries).forEach(System.out::println);
            System.out.print("Please make a selection: ");
            choice = UserInputReader.readNumberInput();
        } while (choice < 0 || choice > menuEntries.length);
        return choice.intValue();
    }
}
