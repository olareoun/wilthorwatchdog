package org.olareoun.wwd.client.drive;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.olareoun.wwd.shared.GwtDrive;

import java.util.ArrayList;
import java.util.List;

public class DrivesFrame extends Composite implements UsersEventsHandler{
  interface MyUiBinder extends UiBinder<VerticalPanel, DrivesFrame> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  Button addButton;

  @UiField
  FlexTable drivesTable;

  @UiField
  Button changeButton;

  final MainScreen mainScreen;

  private List<GwtDrive> drives;

  private final HandlerManager mainEventBus;
  
  public DrivesFrame(HandlerManager mainEventBus, MainScreen main) {
    this.mainEventBus = mainEventBus;
    this.mainEventBus.addHandler(UsersFetchedEvent.TYPE, this);
    this.mainEventBus.addHandler(SearchUsersEvent.TYPE, this);
    this.mainScreen = main;
    initWidget(uiBinder.createAndBindUi(this));
    
    this.drivesTable.setVisible(false);
    this.changeButton.setVisible(false);
  }

  @UiHandler("addButton")
  void handleAdd(ClickEvent e) {
    deleteDocs();
    MainScreen.SERVICE.getDocuments(mainScreen.getUsers(), new AsyncCallback<List<GwtDrive>>() {

      @Override
      public void onFailure(Throwable caught) {
        MainScreen.handleFailure(caught);
      }

      @Override
      public void onSuccess(List<GwtDrive> aDrives) {
        setDrives(aDrives);
        refreshTable();
        drivesTable.setVisible(true);
        changeButton.setVisible(true);
      }
    });
  }

  @UiHandler("changeButton")
  void handleChange(ClickEvent e) {
    MainScreen.SERVICE.changePermissions(this.drives, new AsyncCallback<List<GwtDrive>>() {

      @Override
      public void onFailure(Throwable caught) {
//        MainScreen.handleFailure(caught);
      }

      @Override
      public void onSuccess(List<GwtDrive> aDrives) {
//        setDrives(aDrives);
//        refreshTable();
      }
    });
  }

  void refreshTable() {
    drivesTable.removeAllRows();
    drivesTable.setText(0, 1, "Drive Title");
    drivesTable.setText(0, 2, "Drive Owners");
    drivesTable.getCellFormatter().addStyleName(0, 1, "methodsHeaderRow");
    drivesTable.getCellFormatter().addStyleName(0, 2, "methodsHeaderRow");
    for (int i = 0; i < drives.size(); i++) {
      GwtDrive drive = drives.get(i);
      drivesTable.setText(i + 1, 1, drive.title);
      drivesTable.setText(i + 1, 2, drive.ownerNamesString());
    }
  }

  public void hide(){
    this.setVisible(false);
  }

  public void show(){
    this.setVisible(true);
  }

  void setDrives(List<GwtDrive> aDrives){
    this.drives = aDrives;
  }

  @Override
  public void onFetched(List<String> users) {
    show();
  }

  @Override
  public void onFetching() {
    deleteDocs();
  }

  private void deleteDocs() {
    this.drives = new ArrayList<GwtDrive>();
    this.refreshTable();
  }

}
