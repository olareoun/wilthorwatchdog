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

package org.olareoun.wwd.shared;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author maquina infernal@google.com (Your Name Here)
 *
 */
public class UsersDocs implements Serializable {

  private static final long serialVersionUID = 8729273803822367512L;
  private Map<String, List<GwtDoc>> map;

  public UsersDocs() {
    this.map = new HashMap<String, List<GwtDoc>>();
  }

  public void put(String email, List<GwtDoc> docs) {
    this.map.put(email, docs);
  }

  public List<GwtDoc> get(String email) {
    return this.map.get(email);
  }
  
  public void add(String email, GwtDoc doc) {
    List<GwtDoc> docs = this.map.get(email);
    if (docs == null){
      docs = initEmailDocs(email);
    }
    docs.add(doc);
  }
  public List<GwtDoc> getAllDocs() {
    
    Collection<List<GwtDoc>> listsdocs = this.map.values();
    Iterator<List<GwtDoc>> iterator = listsdocs.iterator();
    List<GwtDoc> alldocs = new ArrayList<GwtDoc>();
    
    
    while (iterator.hasNext()) {
      List<GwtDoc> next = iterator.next();
      alldocs.addAll(next);
    }
 
    return alldocs;
  }

  /**
   * @param email
   * @return
   */
  private List<GwtDoc> initEmailDocs(String email) {
    List<GwtDoc> docs;
    docs = new ArrayList<GwtDoc>();
    this.map.put(email, docs);
    return docs;
  }
  
}
