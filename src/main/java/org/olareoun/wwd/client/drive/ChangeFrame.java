package org.olareoun.wwd.client.drive;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import org.olareoun.wwd.client.users.DocsEventsHandler;
import org.olareoun.wwd.client.users.DocsFetchedEvent;
import org.olareoun.wwd.shared.GwtDoc;
import org.olareoun.wwd.shared.UsersDocs;

import java.util.List;

public class ChangeFrame extends Composite implements UsersEventsHandler, DocsEventsHandler{

  Button changeButton;
  Label userForDocsLabel;
  ListBox userForDocs;

  private final HandlerManager mainEventBus;

  private UsersDocs usersDocs;
  private HorizontalPanel panel;
  
  public ChangeFrame(HandlerManager mainEventBus) {
    this.mainEventBus = mainEventBus;
    this.mainEventBus.addHandler(UsersFetchedEvent.TYPE, this);
    this.mainEventBus.addHandler(SearchUsersEvent.TYPE, this);
    this.mainEventBus.addHandler(DocsFetchedEvent.TYPE, this);
    this.panel = new HorizontalPanel();
    initChangeButton();
    this.userForDocsLabel = new Label("Owner");
    initUserForDocs();
    this.panel.add(userForDocsLabel);
    this.panel.add(userForDocs);
    this.panel.add(changeButton);
    initWidget(this.panel);
  }

  private void initUserForDocs() {
    this.userForDocs = new ListBox();
  }

  /**
   * 
   */
  private void initChangeButton() {
    this.changeButton = new Button("Change Owner");
    this.changeButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        handleChange(event);
      }
    });
  }

  void handleChange(ClickEvent e) {
    MainScreen.SERVICE.changePermissions(this.userForDocs.getItemText(this.userForDocs.getSelectedIndex()), this.usersDocs.getAllDocs(), new AsyncCallback<List<GwtDoc>>() {
      @Override
      public void onFailure(Throwable caught) {
      }
      @Override
      public void onSuccess(List<GwtDoc> aChange) {
        Window.alert("Owners changed");
      }
    });
  }

  public void hide(){
    this.setVisible(false);
  }

  public void show(){
    this.setVisible(true);
  }

  @Override
  public void onUsersFetched(List<String> users) {
    for (String email : users) {
      this.userForDocs.addItem(email);
    }
  }

  @Override
  public void onFetching() {
    hide();
  }

  @Override
  public void onDocsFetching() {
  }

  @Override
  public void onDocsFetched(UsersDocs usersDocs) {
    this.usersDocs = usersDocs;
  }

}
