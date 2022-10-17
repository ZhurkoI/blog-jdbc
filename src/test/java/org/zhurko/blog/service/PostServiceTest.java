package org.zhurko.blog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zhurko.blog.model.Post;
import org.zhurko.blog.model.PostStatus;
import org.zhurko.blog.repository.PostRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepo;

    @InjectMocks
    private PostService postService;

    @Test
    void givenPostService_whenInvokeSaveMethod_thenPostIsSaved() {
        String newPostContent = "post A";
        Date date = new Date();

        Post expectedPost = new Post(1L, newPostContent, date, date, PostStatus.UNDER_REVIEW);

        when(postRepo.save(new Post(newPostContent))).thenReturn(expectedPost);

        Post actualPost = postService.save(new Post(newPostContent));

        assertEquals(expectedPost, actualPost);
        assertNotNull(actualPost.getId());
        verify(postRepo, times(1)).save(new Post(newPostContent));
    }

    @Test
    void givenPostService_whenInvokeGetAllMethod_thanListOfAllNotDeletedPostsIsReturned() {
        List<Post> storedPosts = new ArrayList<>();
        Date date = new Date();

        Post postA = new Post(1L, "Post Content A", date, date, PostStatus.UNDER_REVIEW);
        Post postB = new Post(2L, "Post Content B", date, date, PostStatus.ACTIVE);
        Post postC = new Post(3L, "Post Content C", date, date, PostStatus.DELETED);

        storedPosts.add(postA);
        storedPosts.add(postB);
        storedPosts.add(postC);

        List<Post> expectedPosts = storedPosts.stream()
                .filter(p -> !(p.getPostStatus().equals(PostStatus.DELETED)))
                .collect(Collectors.toList());

        when(postRepo.getAll()).thenReturn(expectedPosts);

        List<Post> actualPosts = postService.getAll();

        assertTrue(expectedPosts.size() == actualPosts.size()
                && expectedPosts.containsAll(actualPosts)
                && actualPosts.containsAll(expectedPosts));
        verify(postRepo, times(1)).getAll();
    }

    @Test
    void givenPostService_whenInvokeGetByIdMethod_thanCorrectNotDeletedPostIsFound() {
        Long notDeletedLabelId = 1L;
        Date date = new Date();
        Post expectedPost = new Post(1L, "Post Content B", date, date, PostStatus.ACTIVE);

        when(postService.getById(notDeletedLabelId)).thenReturn(expectedPost);

        Post actualPost = postService.getById(notDeletedLabelId);

        assertEquals(expectedPost, actualPost);
        verify(postRepo, times(1)).getById(notDeletedLabelId);
    }

    @Test
    void givenPostService_whenInvokeDeleteByIdMethod_thenDeleteMethodOfRepoIsCalledOneTime() {
        Long postId = 1L;

        postService.deleteById(postId);

        verify(postRepo, times(1)).deleteById(postId);
    }

    @Test
    void givenPostService_whenInvokeUpdateMethod_thenUpdateMethodOfRepoIsCalledOneTime() {
        Post updatedPost = new Post("Post content A");

        postService.update(updatedPost);

        verify(postRepo, times(1)).update(updatedPost);
    }
}