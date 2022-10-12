package org.zhurko.blog.view;

import org.zhurko.blog.controller.PostController;
import org.zhurko.blog.controller.WriterController;
import org.zhurko.blog.model.Post;
import org.zhurko.blog.model.Writer;
import org.zhurko.blog.util.UserInputReader;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WriterView {

    private static final String[] WRITER_MENU = {
            "0 - .. (Back to parent menu)",
            "1 - Create writer",
            "2 - Get all writers",
            "3 - Get writer by ID",
            "4 - Edit writer",
            "5 - Add post to the writer",
            "6 - Remove post from the writer",
            "7 - Delete writer by ID"
    };

    private final Scanner scanner = new Scanner(System.in);
    private final WriterController writerController = new WriterController();
    private final PostController postController = new PostController();

    public void runMenu() {
        while (true) {
            int choice = getChoice(WRITER_MENU);

            switch (choice) {
                case 0:
                    return;
                case 1:
                    createWriter();
                    break;
                case 2:
                    showAllWriters();
                    break;
                case 3:
                    showWriterById();
                    break;
                case 4:
                    editWriter();
                    break;
                case 5:
                    addPostToWriter();
                    break;
                case 6:
                    removePostFromWriter();
                    break;
                case 7:
                    removeWriter();
                    break;
            }
        }
    }

    private void createWriter() {
        System.out.print("Enter first name: ");
        String stringInput1 = scanner.nextLine();
        System.out.print("Enter last name: ");
        String stringInput2 = scanner.nextLine();
        Writer createdWriter = writerController.saveNewWriter(stringInput1, stringInput2);
        if (createdWriter != null) {
            System.out.println("Writer has been created:");
            printWriters(createdWriter);
        } else {
            System.out.println("Writer has not been created.");
        }
    }

    private void showAllWriters() {
        List<Writer> allWriters = writerController.getAll();
        if (!allWriters.isEmpty()) {
            System.out.println("List of writers:");
            printWriters(allWriters);
        } else {
            System.out.println("No writers exist.");
        }
    }

    private void showWriterById() {
        System.out.print("Enter ID of the writer: ");
        Long numberInput = UserInputReader.readNumberInput();
        Writer writer = writerController.getWriterById(numberInput);
        if (writer != null) {
            System.out.println("Writer is:");
            printWriters(writer);
        } else {
            System.out.printf("Writer with ID=%d doesn't exist.%n", numberInput);
        }
    }

    private void editWriter() {
        System.out.print("Enter ID of the writer you want to edit: ");
        Long numberInput = UserInputReader.readNumberInput();
        Writer writer = writerController.getWriterById(numberInput);
        if (writer == null) {
            System.out.printf("Writer with ID=%d doesn't exist.%n", numberInput);
            return;
        }

        System.out.print("Enter new writer's first name: ");
        String stringInput1 = scanner.nextLine();
        System.out.print("Enter new writer's last name: ");
        String stringInput2 = scanner.nextLine();
        writer = writerController.updateWriter(writer.getId(), stringInput1, stringInput2);
        if (writer == null) {
            System.out.printf("Writer with ID=%d wasn't updated.%n", numberInput);
            return;
        }

        System.out.printf("Writer with ID=%d has been updated:%n", writer.getId());
        printWriters(writer);
    }

    private void addPostToWriter() {
        System.out.print("Enter ID of the writer: ");
        Long numberInput = UserInputReader.readNumberInput();
        Writer writer = writerController.getWriterById(numberInput);
        if (writer == null) {
            System.out.printf("Writer with ID=%d doesn't exist.%n", numberInput);
            return;
        }

        List<Post> allPosts = postController.getAll();
        if (!allPosts.isEmpty()) {
            System.out.println("List of available posts in the application:");
            allPosts.forEach((p -> System.out.printf("ID=%d | CONTENT: %s%n", p.getId(), p.getContent())));
        } else {
            System.out.println("There are no posts in the application.");
            return;
        }

        System.out.print("Enter ID of the post: ");
        Long id = UserInputReader.readNumberInput();
        Post post = allPosts.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
        if (post == null) {
            System.out.printf("Post with ID=%d doesn't exist.%n", numberInput);
            return;
        }

        writer = writerController.addPost(writer.getId(), post.getId());
        if (writer != null) {
            System.out.println("Post has been added:");
            printWriters(writer);
        } else {
            System.out.println("Post wasn't added.");
        }
    }

    private void removePostFromWriter() {
        System.out.print("Enter ID of the writer: ");
        Long numberInput = UserInputReader.readNumberInput();
        Writer writer = writerController.getWriterById(numberInput);
        if (writer == null) {
            System.out.printf("Writer with ID=%d doesn't exist.%n", numberInput);
            return;
        }

        if (writer.getPosts().isEmpty()) {
            System.out.println("Selected writer has no posts.");
            return;
        }

        System.out.println("There are posts by selected writer:");
        writer.getPosts().forEach(p -> System.out.printf("ID=%d | CONTENT: %s%n", p.getId(), p.getContent()));

        System.out.print("Enter ID of the post you want to remove: ");
        numberInput = UserInputReader.readNumberInput();
        Post post = postController.getPostById(numberInput);
        if (post == null) {
            System.out.printf("There is no post with ID=%d.%n", numberInput);
            return;
        }

        writer = writerController.removePost(writer.getId(), post.getId());
        if (writer != null) {
            System.out.println("Post has been removed:");
            printWriters(writer);
        } else {
            System.out.println("Post wasn't removed.");
        }
    }

    private void removeWriter() {
        System.out.print("Enter ID of the writer: ");
        Long numberInput = UserInputReader.readNumberInput();
        writerController.deleteById(numberInput);
        System.out.println("Post has been deleted.");
    }

    private int getChoice(String[] menuEntries) {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        do {
            System.out.println();
            System.out.println("Writer Menu:");
            Arrays.stream(menuEntries).forEach(System.out::println);
            System.out.print("Please make a selection: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid selection. Numbers only please.");
                scanner.next();
            }
        } while (choice < 0 || choice > menuEntries.length);
        return choice;
    }

    private String getRelatedPosts(Writer writer) {
        if (writer.getPosts().isEmpty()) {
            return "<no posts>";
        } else {
            return writer.getPosts().stream()
                    .map(Post::getId)
                    .map(Objects::toString)
                    .collect(Collectors.joining(", "));
        }
    }

    private void printWriters(List<Writer> writers) {
        writers.forEach(w -> System.out.printf("ID=%d | FIRST NAME: %s | LAST NAME: %s | POST ID: %s%n",
                w.getId(), w.getFirstName(), w.getLastName(), getRelatedPosts(w)));
    }

    private void printWriters(Writer writer) {
        System.out.printf("ID=%d | FIRST NAME: %s | LAST NAME: %s | POST ID: %s%n",
                writer.getId(), writer.getFirstName(), writer.getLastName(), getRelatedPosts(writer));
    }
}
