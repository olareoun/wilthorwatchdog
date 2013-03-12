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

package org.olareoun.wwd.server.users;

import java.util.ArrayList;
import java.util.List;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
public class UserToEmailConverter {
  
  public static List<String> convert(List<String> users, String domain){
    List<String> emails = new ArrayList<String>();
    for (String user : users) {
      String email = convert(user, domain);
      emails.add(email);
    }
    return emails;
  }

  public static String convert(String user, String domain) {
    String userId = user.substring(user.lastIndexOf("/") + 1);
    StringBuffer buf = new StringBuffer();
    String email = buf.append(userId).append("@").append(domain).toString();
    return email;
  }

}
