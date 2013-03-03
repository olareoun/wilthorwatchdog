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
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.olareoun.wwd.client.drive.DriveService;
import org.olareoun.wwd.shared.GwtDrive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Drive GWT RPC service implementation.
 * 
 * @author Yaniv Inbar
 */
@SuppressWarnings("serial")
public class DriveGwtRpcSample extends RemoteServiceServlet implements DriveService {

  @Override
  public List<GwtDrive> getDrives() throws IOException {
    try {
      Drive client = DriveUtils.loadDriveClient();
      com.google.api.services.drive.Drive.Files.List listRequest = client.files().list();
      listRequest.setFields("items(id,summary)");
      FileList list = listRequest.execute();
      ArrayList<GwtDrive> result = new ArrayList<GwtDrive>();
      if (list.getItems() != null) {
        for (File entry : list.getItems()) {
          result.add(new GwtDrive(entry.getId(), entry.getTitle()));
        }
      }
      return result;
    } catch (IOException e) {
      throw Utils.wrappedIOException(e);
    }
  }

}
