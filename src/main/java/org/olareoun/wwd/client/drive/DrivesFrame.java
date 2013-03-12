package org.olareoun.wwd.client.drive;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.olareoun.wwd.shared.GwtDrive;

import java.util.List;

public class DrivesFrame extends Composite {
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
  
  public DrivesFrame(MainScreen main) {
    this.mainScreen = main;
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("addButton")
  void handleAdd(ClickEvent e) {
    MainScreen.SERVICE.getDocuments(mainScreen.getUsers(), new AsyncCallback<List<GwtDrive>>() {

      @Override
      public void onFailure(Throwable caught) {
        MainScreen.handleFailure(caught);
      }

      @Override
      public void onSuccess(List<GwtDrive> aDrives) {
        setDrives(aDrives);
        refreshTable();
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

}
