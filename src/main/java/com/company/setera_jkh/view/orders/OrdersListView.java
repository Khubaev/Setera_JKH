package com.company.setera_jkh.view.orders;

import com.company.setera_jkh.entity.Orders;
import com.company.setera_jkh.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.view.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Route(value = "orderses", layout = MainView.class)
@ViewController(id = "setera_Orders.list")
@ViewDescriptor(path = "orders-list-view.xml")
@LookupComponent("ordersesDataGrid")
@DialogMode(width = "64em")
public class OrdersListView extends StandardListView<Orders> {

}

