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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author maquina infernal@google.com (Your Name Here)
 *
 */
public class UsersDocsTest {

  String email = "a@b.c";
  List<GwtDoc> docs = createDocs();
  private UsersDocs usersDocs;
  

  @Test
  public void test_when_i_insert_email_and_docs_then_i_can_access_docs_by_email() {
    usersDocs = createWithOneEmail(email, docs);
    
    assertEquals(docs, usersDocs.get(email));
  }
  
  @Test
  public void test_when_i_insert_one_doc_for_an_email_it_should_be_added_to_its_docs() {
    usersDocs = new UsersDocs();
    GwtDoc doc = new GwtDoc();
    
    usersDocs.add(email, doc);
    
    assertTrue(usersDocs.get(email).contains(doc));  
  }
  
  @Test
  public void test_i_want_plain_list_of_all_documents_from_all_emails() {
    usersDocs = new UsersDocs();
    String email2 = "d@e.f";
    GwtDoc doc = new GwtDoc();
    GwtDoc doc2 = new GwtDoc();
    List<GwtDoc> docs2 = createDocs();
    List<GwtDoc> alldocs = new ArrayList<GwtDoc>();
    
    usersDocs = createWithOneEmail(email,docs);
    usersDocs.add(email, doc);
    usersDocs.put(email2, docs2);
    usersDocs.add(email2, doc2);
    
    alldocs.addAll(usersDocs.get(email));
    alldocs.addAll(usersDocs.get(email2));
    
    assertEquals(alldocs, usersDocs.getAllDocs());
    
    
  }
  
  
  @Test
  public void test_when_i_insert_another_email_then_i_can_obtain_docs_from_this_email() {
    usersDocs = createWithOneEmail(email, docs);
    String email2 = "d@e.f";
    List<GwtDoc> docs2 = createDocs();
    usersDocs.put(email2, docs2);
    assertEquals(docs2, usersDocs.get(email2));
  }

  @Test
  public void test_when_i_insert_another_email_then_i_can_obtain_docs_from_first_email(){
    usersDocs = createWithOneEmail(email, docs);
    String email2 = "d@e.f";
    List<GwtDoc> docs2 = createDocs();
    usersDocs.put(email2, docs2);
    assertEquals(docs, usersDocs.get(email));
  }

  private UsersDocs createWithOneEmail(String email, List<GwtDoc> docs) {
    UsersDocs userDocs = new UsersDocs();
    userDocs.put(email, docs);
    return userDocs;
  }
  
  private List<GwtDoc> createDocs(){
    List<GwtDoc> docs = new ArrayList<GwtDoc>();
    docs.add(new GwtDoc());
    return docs;
  }
  
  
  
  
  }
