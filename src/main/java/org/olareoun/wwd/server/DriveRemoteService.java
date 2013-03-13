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

import com.google.api.services.drive.Drive;
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
import java.util.List;

@SuppressWarnings("serial")
public class DriveRemoteService extends RemoteServiceServlet implements DriveService {

  @Override
  public UsersDocs getDocuments(List<String> emails) throws IOException {
    try {
      UsersDocs usersDocs = new UsersDocs();
      for (String email : emails) {
        Drive driveService = DriveUtils.loadDriveClient(email);
        com.google.api.services.drive.Drive.Files.List listRequest = driveService.files().list();
        FileList list = listRequest.setQ(createQ(email)).execute();
        if (list.getItems() != null) {
          for (File entry : list.getItems()) {
            GwtDoc gwtDoc = new GwtDoc(entry.getId(), entry.getTitle(), entry.getOwnerNames(), email);
            usersDocs.add(email, gwtDoc);
          }
        }
      }
      return usersDocs;
    } catch (IOException e) {
      throw DriveUtils.wrappedIOException(e);
    }
  }

  private String createQ(String email) {
    StringBuffer buffer = new StringBuffer();
    buffer
    .append("'")
    .append(email)
    .append("'")
    .append(" in owners");
    return buffer.toString();
  }

  @Override
  public List<GwtDoc> changePermissions(List<GwtDoc> drives) throws IOException {
    for (GwtDoc drive: drives) {
      Drive driveService = DriveUtils.loadDriveClient(drive.getEmail());
      Permission newOwnerPermission = createPermission(UserServiceFactory.getUserService().getCurrentUser().getEmail());
      try {
        driveService.permissions().insert(drive.id, newOwnerPermission).execute();
      } catch (IOException e) {
        System.out.println(drive.getEmail() + " - " + drive.title);
        
        PermissionList permissionList = driveService.permissions().list(drive.id).execute();
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
    }
    return drives;
  }

  private static Permission createPermission(String newOwnerEmail) {
    Permission newPermission = new Permission();
    newPermission.setValue(newOwnerEmail);
    newPermission.setType("user");
    newPermission.setRole("owner");
    return newPermission;
  }
}
