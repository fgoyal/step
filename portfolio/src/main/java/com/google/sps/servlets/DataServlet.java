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
import com.google.sps.data.Comment;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that keeps a record of all comments that the server processes and sends them as a json */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  /**
   * Converts an ArrayList instance into a JSON string using the Gson library.
   */
  private static <T> String convertToJsonUsingGson(List<T> list) {
    Gson gson = new Gson();
    return gson.toJson(list);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    
    List<Comment> allComments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String name = (String) entity.getProperty("name");
      String message = (String) entity.getProperty("message");
      Comment comment = new Comment(name, message);
      allComments.add(comment);
    }

    response.setContentType("application/json;");
    String json = convertToJsonUsingGson(allComments);
    response.getWriter().println(json);
  }

  /**
   * Add form comments into datastore as a Comment Entity with name, message, and timestamp properties
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String name = getParameter(request, "name-input", "Anonymous");
    String message = getParameter(request, "message-input", "");
    long timestamp = System.currentTimeMillis();

    Entity taskEntity = new Entity("Comment");
    taskEntity.setProperty("name", name);
    taskEntity.setProperty("message", message);
    taskEntity.setProperty("timestamp", timestamp);

    datastore.put(taskEntity);

    response.sendRedirect("/index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null || value.isEmpty()) {
      return defaultValue;
    }
    return value;
  }
}
