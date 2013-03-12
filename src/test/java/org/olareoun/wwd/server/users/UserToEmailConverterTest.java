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

package org.olareoun.wwd.server.users;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author victor@google.com (Your Name Here)
 *
 */
public class UserToEmailConverterTest {

  @Test
  public void test() {
    String user = "https://apps-apis.google.com/a/feeds/ideasbrillantes.org/user/2.0/juanito";
    String domain = "tikitaka.com";
    assertEquals("juanito@tikitaka.com", UserToEmailConverter.convert(user, domain));
  }

}
