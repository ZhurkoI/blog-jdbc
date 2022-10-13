package org.zhurko.blog.view;

import org.zhurko.blog.controller.LabelController;
import org.zhurko.blog.controller.PostController;
import org.zhurko.blog.model.Label;
import org.zhurko.blog.model.Post;
import org.zhurko.blog.repository.sql.SqlLabelRepositoryImpl;
import org.zhurko.blog.repository.sql.SqlPostRepositoryImpl;
import org.zhurko.blog.util.UserInputReader;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PostView {

    private static final String[] POST_MENU = {
            "0 - .. (Back to parent menu)",
            "1 - Create post",
            "2 - Get all posts",
            "3 - Get post by ID",
            "4 - Edit post",
            "5 - Add label to the post",
            "6 - Remove label from the post",
            "7 - Delete post by ID"
    };

    private final Scanner scanner = new Scanner(System.in);
    private final PostController postController = new PostController(new SqlPostRepositoryImpl(),
            new SqlLabelRepositoryImpl());
    private final LabelController labelController = new LabelController(new SqlLabelRepositoryImpl());

    public void runMenu() {
        while (true) {
            int choice = getChoice(POST_MENU);

            switch (choice) {
                case 0:
                    return;
                case 1:
                    createPost();
                    break;
                case 2:
                    showAllPosts();
                    break;
                case 3:
                    showPostById();
                    break;
                case 4:
                    editPost();
                    break;
                case 5:
                    addLabelToPost();
                    break;
                case 6:
                    removeLabelFromPost();
                    break;
                case 7:
                    removePost();
                    break;
            }
        }
    }

    private void removePost() {
        System.out.print("Enter ID of the post you want to remove: ");
        Long numberInput = UserInputReader.readNumberInput();
        postController.deleteById(numberInput);
    }

    private void removeLabelFromPost() {
        System.out.print("Enter ID of the post: ");
        Long numberInput = UserInputReader.readNumberInput();
        Post post = postController.getPostById(numberInput);
        if (post == null) {
            System.out.printf("Post with ID=%d doesn't exist.%n", numberInput);
            return;
        }

        if (post.getLabels().isEmpty()) {
            System.out.println("There are no labels assigned to the post");
            return;
        }

        System.out.println("List of labels assigned to the post:");
        post.getLabels().forEach(l -> System.out.printf("ID=%d | %s%n", l.getId(), l.getName()));

        System.out.print("Enter ID of the label you want to remove: ");
        numberInput = UserInputReader.readNumberInput();
        Label label = labelController.getLabelById(numberInput);
        if (label == null) {
            System.out.printf("There is no label with ID=%d assigned to selected post.%n", numberInput);
            return;
        }

        post = postController.removeLabel(post.getId(), label.getId());
        System.out.println("Label removed:");
        printPosts(post);
    }

    private void addLabelToPost() {
        System.out.print("Enter ID of the post: ");
        Long numberInput = UserInputReader.readNumberInput();
        Post post = postController.getPostById(numberInput);
        if (post == null) {
            System.out.printf("Post with ID=%d doesn't exist.%n", numberInput);
            return;
        }

        List<Label> allLabels = labelController.getAll();
        if (!allLabels.isEmpty()) {
            System.out.println("List of available labels:");
            allLabels.forEach(n -> System.out.printf("ID=%d | NAME: %s%n", n.getId(), n.getName()));
        } else {
            System.out.println("No labels exist.");
        }

        System.out.print("Enter ID of the label: ");
        Long id = UserInputReader.readNumberInput();
        Label label = allLabels.stream().filter(l -> l.getId().equals(id)).findFirst().orElse(null);
        if (label == null) {
            System.out.printf("Label with ID=%d doesn't exist.%n", numberInput);
            return;
        }

        post = postController.addLabel(post.getId(), label.getId());

        if (post != null) {
            System.out.println("Label added:");
            printPosts(post);
        } else {
            System.out.println("Error occurred. The label has not been added.");
        }
    }

    private void editPost() {
        System.out.print("Enter ID of the post you want to edit: ");
        Long numberInput = UserInputReader.readNumberInput();
        Post post = postController.getPostById(numberInput);
        if (post == null) {
            System.out.printf("Post with ID=%d doesn't exist.%n", numberInput);
            return;
        }

        System.out.print("Enter new content: ");
        String stringInput = scanner.nextLine();
        post = postController.updatePost(post.getId(), stringInput);
        System.out.printf("Post with ID=%d has been updated. New post content:%n", post.getId());
        printPosts(post);
    }

    private void showPostById() {
        System.out.print("Enter ID of the post: ");
        Long numberInput = UserInputReader.readNumberInput();
        Post post = postController.getPostById(numberInput);
        if (post != null) {
            System.out.println("Post found:");
            printPosts(post);
        } else {
            System.out.printf("Post with ID=%d doesn't exist.%n", numberInput);
        }
    }

    private void showAllPosts() {
        List<Post> allPosts = postController.getAll();
        if (!allPosts.isEmpty()) {
            System.out.println("List of available posts:");
            printPosts(allPosts);
        } else {
            System.out.println("No posts exist.");
        }
    }

    private void createPost() {
        String stringInput;
        System.out.print("Enter post content: ");
        stringInput = scanner.nextLine();
        Post createdPost = postController.saveNewPost(stringInput);

        if (createdPost != null) {
            System.out.println("Post has been created:");
            printPosts(createdPost);
        } else {
            System.out.println("Post has not been created.");
        }
    }

    private int getChoice(String[] menuEntries) {
        Long choice;
        do {
            System.out.println();
            System.out.println("Post Menu:");
            Arrays.stream(menuEntries).forEach(System.out::println);
            System.out.print("Please make a selection: ");
            choice = UserInputReader.readNumberInput();
        } while (choice < 0 || choice > menuEntries.length);
        return choice.intValue();
    }

    private String getAssignedLabels(Post post) {
        if (post.getLabels().isEmpty()) {
            return "<no labels>";
        } else {
            return post.getLabels().stream()
                    .map(Label::getName)
                    .collect(Collectors.joining(", "));
        }
    }

    private void printPosts(List<Post> posts) {
        posts.forEach(p -> System.out.printf("ID=%d | CREATED: %s | UPDATED: %s | STATUS: %s | LABELS: %s | CONTENT: %s%n",
                p.getId(), p.getCreated(), p.getUpdated(), p.getPostStatus(), getAssignedLabels(p), p.getContent()));
    }

    private void printPosts(Post post) {
        System.out.printf("ID=%d | CREATED: %s | UPDATED: %s | STATUS: %s | LABELS: %s | CONTENT: %s%n",
                post.getId(), post.getCreated(), post.getUpdated(), post.getPostStatus(), getAssignedLabels(post),
                post.getContent());
    }
}
