package org.olareoun.wwd.client.drive;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;

import java.util.List;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
public class AuthFrame  extends Composite {

  Label passLabel;
  PasswordTextBox passTextBox;
  Button addButton;

  final HandlerManager mainEventBus;
  private HorizontalPanel panel;

  public AuthFrame(HandlerManager mainEventBus) {
    this.mainEventBus = mainEventBus;
    this.panel = new HorizontalPanel();
    this.passLabel = new Label("Password");
    this.passTextBox = new PasswordTextBox();
    initAddButton();
    this.panel.add(this.passLabel);
    this.panel.add(this.passTextBox);
    this.panel.add(this.addButton);
    this.initWidget(this.panel);
  }

  private void initAddButton() {
    this.addButton = new Button("Find users");
    this.addButton.addClickHandler(new ClickHandler() {
      
      @Override
      public void onClick(ClickEvent event) {
        handleAdd(event);
      }
    });
  }

  void handleAdd(ClickEvent e) {
    this.mainEventBus.fireEvent(new SearchUsersEvent());
    String password = passTextBox.getText();
    if (password != null) {
      passTextBox.setText("");
      MainScreen.USERS_SERVICE.getUsers(password, new AsyncCallback<List<String>>() {
        @Override
        public void onFailure(Throwable caught) {
          MainScreen.handleFailure(caught);
        }
        @Override
        public void onSuccess(List<String> users) {
          mainEventBus.fireEvent(new UsersFetchedEvent(users));
        }
      });
    }
  }
}
