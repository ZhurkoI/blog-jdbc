package org.zhurko.blog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zhurko.blog.model.Label;
import org.zhurko.blog.repository.LabelRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LabelServiceTest {

    @Mock
    private LabelRepository labelRepo;

    @InjectMocks
    private LabelService labelService;

    @Test
    void givenLabelService_whenInvokeSaveLabel_thenLabelIsSaved() {
        String newLabelName = "Label_1";
        Label newLabel = new Label(newLabelName);

        when(labelRepo.save(newLabel)).thenReturn(newLabel);

        Label actualLabel = labelService.save(newLabel);

        assertEquals(newLabelName, actualLabel.getName());
        verify(labelRepo, times(1)).save(newLabel);
    }

    @Test
    void givenLabelService_whenInvokeFindLabelByName_thenCorrectLabelIsFound() {
        String expectedLabelName = "Label_1";

        when(labelRepo.findByName(expectedLabelName)).thenReturn(new Label(1L, expectedLabelName));

        Label actualLabel = labelService.findByName(expectedLabelName);

        assertEquals(expectedLabelName, actualLabel.getName());
        verify(labelRepo, times(1)).findByName(expectedLabelName);
    }

    @Test
    void givenLabelService_whenInvokeGetAllLabels_thanGetAllMethodOfRepositoryReturnsListOfLabels() {
        List<Label> expectedLabels = new ArrayList<>();
        expectedLabels.add(new Label(1L, "label_1"));
        expectedLabels.add(new Label(2L, "label_2"));
        expectedLabels.add(new Label(3L, "label_3"));

        when(labelRepo.getAll()).thenReturn(expectedLabels);

        List<Label> actualLabels = labelService.getAll();

        assertEquals(expectedLabels.size(), actualLabels.size());
        verify(labelRepo, times(1)).getAll();
    }

    @Test
    void givenLabelService_whenInvokeDeleteLabelById_thenDeleteMethodOfRepoIsCalledOneTime() {
        Long labelId = 1L;

        labelService.deleteById(labelId);

        verify(labelRepo, times(1)).deleteById(labelId);
    }

    @Test
    void givenLabelService_whenInvokeUpdateLabel_thenLabelIsUpdated() {
        String newName = "Label_1_EDITED";
        Label expectedLabel = new Label(1L, newName);

        when(labelRepo.update(expectedLabel)).thenReturn(expectedLabel);


        Label actualLabel = labelService.update(expectedLabel);

        assertEquals(expectedLabel, actualLabel);
        verify(labelRepo, times(1)).update(expectedLabel);
    }

    @Test
    void givenLabelService_whenInvokeGetLabelById_thenCorrectLabelIsFound() {
        Long labelId = 1L;
        Label expectedLabel = new Label(1L, "Label_1");

        when(labelRepo.getById(labelId)).thenReturn(expectedLabel);

        Label actualLabel = labelService.getById(labelId);

        assertEquals(expectedLabel, actualLabel);
        verify(labelRepo, times(1)).getById(labelId);
    }
}