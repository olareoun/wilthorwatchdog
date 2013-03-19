package org.olareoun.wwd.client.drive;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.olareoun.wwd.shared.GwtDoc;
import org.olareoun.wwd.shared.UsersDocs;

import java.util.List;

public interface DriveServiceAsync {

  void getDocuments(List<String> userEmail, AsyncCallback<UsersDocs> callback);

  void changePermissions(String userForDocs, List<GwtDoc> drives, AsyncCallback<List<GwtDoc>> asyncCallback);

}
