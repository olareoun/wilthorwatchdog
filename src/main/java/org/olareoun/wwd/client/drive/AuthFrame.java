/*
 * Copyright (c) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.List;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
public class AuthFrame  extends Composite {

  interface MyUiBinder extends UiBinder<VerticalPanel, AuthFrame> {
  }
  
  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  PasswordTextBox passTextBox;

  @UiField
  Button addButton;

  final HandlerManager mainEventBus;

  public AuthFrame(HandlerManager mainEventBus) {
    initWidget(uiBinder.createAndBindUi(this));

    this.mainEventBus = mainEventBus;

  }

  @UiHandler("addButton")
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
