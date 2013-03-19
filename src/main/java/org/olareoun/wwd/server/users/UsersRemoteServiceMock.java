package org.olareoun.wwd.server.users;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.olareoun.wwd.client.users.UsersService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
@SuppressWarnings("serial")
public class UsersRemoteServiceMock extends RemoteServiceServlet implements UsersService {

  @Override
  public boolean hasPermission(){
    return true;
//    return UserDriveUtils.isAdmin();
  }

  @Override
  public List<String> getUsers(String password) {
    List<String> usersEmails = new ArrayList<String>();
    usersEmails.add("pepe@navarro.es");
    usersEmails.add("juan@echanove.es");
    usersEmails.add("pepe@viyuela.es");
    usersEmails.add("marianico@elcorto.es");
    return usersEmails;
  }

}
