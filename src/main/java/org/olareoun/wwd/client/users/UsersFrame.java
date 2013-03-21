package org.olareoun.wwd.client.users;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;

import org.olareoun.wwd.client.drive.MainScreen;
import org.olareoun.wwd.client.drive.SearchUsersEvent;
import org.olareoun.wwd.client.drive.UsersEventsHandler;
import org.olareoun.wwd.client.drive.UsersFetchedEvent;
import org.olareoun.wwd.shared.UsersDocs;

import java.util.List;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
public class UsersFrame  extends Composite implements UsersEventsHandler{

  Button searchButton;
  FlexTable usersTable;
  private List<String> users;
  final HandlerManager mainEventBus;
  private HorizontalPanel panel;


  public UsersFrame(HandlerManager mainEventBus) {
    this.mainEventBus = mainEventBus;
    this.mainEventBus.addHandler(UsersFetchedEvent.TYPE, this);
    this.mainEventBus.addHandler(SearchUsersEvent.TYPE, this);

    this.panel = new HorizontalPanel();
    this.usersTable = new FlexTable();
    this.panel.add(this.usersTable);
    initSearchButton();
    this.panel.add(this.searchButton);

    this.initWidget(this.panel);
  }

  private void initSearchButton() {
    this.searchButton = new Button("Search Docs");
    this.searchButton.setVisible(false);
    this.searchButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        handleSearch(event);
      }
    });
  }

  public void refreshTable() {
    usersTable.removeAllRows();
    usersTable.setText(0, 1, "Users");
    usersTable.getCellFormatter().addStyleName(0, 1, "methodsHeaderRow");
    for (int i = 0; i < users.size(); i++) {
      String user = users.get(i);
      usersTable.setText(i + 1, 1, user);
    }
  }

  public List<String> getUsers() {
    return this.users;
  }

  public void setUsers(List<String> aUsers) {
    this.users = aUsers;
  }

  @Override
  public void onUsersFetched(List<String> users) {
    this.users = users;
    this.show();
    this.usersTable.setVisible(true);
    this.refreshTable();
    this.searchButton.setVisible(true);
  }

  @Override
  public void onFetching() {
    this.usersTable.setVisible(false);
    this.usersTable.removeAllRows();
    this.searchButton.setVisible(false);
  }

  public void hide() {
    this.setVisible(false);
  }

  private void show() {
    this.setVisible(true);
  }

  void handleSearch(ClickEvent e) {
    this.mainEventBus.fireEvent(new SearchDocsEvent());
    MainScreen.SERVICE.getDocuments(this.users, new AsyncCallback<UsersDocs>() {

      @Override
      public void onFailure(Throwable caught) {
        MainScreen.handleFailure(caught);
      }

      @Override
      public void onSuccess(UsersDocs usersDocs) {
        mainEventBus.fireEvent(new DocsFetchedEvent(usersDocs));
      }
    });
  }

}
