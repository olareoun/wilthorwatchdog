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

package org.olareoun.wwd.client.drive;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.olareoun.wwd.shared.GwtDrive;

import java.util.List;

/**
 * Async interface for GWT RPC service for calendars.
 * 
 * @author Yaniv Inbar
 */
public interface DriveServiceAsync {

  void getDrives(String userEmail, AsyncCallback<List<GwtDrive>> callback);

}
