package org.olareoun.wwd.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GwtDrive implements Serializable {
  public GwtDrive() {
  }

  public GwtDrive(String id, String title) {
    this.id = id;
    this.title = title;
  }

  public String id;
  public String title;
}
