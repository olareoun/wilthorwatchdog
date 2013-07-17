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

import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
public class UsersFetchedEvent extends GwtEvent<UsersEventsHandler> {

  public static final GwtEvent.Type<UsersEventsHandler> TYPE = new GwtEvent.Type<UsersEventsHandler>();

  private final List<String> users;

  public UsersFetchedEvent(List<String> users) {
    this.users = users;
  }

  @Override
  public GwtEvent.Type<UsersEventsHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(UsersEventsHandler handler) {
    handler.onUsersFetched(this.users);
  }
  
}