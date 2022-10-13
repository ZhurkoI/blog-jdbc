package org.zhurko.blog.controller;

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
class LabelControllerTest {

    @Mock
    LabelRepository labelRepo;

    @InjectMocks
    LabelController labelController;

    @Test
    void givenLabelController_whenInvokeSaveLabel_thenLabelIsSaved() {
        String newLabelName = "Label_1";
        Label newLabel = new Label(newLabelName);

        when(labelRepo.save(newLabel)).thenReturn(newLabel);

        Label actualLabel = labelController.saveLabel(newLabelName);

        assertEquals(newLabelName, actualLabel.getName());
        verify(labelRepo, times(1)).save(new Label(newLabelName));
    }

    @Test
    void givenLabelController_whenInvokeFindLabelByName_thenCorrectLabelIsFound() {
        String expectedLabelName = "Label_1";

        when(labelRepo.findByName(expectedLabelName)).thenReturn(new Label(1L, expectedLabelName));

        Label actualLabel = labelController.findLabelByName(expectedLabelName);

        assertEquals(expectedLabelName, actualLabel.getName());
        verify(labelRepo, times(1)).findByName(expectedLabelName);
    }

    @Test
    void givenLabelController_whenInvokeGetAllLabels_thanGetAllMethodOfRepositoryReturnsListOfLabels() {
        List<Label> expectedLabels = new ArrayList<>();
        expectedLabels.add(new Label(1L, "label_1"));
        expectedLabels.add(new Label(2L, "label_2"));
        expectedLabels.add(new Label(3L, "label_3"));

        when(labelRepo.getAll()).thenReturn(expectedLabels);

        List<Label> actualLabels = labelController.getAll();

        assertEquals(expectedLabels.size(), actualLabels.size());
        verify(labelRepo, times(1)).getAll();
    }

    @Test
    void givenLabelController_whenInvokeDeleteLabelById_thenDeleteMethodOfRepoIsCalledOneTime() {
        Long labelId = 1L;

        labelController.deleteLabelById(labelId);

        verify(labelRepo, times(1)).deleteById(labelId);
    }

    @Test
    void givenLabelController_whenInvokeUpdateLabel_thenLabelIsUpdated() {
        Label existentLabel = new Label(1L, "Label_1");
        String newName = "Label_1_EDITED";
        Label expectedLabel = new Label(1L, newName);

        when(labelRepo.findByName(existentLabel.getName())).thenReturn(existentLabel);
        when(labelRepo.update(expectedLabel)).thenReturn(expectedLabel);


        Label actualLabel = labelController.updateLabel(existentLabel.getName(), newName);

        assertEquals(expectedLabel, actualLabel);
        verify(labelRepo, times(1)).update(expectedLabel);
    }

    @Test
    void givenLabelController_whenInvokeGetLabelById_thenCorrectLabelIsFound() {
        Long labelId = 1L;
        Label expectedLabel = new Label(1L, "Label_1");

        when(labelRepo.getById(labelId)).thenReturn(expectedLabel);

        Label actualLabel = labelController.getLabelById(labelId);

        assertEquals(expectedLabel, actualLabel);
        verify(labelRepo, times(1)).getById(labelId);
    }
}