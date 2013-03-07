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

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility class for JDO persistence, OAuth flow helpers, and others.
 *
 * @author Yaniv Inbar
 */
class DriveUtils {

  /**
   * 
   */
  private static final String P12_PATH = "/8463127e5b4fb444c1e47389f1b6ce1b57d0f83b-privatekey.p12";

  /**
   * 
   */
  private static final String CLIENT_SERVICE_EMAIL =
      "121078344609-ubnbhq4fthfvq2t6fls53lnnt847cl5n@developer.gserviceaccount.com";

  static String getRedirectUri(HttpServletRequest req) {
    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
    url.setRawPath("/oauth2callback");
    return url.build();
  }

  static GoogleAuthorizationCodeFlow getFlow() throws IOException {
    return FlowUtils.getFlow(Collections.singleton(DriveScopes.DRIVE));
  }

  /**
   * @return
   * @throws IOException 
   * @throws FileNotFoundException 
   */
  static Drive loadDriveClient() throws IOException {
    User currentUser = UserServiceFactory.getUserService().getCurrentUser();
    String userEmail = currentUser.getEmail();
    return loadDriveClientForEmail(currentUser, userEmail);
  }

  static Drive loadDriveClient(String userEmail) throws IOException {
    User currentUser = UserServiceFactory.getUserService().getCurrentUser();
    return loadDriveClientForEmail(currentUser, userEmail);
  }

  /**
   * @param currentUser
   * @param userEmail
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  private static Drive loadDriveClientForEmail(User currentUser, String userEmail)
      throws IOException {
    try {
      File p12File = getKeyFile();
      GoogleCredential credential = new GoogleCredential.Builder()
      .setTransport(FlowUtils.HTTP_TRANSPORT)
      .setJsonFactory(FlowUtils.JSON_FACTORY)
      .setServiceAccountId(CLIENT_SERVICE_EMAIL)
      .setServiceAccountScopes(DriveScopes.DRIVE)
      .setServiceAccountUser(userEmail)
      .setServiceAccountPrivateKeyFromP12File(
          p12File)
          .build();
      return new Drive.Builder(FlowUtils.HTTP_TRANSPORT, FlowUtils.JSON_FACTORY, null)
      .setHttpRequestInitializer(credential).build();
    } catch (GeneralSecurityException exception) {
      return handleException(currentUser, exception);
    } catch (URISyntaxException exception) {
      return handleException(currentUser, exception);
    }
  }
  
  /**
   * @param currentUser
   * @param exception
   * @return
   * @throws IOException
   */
  private static Drive handleException(User currentUser, Exception exception)
      throws IOException {
    exception.printStackTrace();
    String userId = currentUser.getUserId();
    Credential credential = getFlow().loadCredential(userId);
    return new Drive.Builder(FlowUtils.HTTP_TRANSPORT, FlowUtils.JSON_FACTORY, credential).build();
  }

  /**
   * @return
   * @throws URISyntaxException
   * @throws FileNotFoundException
   * @throws IOException
   */
  private static File getKeyFile() throws URISyntaxException, FileNotFoundException, IOException {
    URL systemResource = DriveUtils.class.getResource(P12_PATH);
    File p12File = new File(systemResource.toURI());
    return p12File;
  }

  /**
   * Returns an {@link IOException} (but not a subclass) in order to work around restrictive GWT
   * serialization policy.
   */
  static IOException wrappedIOException(IOException e) {
    if (e.getClass() == IOException.class) {
      return e;
    }
    return new IOException(e.getMessage());
  }

  private DriveUtils() {
  }

}
