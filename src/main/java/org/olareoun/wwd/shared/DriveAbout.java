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

package org.olareoun.wwd.shared;

import java.io.Serializable;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
@SuppressWarnings("serial")
public class DriveAbout implements Serializable {
  
  public String name;
  public String rootFolderId;

  /**
   * @param name
   * @param rootFolderId
   */
  public DriveAbout(String name, String rootFolderId) {
    super();
    this.name = name;
    this.rootFolderId = rootFolderId;
  }
  /**
   * 
   */
  public DriveAbout() {
    super();
    // TODO Auto-generated constructor stub
  }

}
