package org.olareoun.wwd.server;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.drive.Drive;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class UserDriveUtils {

  private static final List<String> SCOPES = Arrays.asList(
      "https://www.googleapis.com/auth/drive.file",
      "https://www.googleapis.com/auth/userinfo.email",
      "https://www.googleapis.com/auth/userinfo.profile");

  private static GoogleAuthorizationCodeFlow flow = null;

  private static Userinfo userInfo;
  
  public static boolean isAdmin(){
    return UserServiceFactory.getUserService().isUserAdmin();
  }

  public static String getRedirectUri(HttpServletRequest req) {
    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
    url.setRawPath("/oauth2callback");
    return url.build();
  }

  static GoogleAuthorizationCodeFlow getFlow() throws IOException {
    if (flow == null){
      flow = FlowUtils.getFlow(SCOPES);
    }
    return flow;
  }

  static Drive loadDriveClient() throws IOException {
    User currentUser = UserServiceFactory.getUserService().getCurrentUser();
    Credential credential = FlowUtils.getFlow(SCOPES).loadCredential(currentUser.getUserId());
    try {
      userInfo = getUserInfo(credential);
    } catch (NoUserIdException exception) {
      // TODO Auto-generated catch block
      exception.printStackTrace();
    }
    return new Drive.Builder(FlowUtils.HTTP_TRANSPORT, FlowUtils.JSON_FACTORY, credential).build();
  }

  static Userinfo getUserInfo(Credential credentials)
      throws NoUserIdException {
    Oauth2 userInfoService =
        new Oauth2.Builder(FlowUtils.HTTP_TRANSPORT, FlowUtils.JSON_FACTORY, credentials).build();
    Userinfo userInfo = null;
    try {
      userInfo = userInfoService.userinfo().get().execute();
    } catch (IOException e) {
      System.err.println("An error occurred: " + e);
    }
    if (userInfo != null && userInfo.getId() != null) {
      return userInfo;
    }
    throw new NoUserIdException();
  }
  
  public static String getUserEmail() throws IOException, NoUserIdException{
    reloadUserInfoIfNull();
    return userInfo.getEmail();
  }

  public static String getDomain() throws IOException, NoUserIdException {
    reloadUserInfoIfNull();
    return userInfo.getHd();
  }
  
  public static void revoke() throws IOException{
    User currentUser = UserServiceFactory.getUserService().getCurrentUser();
    Credential credential = FlowUtils.getFlow(SCOPES).loadCredential(currentUser.getUserId());
    flow.getCredentialStore().delete(currentUser.getUserId(), credential);
  }

  private static void reloadUserInfoIfNull() throws IOException, NoUserIdException {
    if (userInfo == null){
      User currentUser = UserServiceFactory.getUserService().getCurrentUser();
      Credential credential = FlowUtils.getFlow(SCOPES).loadCredential(currentUser.getUserId());
      userInfo = getUserInfo(credential);
    }
  }
}
