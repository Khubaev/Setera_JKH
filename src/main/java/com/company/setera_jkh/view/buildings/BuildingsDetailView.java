package com.company.setera_jkh.view.buildings;

import com.company.setera_jkh.entity.Buildings;
import com.company.setera_jkh.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "buildingses/:id", layout = MainView.class)
@ViewController(id = "setera_Buildings.detail")
@ViewDescriptor(path = "buildings-detail-view.xml")
@EditedEntityContainer("buildingsDc")
public class BuildingsDetailView extends StandardDetailView<Buildings> {
}