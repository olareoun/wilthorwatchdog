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

import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.olareoun.wwd.client.users.UsersService;
import org.olareoun.wwd.server.NoUserIdException;
import org.olareoun.wwd.server.UserDriveUtils;
import org.olareoun.wwd.server.UserUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
@SuppressWarnings("serial")
public class UsersRemoteService extends RemoteServiceServlet implements UsersService {

  @Override
  public boolean hasPermission(){
    return true;
//    return UserDriveUtils.isAdmin();
  }

  @Override
  public List<String> getUsers(String password) {
    List<String> usersEmails = new ArrayList<String>();
    try {
      String userEmail = UserDriveUtils.getUserEmail();
      String domain = UserDriveUtils.getDomain();
      UserUtils userUtils = new UserUtils(userEmail, password, domain);
      UserFeed users = userUtils.retrieveAllUsers();
      for (UserEntry entry: users.getEntries()) {
        usersEmails.add(UserToEmailConverter.convert(entry.getId(), domain));
      }
    } catch (AuthenticationException exception) {
      exception.printStackTrace();
    } catch (IOException exception) {
      exception.printStackTrace();
    } catch (NoUserIdException exception) {
    } catch (AppsForYourDomainException exception) {
      exception.printStackTrace();
    } catch (ServiceException exception) {
      exception.printStackTrace();
    }
    return usersEmails;
  }

}
