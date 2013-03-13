package org.olareoun.wwd.client.drive;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

import org.olareoun.wwd.client.users.UsersFrame;
import org.olareoun.wwd.client.users.UsersService;
import org.olareoun.wwd.client.users.UsersServiceAsync;
import org.olareoun.wwd.shared.AuthenticationException;
import org.olareoun.wwd.shared.GwtDoc;

import java.util.List;

public class MainScreen implements EntryPoint {

  DrivesFrame driveFrame;
  List<GwtDoc> drives;

  private AuthFrame authFrame;
  private UsersFrame usersFrame;

  private RootPanel rootPanel;

  static final DriveServiceAsync SERVICE = GWT.create(DriveService.class);
  static final UsersServiceAsync USERS_SERVICE = GWT.create(UsersService.class);
  private HorizontalPanel horizontalPanel;

  @Override
  public void onModuleLoad() {
    rootPanel = RootPanel.get("main");
    
    authFrame = new AuthFrame(this);
    rootPanel.add(authFrame);
    
    horizontalPanel = new HorizontalPanel();
    rootPanel.add(horizontalPanel);
    
    usersFrame = new UsersFrame();
    horizontalPanel.add(usersFrame);

    driveFrame = new DrivesFrame(this);
    horizontalPanel.add(driveFrame);

    driveFrame.hide();
    
  }
  
  static void handleFailure(Throwable caught) {
    if (caught instanceof AuthenticationException) {
      Window.Location.reload();
    } else {
      caught.printStackTrace();
      Window.alert("ERROR: " + caught.getMessage());
    }
  }

  public void refreshUsersTable(List<String> users) {
    usersFrame.setUsers(users);
    usersFrame.refreshTable();
    driveFrame.show();
  }

  public List<String> getUsers() {
    return usersFrame.getUsers();
  }

}
