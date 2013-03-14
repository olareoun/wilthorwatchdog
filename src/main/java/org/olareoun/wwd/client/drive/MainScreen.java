package org.olareoun.wwd.client.drive;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

import org.olareoun.wwd.client.users.UsersFrame;
import org.olareoun.wwd.client.users.UsersService;
import org.olareoun.wwd.client.users.UsersServiceAsync;
import org.olareoun.wwd.shared.AuthenticationException;

import java.util.List;

public class MainScreen implements EntryPoint {

  private DrivesFrame driveFrame;
  private AuthFrame authFrame;
  private UsersFrame usersFrame;
  
  private HandlerManager eventBus;

  private RootPanel rootPanel;

  static final DriveServiceAsync SERVICE = GWT.create(DriveService.class);
  static final UsersServiceAsync USERS_SERVICE = GWT.create(UsersService.class);
  private HorizontalPanel horizontalPanel;

  @Override
  public void onModuleLoad() {
    this.eventBus = new HandlerManager(this);

    rootPanel = RootPanel.get("main");
    
    USERS_SERVICE.hasPermission(new AsyncCallback<Boolean>() {
      @Override
      public void onSuccess(Boolean result) {
        if (result){
          initScreen();
        } else {
          Window.alert("Logged users is not admin");
        }
      }
      @Override
      public void onFailure(Throwable caught) {
        Window.alert("Failed to check user permission");
      }
    });
    
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

  void initScreen() {
    authFrame = new AuthFrame(this.eventBus);
    rootPanel.add(authFrame);
    
    horizontalPanel = new HorizontalPanel();
    rootPanel.add(horizontalPanel);
    
    usersFrame = new UsersFrame(this.eventBus);
    horizontalPanel.add(usersFrame);
    
    driveFrame = new DrivesFrame(this.eventBus, this);
    horizontalPanel.add(driveFrame);
    
    driveFrame.hide();
  }

}
