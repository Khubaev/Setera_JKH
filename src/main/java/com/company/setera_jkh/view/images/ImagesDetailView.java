package com.company.setera_jkh.view.images;

import com.company.setera_jkh.entity.Images;
import com.company.setera_jkh.view.main.MainView;
import com.vaadin.flow.component.upload.FinishedEvent;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.view.*;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

@Route(value = "imageses/:id", layout = MainView.class)
@ViewController(id = "setera_Images.detail")
@ViewDescriptor(path = "images-detail-view.xml")
@EditedEntityContainer("imagesDc")
public class ImagesDetailView extends StandardDetailView<Images> {

    }

