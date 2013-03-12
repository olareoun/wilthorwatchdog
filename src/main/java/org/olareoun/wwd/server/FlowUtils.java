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

import com.google.api.client.extensions.appengine.auth.oauth2.AppEngineCredentialStore;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.common.base.Preconditions;

import java.io.IOException;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
public class FlowUtils {
  
  /** Global instance of the HTTP transport. */
  static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport();

  /** Global instance of the JSON factory. */
  static final JsonFactory JSON_FACTORY = new JacksonFactory();

  private static GoogleClientSecrets clientSecrets = null;

  static GoogleAuthorizationCodeFlow getFlow(Iterable<String> scopes) throws IOException{
    return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        getClientSecrets(), scopes).setCredentialStore(
            new AppEngineCredentialStore()).setAccessType("offline").build();
  }

  static GoogleClientSecrets getClientSecrets() throws IOException {
    if (clientSecrets == null) {
      clientSecrets = GoogleClientSecrets.load(
          JSON_FACTORY, FlowUtils.class.getResourceAsStream("/client_secrets.json"));
      Preconditions.checkArgument(!clientSecrets.getDetails().getClientId().startsWith("Enter ")
          && !clientSecrets.getDetails().getClientSecret().startsWith("Enter "),
          "Download client_secrets.json file from https://code.google.com/apis/console/?api=calendar "
              + "into wilthorwatchdog/src/main/resources/client_secrets.json");
    }
    return clientSecrets;
  }

}
