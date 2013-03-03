/*
 * Copyright (c) 2010 Google Inc.
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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

import org.olareoun.wwd.shared.GwtDrive;

/**
 * Buttons for a calendar.
 * 
 * @author Yaniv Inbar
 */
public class DriveButtons extends Composite {
  interface MyUiBinder extends UiBinder<HorizontalPanel, DriveButtons> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  Button deleteButton;

  @UiField
  Button updateButton;

  private final int calendarIndex;

  private final DriveGwtSample main;

  private final GwtDrive drive;

  public DriveButtons(DriveGwtSample main, GwtDrive drive, int calendarIndex) {
    this.main = main;
    this.drive = drive;
    this.calendarIndex = calendarIndex;
    initWidget(uiBinder.createAndBindUi(this));
  }

}
