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

import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;


/**
 * @author vmiguez@google.com (Your Name Here)
 *
 */
public class UserUtils {

  private static final String SERVICE_VERSION = "2.0";
  private static final String APPS_FEEDS_URL_BASE =
      "https://apps-apis.google.com/a/feeds/";
  private DomainUserService service;
  private String domain;
  private String domainUrlBase;

  public UserUtils(String admin, String password, String domain) throws AuthenticationException{
    this.domain = domain;
    this.domainUrlBase = APPS_FEEDS_URL_BASE + this.domain + "/";
    service = new DomainUserService("wilthorwatchdog");
    service.setUserCredentials(admin, password);
  }

  public UserFeed retrieveAllUsers()
      throws AppsForYourDomainException, ServiceException, IOException {

    URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/");
    UserFeed allUsers = new UserFeed();
    UserFeed currentPage;
    Link nextLink;

    do {
      currentPage = service.getFeed(retrieveUrl, UserFeed.class);
      allUsers.getEntries().addAll(currentPage.getEntries());
      nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
      if (nextLink != null) {
        retrieveUrl = new URL(nextLink.getHref());
      }
    } while (nextLink != null);

    return allUsers;
  }
}
