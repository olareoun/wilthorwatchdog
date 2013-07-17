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


import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpResponseException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.olareoun.wwd.client.drive.DriveService;
import org.olareoun.wwd.shared.GwtDoc;
import org.olareoun.wwd.shared.UsersDocs;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;



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
      System.out.println(e.getMessage());
      throw DriveUtils.wrappedIOException(e);
    }
  }
  
  @Override
  public List<GwtDoc> changePermissions(String userForDocs, UsersDocs usersDocs) throws IOException {
    Iterator<String> emailIterator = usersDocs.iterator();
    while (emailIterator.hasNext()) {
      String email = emailIterator.next();
      List<GwtDoc> emailDocs = usersDocs.get(email);
      if (!email.contentEquals(userForDocs)) {
      changeUserDocsPermissions(email, emailDocs, userForDocs);
      }
    }
    return usersDocs.getAllDocs();
  }

  private void getOwnedDocs(UsersDocs usersDocs, String email) throws IOException {
    
   // String privateId = null;
   // List<String> privateFolderIds = new ArrayList<String>();
    Drive driveService = DriveUtils.loadDriveClient(email);
    Files.List listRequest = driveService.files().list();
    
    do {
     try {  
         //FileList fileList = listRequest.execute();
         FileList fileList = getListOwnedDocs(driveService, listRequest, email);
         /*privateId = getPrivateId(fileList, driveService);
         if (privateId != null) {
           privateFolderIds = getPrivateFolderIds(driveService, privateId);
         }*/
    
         //FileList list = getListOwnedDocs(driveService, listRequest, email);  
  
         if (fileList.getItems() != null){
           for (File doc : fileList.getItems()) {
             //if (!isFileInPrivateFolder(driveService, privateFolderIds, doc.getId()))
               insertOwnedDocs(usersDocs, email, doc);
           }
         }
         listRequest.setPageToken(fileList.getNextPageToken());
     } catch (IOException e) {
       System.out.println ("An error occurred: " + e);
       listRequest.setPageToken(null);
     }
    } while (listRequest.getPageToken() != null && listRequest.getPageToken().length() > 0);
  }

  /*private static List<File> retrieveAllFiles (Drive driveService, String email) throws IOException {
    
    List<File> allFiles = new ArrayList<File>();
    Files.List request = driveService.files().list();
    do {
      try {
        FileList files = request.setQ(searchOwners(email)).execute();
        
        allFiles.addAll(files.getItems());
        request.setPageToken(files.getNextPageToken());
      } catch (IOException e) {
        System.out.println("An error occurred while retrieven files in drive: " + e);
        request.setPageToken(null);
      }
    } while (request.getPageToken() != null && request.getPageToken().length() > 0);
   
    return allFiles;
  }
  
  private List <String> getPrivateFolderIds(Drive driveService, String privateId) throws IOException {
    
    String childId = null;
    List<String> privateFolderIds = new ArrayList<String> ();  
         privateFolderIds.add(privateId);
         Children.List requestChilds = driveService.children().list(privateId);
         ChildList children = requestChilds.execute();
         for (ChildReference child : children.getItems()) {
           childId = child.getId();
           if (isFolder(driveService, childId)) {
             privateFolderIds.add(childId);
             getChildFolders(driveService, childId, privateFolderIds);
           } 
         }
    
    return privateFolderIds;
  }
  

  
  private void getChildFolders(Drive driveService, String childId, List<String> privateFolderIds) throws IOException {


    Children.List requestChilds = driveService.children().list(childId);
    ChildList children = requestChilds.execute();
    for (ChildReference child : children.getItems()) {
      childId = child.getId();
      if (isFolder(driveService, childId)) {
      privateFolderIds.add(childId);
      getChildFolders(driveService, childId, privateFolderIds);
      }
    }

  }
  
  private boolean isFolder (Drive driveService, String fileId) throws IOException {
    File file;
    String fileType;
    file = driveService.files().get(fileId).execute();
    fileType = file.getMimeType();
    if (fileType.contentEquals("application/vnd.google-apps.folder")) {
      return true;
    }
    else {return false;}
    
  }
  
  private String getPrivateId(FileList fileList, Drive driveService) throws IOException {
    int item = 0;
    boolean findPrivate=false;
    File doc = null;
    String title,privateFolderId = null;
    
    while (item < fileList.getItems().size() && !findPrivate) {
      doc = fileList.getItems().get(item);
      title = doc.getTitle();
      if (title.contentEquals("Private") && isFolder(driveService,doc.getId())) {
        privateFolderId = doc.getId();
        findPrivate = true;
      }
      item++;
    }
      return privateFolderId;
  
  }*/
  
  private void insertOwnedDocs(UsersDocs usersDocs, String email, File doc) {
    GwtDoc gwtDoc = new GwtDoc(doc.getId(), doc.getTitle(), doc.getOwnerNames(), email);
    usersDocs.add(email, gwtDoc);
  }

  /*
  private static boolean isFileInPrivateFolder(Drive driveService, List<String> privateFolderIds, String fileId) throws IOException {
    
    ParentList fileParentList = driveService.parents().list(fileId).execute();
    ParentReference parent;
    String privateFolderId;
    boolean find=false;
    
    int item = 0;

    if (!privateFolderIds.isEmpty()) {
      privateFolderId = privateFolderIds.get(0);
      if (privateFolderId.contentEquals(fileId))
        return true;
      else {
        while (item<fileParentList.getItems().size() && !find) {
          parent = fileParentList.getItems().get(item);
          
              for (String privateId: privateFolderIds) {
                if (privateId.contentEquals(parent.getId()))
                  find = true;
              }
          item++;
        }
      }
    }
        
      return find;
    
  }*/
  
  
  private FileList getListOwnedDocs(Drive driveService, Files.List listRequest,  String email) throws IOException {
    
    FileList list = listRequest.setQ(searchOwners(email)).execute();
    return list;
  }
  

  private void changeUserDocsPermissions(String email, List<GwtDoc> emailDocs, String userForDocs)
      throws IOException {
    
    Drive driveService = DriveUtils.loadDriveClient(email);
    for (GwtDoc doc: emailDocs) {
      changeDocPermission(driveService, doc, userForDocs);
     
    }
  }


  private void changeDocPermission(Drive driveService, GwtDoc doc, String userForDocs) throws IOException {

    Permission newOwnerPermission = createPermission(userForDocs);
    try {
      driveService.permissions().insert(doc.id, newOwnerPermission).execute();
    } catch (GoogleJsonResponseException e) {
      GoogleJsonError error = e.getDetails();
      System.out.println("Ensure the domain is in GoogleApps");
      System.err.println("Error code: " + error.getCode());
      System.err.println("Error message: " + error.getMessage());
    } catch (HttpResponseException e) {
      System.err.println("HTTP Status code: " + e.getStatusCode());
      System.err.println("HTTP Reason: " + e.getMessage());    
    }
    catch (IOException e) {
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

  private static String searchOwners(String email) {
    StringBuffer buffer = new StringBuffer();
    buffer
    .append("'")
    .append(email)
    .append("'")
    .append(" in owners and trashed = false");
    
    return buffer.toString();
    
  }
    

}  