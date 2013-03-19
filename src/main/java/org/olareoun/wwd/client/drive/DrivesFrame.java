package org.olareoun.wwd.client.drive;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;

import org.olareoun.wwd.client.users.DocsEventsHandler;
import org.olareoun.wwd.client.users.DocsFetchedEvent;
import org.olareoun.wwd.client.users.SearchDocsEvent;
import org.olareoun.wwd.shared.GwtDoc;
import org.olareoun.wwd.shared.UsersDocs;

import java.util.ArrayList;
import java.util.List;

public class DrivesFrame extends Composite implements UsersEventsHandler, DocsEventsHandler{

  FlexTable drivesTable;

  final MainScreen mainScreen;

  private final HandlerManager mainEventBus;

  private UsersDocs usersDocs;

  protected ChangeFrame changeFrame;

  private HorizontalPanel horizontalPanel;
  
  public DrivesFrame(HandlerManager mainEventBus, MainScreen main) {
    this.mainEventBus = mainEventBus;
    this.mainEventBus.addHandler(UsersFetchedEvent.TYPE, this);
    this.mainEventBus.addHandler(SearchUsersEvent.TYPE, this);
    this.mainEventBus.addHandler(DocsFetchedEvent.TYPE, this);
    this.mainEventBus.addHandler(SearchDocsEvent.TYPE, this);
    this.mainScreen = main;
    
    this.changeFrame = new ChangeFrame(mainEventBus);
    this.changeFrame.hide();

    this.drivesTable = new FlexTable();
   
    
    this.horizontalPanel = new HorizontalPanel();

    this.horizontalPanel.add(this.drivesTable);
    this.horizontalPanel.add(changeFrame);
    
    initWidget(this.horizontalPanel);
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

  void setUsersDocs(UsersDocs usersDocs){
    this.usersDocs = usersDocs;
  }

  @Override
  public void onUsersFetched(List<String> users) {
    show();
  }

  @Override
  public void onFetching() {
    deleteDocs();
    hide();
  }

  private void deleteDocs() {
    this.drivesTable.removeAllRows();
  }

  @Override
  public void onDocsFetching() {
    this.hide();
  }

  @Override
  public void onDocsFetched(UsersDocs usersDocs) {
    this.usersDocs = usersDocs;
    this.refreshTable();
    this.changeFrame.show();
    this.show();
  }

}
