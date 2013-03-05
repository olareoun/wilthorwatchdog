package org.olareoun.wwd.client.drive;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.olareoun.wwd.shared.DriveAbout;

public class AboutFrame extends Composite {
  interface MyUiBinder extends UiBinder<VerticalPanel, AboutFrame> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  Button addButton;

  @UiField
  TextBox nameField;

  @UiField
  TextBox rootFolderIdField;


  final DriveGwtSample main;

  public AboutFrame(DriveGwtSample main) {
    this.main = main;
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("addButton")
  void handleAdd(ClickEvent e) {
    DriveGwtSample.SERVICE.getAbout(new AsyncCallback<DriveAbout>() {

      @Override
      public void onFailure(Throwable caught) {
        DriveGwtSample.handleFailure(caught);
      }

      @Override
      public void onSuccess(DriveAbout result) {
        main.setAbout(result);
        main.refreshAbout();
      }
    });
  }
}
