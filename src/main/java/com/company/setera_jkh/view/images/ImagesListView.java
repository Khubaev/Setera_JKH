package com.company.setera_jkh.view.images;

import com.company.setera_jkh.entity.Images;
import com.company.setera_jkh.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "imageses", layout = MainView.class)
@ViewController(id = "setera_Images.list")
@ViewDescriptor(path = "images-list-view.xml")
@LookupComponent("imagesesDataGrid")
@DialogMode(width = "64em")
public class ImagesListView extends StandardListView<Images> {
}