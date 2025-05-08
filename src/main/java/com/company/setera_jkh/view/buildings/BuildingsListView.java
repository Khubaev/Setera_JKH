package com.company.setera_jkh.view.buildings;

import com.company.setera_jkh.entity.Buildings;
import com.company.setera_jkh.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "buildingses", layout = MainView.class)
@ViewController(id = "setera_Buildings.list")
@ViewDescriptor(path = "buildings-list-view.xml")
@LookupComponent("buildingsesDataGrid")
@DialogMode(width = "64em")
public class BuildingsListView extends StandardListView<Buildings> {
}