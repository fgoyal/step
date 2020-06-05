// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * Servlet responsible for deleting comment data
 * Extends DataServlet to keep Entity type
 */
@WebServlet("/delete-comment")
public class DeleteCommentServlet extends DataServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(COMMENT_ENTITY);
    PreparedQuery results = datastore.prepare(query);
    
    for (Entity entity : results.asIterable()) {
      deleteEntity(entity.getKey());
    }
    response.sendRedirect(REDIRECT_URL_HOME);
  }

  /**
   * Delete an entity from the datastore
   * @param key the key of the Entity to delete
   */
  protected void deleteEntity(Key key) {
    datastore.delete(key);
  }
}