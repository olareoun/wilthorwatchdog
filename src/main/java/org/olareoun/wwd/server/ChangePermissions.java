/*
 * Copyright (c) 2011 Google Inc.
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

/**
 * @author maquina infernal@google.com (Your Name Here)
 *
 */

/*
 * VERSIÓN1: Desde el FS del usuario, cambia la propiedad de todos los archivos de los que es propietario.
 * */


import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;

import java.io.IOException;
//import com.google.api.services.drive.Drive.Files;


public class ChangePermissions {

  ChangePermissions (String currentuseremail, String newOwnerEmail) throws IOException
  {
    Drive service = DriveUtils.loadDriveClient(currentuseremail);
    FileList files = service.files().list().execute();
     
    
    SearchFiles (service, files, newOwnerEmail);
    
   
  }//Cons
  
  private static void SearchFiles (Drive service, FileList files, String newOwnerEmail) throws IOException
  {
    File scannedFile = null;
    int i=0;
    String fileId;
     
    
    while (i<files.getItems().size())
    {
      scannedFile = files.getItems().get(i);
      fileId = scannedFile.getId();
      ChangeFilePermissions(service,fileId, newOwnerEmail);
      i++;
    }//while files
    
  }
  
  
  private static void ChangeFilePermissions (Drive service, String fileId, String newOwnerEmail) throws IOException
  {
    
    int j=0;
    boolean isOwner=false;
    Permission newPermission = new Permission();
    Permission currentPermission = new Permission();
    PermissionList permissions;
    permissions = service.permissions().list(fileId).execute();
    
    newPermission.setValue(newOwnerEmail);
    newPermission.setType("user");
    newPermission.setRole("owner");
    
    try
    {
      while (j<permissions.getItems().size() && !isOwner)
      {
        currentPermission = permissions.getItems().get(j);
        if (currentPermission.getRole().contentEquals("owner"))
        { isOwner = true;
          service.permissions().insert(fileId, newPermission).execute(); }
        j++;
       }//while
      isOwner=false;
      j=0;
    }//try
    
    catch (IOException e)
    { System.out.println ("An error ocurred:" + e);}
 
    
    
  }
      
 }
