package com.company.setera_jkh.view.orders;

import com.company.setera_jkh.entity.Images;
import com.company.setera_jkh.entity.Orders;
import com.company.setera_jkh.view.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.upload.*;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.core.FileStorageException;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import com.vaadin.flow.component.html.Div;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.upload.JmixUpload;
import io.jmix.flowui.component.upload.receiver.MultiFileTemporaryStorageBuffer;
import io.jmix.flowui.component.upload.receiver.TemporaryStorageFileData;
import io.jmix.flowui.data.grid.DataGridItems;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.upload.TemporaryStorage;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.notification.NotificationVariant;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

import java.util.*;

@Route(value = "orderses/:id", layout = MainView.class)
@ViewController(id = "setera_Orders.detail")
@ViewDescriptor(path = "orders-detail-view.xml")
@EditedEntityContainer("ordersDc")
public class OrdersDetailView extends StandardDetailView<Orders> {
    private Runnable updateInfoPanel;

    @ViewComponent
    private DataGrid<Images> imagesDataGrid;

    @ViewComponent
    private Div infoPanel;

    @ViewComponent
    private InstanceContainer<Orders> ordersDc;

    @Autowired
    private UiComponents uiComponents;

    @Autowired
    private FileStorage fileStorage;

    @ViewComponent
    private MessageBundle messageBundle;

    @Autowired
    private TemporaryStorage temporaryStorage;

    @ViewComponent
    private JmixUpload upload;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private Notifications notifications;

    private Map<UUID, TemporaryStorageFileData> temporaryFiles = new HashMap<>();

    @Subscribe
    public void onInit(InitEvent event) {
        setupImageColumn();
        setupActionsColumn();
        imagesDataGrid.getDataProvider().refreshAll();
    }


    private void updateInfoPanel() {
        infoPanel.setText(messageBundle.getMessage("infoPanel.text.unsavedChanges"));
        infoPanel.getElement().getThemeList().remove("success");
        infoPanel.getElement().getThemeList().add("error");
    }

    public void setChangeEventListener(Runnable updateInfoPanel) {
        this.updateInfoPanel = updateInfoPanel;
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPostSave(final DataContext.PostSaveEvent event) {
        if (!event.getSavedInstances().isEmpty() && updateInfoPanel != null)
            updateInfoPanel.run();
            imagesDataGrid.getDataProvider().refreshAll();
    }
    private void setupImageColumn() {
        imagesDataGrid.addComponentColumn(images -> {
            if (images != null && images.getFileRef() != null) {
                FileRef fileRef = images.getFileRef();
                Image image = uiComponents.create(Image.class);
                image.setWidth("100px");
                image.setHeight("100px");
                StreamResource streamResource = new StreamResource(fileRef.getFileName(),
                        () -> fileStorage.openStream(fileRef));
                image.setSrc(streamResource);
                return image;
            }
            return null;
        }).setHeader("Image");
    }

    private void setupActionsColumn() {
        imagesDataGrid.addComponentColumn(images -> {
            Button deleteButton = uiComponents.create(Button.class);
            deleteButton.setText("Delete");
            deleteButton.addClickListener(clickEvent -> {
                deleteImage(images);
            });
            return deleteButton;
        }).setHeader("Actions");
    }

    private void deleteImage(Images image) {
        if (image != null) {
            dataManager.remove(image); // Remove the image from the database
            DataGridItems itemsImages = imagesDataGrid.getItems();
            Orders currentOrder = ordersDc.getItem();
            if (currentOrder != null) {
                List<Images> myEntityList = dataManager.load(Images.class)
                        .query("select i from setera_Images i where i.orders = :ordersId1")
                        .parameter("ordersId1", currentOrder)
                        .list();
                imagesDataGrid.setItems(myEntityList); // Устанавливаем обновленный список
            }
        }
    }
    @Subscribe("upload")
    public void onUpload2AllFinished(final AllFinishedEvent event) {
        MultiFileTemporaryStorageBuffer receiver = (MultiFileTemporaryStorageBuffer) event.getSource().getReceiver();
        temporaryFiles = receiver.getFiles();
    }


    @Subscribe("upload")
    public void onUploadFileRejected(final FileRejectedEvent event) {
        notifications.create(event.getErrorMessage())
                .withThemeVariant(NotificationVariant.LUMO_WARNING)
                .withDuration(5000)
                .show();
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        Orders currentOrder = ordersDc.getItem(); // Get the current Orders entity
        if (currentOrder != null) {
            List<Images> myEntityList = dataManager.load(Images.class)
                    .query("select i from setera_Images i where i.orders = :ordersId1")
                    .parameter("ordersId1", currentOrder) // Pass the current Orders entity
                    .list();
            imagesDataGrid.setItems(myEntityList);
        }
    }

    @Install(to = "saveAction", subject = "successHandler")
    private void saveActionSuccessHandler() {
        for (UUID fileId : temporaryFiles.keySet()) {
            try {
                FileRef fileRef = temporaryStorage.putFileIntoStorage(fileId, temporaryFiles.get(fileId).getFileName());
                saveFileToDatabase(fileRef);
            } catch (FileStorageException e) {
                e.printStackTrace();
            }
        }
        // Очистка временных файлов после сохранения
    }
    private void saveFileToDatabase(FileRef fileData) {
        Images image = dataManager.create(Images.class);
        image.setOrders(getEditedEntity());
        image.setFileRef(fileData);
        dataManager.save(image);
    }


}
