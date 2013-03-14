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

package org.olareoun.wwd.client.users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.olareoun.wwd.client.drive.SearchUsersEvent;
import org.olareoun.wwd.client.drive.UsersEventsHandler;
import org.olareoun.wwd.client.drive.UsersFetchedEvent;

import java.util.List;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
public class UsersFrame  extends Composite implements UsersEventsHandler{

  interface MyUiBinder extends UiBinder<VerticalPanel, UsersFrame> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  FlexTable usersTable;

  private List<String> users;

  private final HandlerManager mainEventBus;


  public UsersFrame(HandlerManager mainEventBus) {
    this.mainEventBus = mainEventBus;
    this.mainEventBus.addHandler(UsersFetchedEvent.TYPE, this);
    this.mainEventBus.addHandler(SearchUsersEvent.TYPE, this);
    initWidget(uiBinder.createAndBindUi(this));
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
  }

  @Override
  public void onFetching() {
    this.usersTable.setVisible(false);
    this.usersTable.removeAllRows();
  }

  public void hide() {
    this.setVisible(false);
  }

  private void show() {
    this.setVisible(true);
  }
}
