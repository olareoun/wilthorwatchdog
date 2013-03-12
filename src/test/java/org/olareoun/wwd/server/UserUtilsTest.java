package org.olareoun.wwd.server;

import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;
import com.google.gdata.util.ServiceException;

import org.junit.Test;

import java.io.IOException;


/**
 * @author vmiguez@google.com (Your Name Here)
 *
 */
public class UserUtilsTest {

  @Test
  public void test() throws AppsForYourDomainException, ServiceException, IOException {
    UserUtils userUtils = new UserUtils("moore@ideasbrillantes.org", "olareoun", "ideasbrillantes.org");
    UserFeed users = userUtils.retrieveAllUsers();
    for (UserEntry entry: users.getEntries()) {
      System.out.println(entry.getId());
      System.out.println(entry.getName().getGivenName());
    }
  }

}
