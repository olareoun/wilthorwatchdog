package org.olareoun.wwd.client.drive;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;

import org.olareoun.wwd.shared.AuthenticationException;
import org.olareoun.wwd.shared.DriveAbout;
import org.olareoun.wwd.shared.GwtDrive;

import java.util.List;

public class DriveGwtSample implements EntryPoint {

  DrivesFrame driveFrame;
  List<GwtDrive> drives;

  AboutFrame aboutFrame;
  DriveAbout about;

  static final DriveServiceAsync SERVICE = GWT.create(DriveService.class);

  @Override
  public void onModuleLoad() {
    driveFrame = new DrivesFrame(this);
    RootPanel.get("main").add(driveFrame);
    // "loading drives..."
    FlexTable drivesTable = driveFrame.drivesTable;
    drivesTable.setText(0, 0, "Loading Drives...");
    drivesTable.getCellFormatter().addStyleName(0, 0, "methodsHeaderRow");

    aboutFrame = new AboutFrame(this);
    RootPanel.get("main").add(aboutFrame);
  }
  
  void setDrives(List<GwtDrive> aDrives){
    drives = aDrives;
  }

  void refreshTable() {
    FlexTable drivesTable = driveFrame.drivesTable;
    drivesTable.removeAllRows();
    drivesTable.setText(0, 1, "Drive Title");
    drivesTable.getCellFormatter().addStyleName(0, 1, "methodsHeaderRow");
    for (int i = 0; i < drives.size(); i++) {
      GwtDrive drive = drives.get(i);
      drivesTable.setText(i + 1, 1, drive.title);
    }
  }

  static void handleFailure(Throwable caught) {
    if (caught instanceof AuthenticationException) {
      Window.Location.reload();
    } else {
      caught.printStackTrace();
      Window.alert("ERROR: " + caught.getMessage());
    }
  }

  /**
   * @param result
   */
  public void setAbout(DriveAbout result) {
    this.about = result;
  }

  /**
   * 
   */
  public void refreshAbout() {
    aboutFrame.nameField.setValue(about.name);
    aboutFrame.nameField.setReadOnly(true);
    aboutFrame.rootFolderIdField.setValue(about.rootFolderId);
    aboutFrame.rootFolderIdField.setReadOnly(true);
  }

}
