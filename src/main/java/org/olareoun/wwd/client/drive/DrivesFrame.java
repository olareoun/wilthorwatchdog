package org.olareoun.wwd.client.drive;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.olareoun.wwd.shared.GwtDoc;
import org.olareoun.wwd.shared.UsersDocs;

import java.util.ArrayList;
import java.util.List;

public class DrivesFrame extends Composite {
  interface MyUiBinder extends UiBinder<VerticalPanel, DrivesFrame> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  Button addButton;

  @UiField
  FlexTable drivesTable;

  @UiField
  Button changeButton;

  final MainScreen mainScreen;

  private UsersDocs usersDocs;
  
  public DrivesFrame(MainScreen main) {
    this.mainScreen = main;
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("addButton")
  void handleAdd(ClickEvent e) {
    MainScreen.SERVICE.getDocuments(mainScreen.getUsers(), new AsyncCallback<UsersDocs>() {

      @Override
      public void onFailure(Throwable caught) {
        MainScreen.handleFailure(caught);
      }

      @Override
      public void onSuccess(UsersDocs usersDocs) {
        setDrives(usersDocs);
        refreshTable();
      }
    });
  }

  @UiHandler("changeButton")
  void handleChange(ClickEvent e) {
    MainScreen.SERVICE.changePermissions(this.usersDocs.getAllDocs(), new AsyncCallback<List<GwtDoc>>() {

      @Override
      public void onFailure(Throwable caught) {
//        MainScreen.handleFailure(caught);
      }

      @Override
      public void onSuccess(List<GwtDoc> aDrives) {
//        setDrives(aDrives);
//        refreshTable();
      }
    });
  }

  void refreshTable() {
    List<GwtDoc> alldocs = new ArrayList<GwtDoc>();
    drivesTable.removeAllRows();
    drivesTable.setText(0, 1, "Drive Title");
    drivesTable.setText(0, 2, "Drive Owners");
    drivesTable.getCellFormatter().addStyleName(0, 1, "methodsHeaderRow");
    drivesTable.getCellFormatter().addStyleName(0, 2, "methodsHeaderRow");
    
    alldocs = usersDocs.getAllDocs();
    
    for (int i = 0; i < alldocs.size(); i++) {
      GwtDoc doc = alldocs.get(i);
      drivesTable.setText(i + 1, 1, doc.title);
      drivesTable.setText(i + 1, 2, doc.ownerNamesString());
    }
  }

  public void hide(){
    this.setVisible(false);
  }

  public void show(){
    this.setVisible(true);
  }

  void setDrives(UsersDocs usersDocs){
    this.usersDocs = usersDocs;
  }

}
