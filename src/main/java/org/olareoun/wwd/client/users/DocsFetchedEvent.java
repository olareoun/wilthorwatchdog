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

import com.google.gwt.event.shared.GwtEvent;

import org.olareoun.wwd.shared.UsersDocs;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
public class DocsFetchedEvent extends GwtEvent<DocsEventsHandler> {

  public static final GwtEvent.Type<DocsEventsHandler> TYPE = new GwtEvent.Type<DocsEventsHandler>();

  private final UsersDocs usersDocs;

  public DocsFetchedEvent(UsersDocs usersDocs) {
    super();
    this.usersDocs = usersDocs;
  }

  @Override
  public GwtEvent.Type<DocsEventsHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(DocsEventsHandler handler) {
    handler.onDocsFetched(this.usersDocs);
  }

}
