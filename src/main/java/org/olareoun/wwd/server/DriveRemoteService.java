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

package org.olareoun.wwd.server;


import com.google.api.client.http.HttpResponseException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.olareoun.wwd.client.drive.DriveService;
import org.olareoun.wwd.shared.GwtDoc;
import org.olareoun.wwd.shared.UsersDocs;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
//import com.google.api.services.drive.Drive.Files.List;


@SuppressWarnings("serial")
public class DriveRemoteService extends RemoteServiceServlet implements DriveService {

  @Override
  public UsersDocs getDocuments(List<String> emails) throws IOException {
    try {
      UsersDocs usersDocs = new UsersDocs();
      for (String email : emails) {
        getOwnedDocs(usersDocs, email);
      }
      return usersDocs;
    } catch (IOException e) {
      throw DriveUtils.wrappedIOException(e);
    }
  }

  private void getOwnedDocs(UsersDocs usersDocs, String email) throws IOException {
    Drive driveService = DriveUtils.loadDriveClient(email);
    Files.List listRequest = driveService.files().list();
    String privateFolderId = getPrivateFolderId(listRequest);
    FileList list = getListOwnedDocs(driveService, listRequest, email);
    
    
  
    if (list.getItems() != null){
      for (File doc : list.getItems()) {
        if (isFileInPrivateFolder(driveService, privateFolderId, doc.getId()) == false)
        insertOwnedDocs(usersDocs, email, doc);
      }
    }
  }

  private String getPrivateFolderId(Files.List listRequest) throws IOException {
    
    File doc = null;
    String title, folderId = null;
    boolean findPrivate = false;
    int i=0;
    FileList fileList;
    
    fileList = listRequest.execute();
    while (i < fileList.getItems().size() && !findPrivate) {
      doc = fileList.getItems().get(i);
      title = doc.getTitle();
      if (title.contentEquals("Private")) {
        folderId = doc.getId();
        findPrivate = true;
      }
      i++;
    }
      return folderId;
  }
  
  private void insertOwnedDocs(UsersDocs usersDocs, String email, File doc) {
    GwtDoc gwtDoc = new GwtDoc(doc.getId(), doc.getTitle(), doc.getOwnerNames(), email);
    usersDocs.add(email, gwtDoc);
  }

  private static boolean isFileInPrivateFolder(Drive driveService, String privateFolderId, String fileId) throws IOException {
    
    if (privateFolderId == null) {
      return false;
    }
    else if (fileId.contentEquals(privateFolderId)) {  
      return true;
    }
    else 
    {
     try {
     driveService.parents().get(fileId,privateFolderId).execute();
     } catch (HttpResponseException e) {
      if (e.getStatusCode() == 404) {
        return false;
      } else {
        System.out.println("An error ocurred:" + e);
        throw e;
      }
      
     } catch (IOException e) {
      System.out.println("An error ocurred: " + e);
      throw e;
     }
    
    return true;
    }
  }
  
  
  private FileList getListOwnedDocs(Drive driveService, Files.List listRequest, String email) throws IOException {
    
    FileList list = listRequest.setQ(searchOwners(email)).execute();
    System.out.println(listRequest.toString());
    return list;
  }
  
  



  @Override
  public List<GwtDoc> changePermissions(UsersDocs usersDocs) throws IOException {
    Iterator<String> emailIterator = usersDocs.iterator();
    while (emailIterator.hasNext()) {
      String email = emailIterator.next();
      List<GwtDoc> emailDocs = usersDocs.get(email);
      changeUserDocsPermissions(email, emailDocs);
    }
    return usersDocs.getAllDocs();
  }


  private void changeUserDocsPermissions(String email, List<GwtDoc> emailDocs)
      throws IOException {
    
    Drive driveService = DriveUtils.loadDriveClient(email);
    for (GwtDoc doc: emailDocs) {
      changeDocPermission(driveService, doc);
    }
  }


  private void changeDocPermission(Drive driveService, GwtDoc doc) throws IOException {
    Permission newOwnerPermission = createPermission(UserServiceFactory.getUserService().getCurrentUser().getEmail());
    try {
      driveService.permissions().insert(doc.id, newOwnerPermission).execute();
    } catch (IOException e) {
      handleException(driveService, doc);
    }
  }


  private void handleException(Drive driveService, GwtDoc doc) throws IOException {
    System.out.println(doc.getEmail() + " - " + doc.title);

    PermissionList permissionList = driveService.permissions().list(doc.id).execute();
    for (Permission permission : permissionList.getItems()) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(permission.getName())
      .append(" - ")
      .append(permission.getRole())
      .append(" - ")
      .append(permission.getType())
      .append(" - ")
      .append(permission.getValue())
      ;
      System.out.println(buffer.toString());
    }
  }

  private static Permission createPermission(String newOwnerEmail) {
    Permission newPermission = new Permission();
    newPermission.setValue(newOwnerEmail);
    newPermission.setType("user");
    newPermission.setRole("owner");
    return newPermission;
  }

  private String searchOwners(String email) {
    StringBuffer buffer = new StringBuffer();
    buffer
    .append("'")
    .append(email)
    .append("'")
    .append(" in owners");
    return buffer.toString();
  }
    

}  