package org.zhurko.blog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zhurko.blog.model.Writer;
import org.zhurko.blog.repository.WriterRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriterServiceTest {

    @Mock
    private WriterRepository writerRepo;

    @InjectMocks
    private WriterService writerService;

    @Test
    void givenWriterService_whenInvokeGetAllMethod_thanListOfAllWritersIsReturned() {
        Writer writer1 = new Writer(1L, "name_1", "surname_1");
        Writer writer2 = new Writer(2L, "name_2", "surname_2");
        List<Writer> expectedWriters = new ArrayList<>();
        expectedWriters.add(writer1);
        expectedWriters.add(writer2);

        when(writerRepo.getAll()).thenReturn(expectedWriters);

        List<Writer> actualWriters = writerService.getAll();

        assertTrue(expectedWriters.size() == actualWriters.size()
                && (expectedWriters.containsAll(actualWriters))
                && (actualWriters.containsAll(expectedWriters)));
        verify(writerRepo, times(1)).getAll();
    }

    @Test
    void givenWriterService_whenInvokeGetByIdMethod_thanCorrectWriterIsFound() {
        Long writerId = 1L;
        Writer expectedWriter = new Writer(writerId, "name_1", "name_2");

        when(writerService.getById(writerId)).thenReturn(expectedWriter);

        Writer actualWriter = writerService.getById(writerId);

        assertEquals(expectedWriter, actualWriter);
        verify(writerRepo, times(1)).getById(writerId);
    }

    @Test
    void givenWriterService_whenInvokeSaveMethod_thenWriterIsSaved() {
        String name = "Name_1";
        String surname = "Surname_1";

        Writer expectedWriter = new Writer(1L, name, surname);

        when(writerRepo.save(new Writer(name, surname))).thenReturn(expectedWriter);

        Writer actualWriter = writerService.save(new Writer(name, surname));

        assertEquals(expectedWriter, actualWriter);
        assertNotNull(actualWriter.getId());
        verify(writerRepo, times(1)).save(new Writer(name, surname));
    }

    @Test
    void givenWriterService_whenInvokeDeleteByIdMethod_thenDeleteMethodOfRepoIsCalledOneTime() {
        Long writerId = 1L;

        writerService.deleteById(writerId);

        verify(writerRepo, times(1)).deleteById(writerId);
    }

    @Test
    void update() {
        Writer updatedWriter = new Writer(1L, "John", "Doe");

        writerService.update(updatedWriter);

        verify(writerRepo, times(1)).update(updatedWriter);
    }
}