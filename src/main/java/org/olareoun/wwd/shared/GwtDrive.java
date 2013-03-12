package org.olareoun.wwd.shared;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class GwtDrive implements Serializable {
  public String id;
  public String title;
  public List<String> ownersNames;
  public String email;

  public GwtDrive() {
  }

  public GwtDrive(String id, String title, List<String> ownersNames, String email) {
    this.id = id;
    this.title = title;
    this.ownersNames = ownersNames;
    this.email = email;
  }
  
  public String ownerNamesString(){
    StringBuffer buf = new StringBuffer();
    for (String name : ownersNames) {
      buf.append(name).append(",");
    }
    String namesString = buf.toString();
    return namesString.substring(0, namesString.length() - 1);
  }

  /**
   * @return
   */
  public String getEmail() {
    return this.email;
  }

}
