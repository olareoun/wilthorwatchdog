package org.olareoun.wwd.client.drive;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DrivesFrame extends Composite {
  interface MyUiBinder extends UiBinder<VerticalPanel, DrivesFrame> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  TextBox addTextBox;

  @UiField
  Button addButton;

  @UiField
  FlexTable drivesTable;

  final DriveGwtSample main;

  public DrivesFrame(DriveGwtSample main) {
    this.main = main;
    initWidget(uiBinder.createAndBindUi(this));
  }

}
