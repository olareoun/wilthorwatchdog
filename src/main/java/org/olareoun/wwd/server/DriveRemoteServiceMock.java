package org.olareoun.wwd.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.olareoun.wwd.client.drive.DriveService;
import org.olareoun.wwd.shared.GwtDoc;
import org.olareoun.wwd.shared.UsersDocs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DriveRemoteServiceMock extends RemoteServiceServlet implements DriveService {

  @Override
  public UsersDocs getDocuments(List<String> emails) throws IOException {
    UsersDocs usersDocs = new UsersDocs();
    for (String email : emails) {
      for (int i = 0; i < 3; i++) {
        GwtDoc gwtDoc = new GwtDoc(email + i, email + "_Doc_" + i, new ArrayList<String>(), email);
        usersDocs.add(email, gwtDoc);
      }
    }
    return usersDocs;
  }

  @Override
  public List<GwtDoc> changePermissions(String userForDocs, List<GwtDoc> drives) throws IOException {
    return drives;
  }

}
