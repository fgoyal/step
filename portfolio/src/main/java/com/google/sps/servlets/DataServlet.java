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
import com.google.sps.utils.Comment;
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
import com.google.appengine.api.datastore.FetchOptions;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Servlet that keeps a record of all comments that the server processes and sends them as a json */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  protected DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  
  protected final String REDIRECT_URL_HOME = "/";
  protected final String JSON_CONTENT_TYPE = "application/json;";
  protected final String COMMENT_ENTITY = "Comment";
  protected final String COMMENT_LIMIT = "limit";

  protected final String COMMENT_NAME = "name";
  protected final String COMMENT_EMAIL = "email";
  protected final String COMMENT_MESSAGE = "message";
  protected final String COMMENT_TIMESTAMP = "timestamp";

  protected final String FORM_INPUT_NAME = "name-input";
  protected final String FORM_INPUT_MESSAGE = "message-input";

  protected final String DEFAULT_NAME = "Anonymous";
  protected final String DEFAULT_EMAIL = "Guest";
  protected final String DEFAULT_MESSAGE = null;

  /**
   * Converts an ArrayList instance into a JSON string using the Gson library.
   */
  public static <T> String convertToJsonUsingGson(List<T> list) {
    Gson gson = new Gson();
    return gson.toJson(list);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int limit = Integer.parseInt(request.getParameter(COMMENT_LIMIT));
    Query query = new Query(COMMENT_ENTITY).addSort(COMMENT_TIMESTAMP, SortDirection.DESCENDING);
    // PreparedQuery results = datastore.prepare(query);
    List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(limit));
    List<Comment> allComments = new ArrayList<>();
    
    for (Entity entity : entities) {
      String name = (String) entity.getProperty(COMMENT_NAME);
      String email = (String) entity.getProperty(COMMENT_EMAIL);
      String message = (String) entity.getProperty(COMMENT_MESSAGE);
      long timestamp = (long) entity.getProperty(COMMENT_TIMESTAMP);
      Comment comment = new Comment(name, email, message, timestamp);
      allComments.add(comment);
    }

    response.setContentType(JSON_CONTENT_TYPE);
    String json = convertToJsonUsingGson(allComments);
    response.getWriter().println(json);
  }

  /**
   * Add form comments into datastore as a Comment Entity with name, message, and timestamp properties
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    // get Comment parameters
    String default_name;
    String email;

    if (userService.isUserLoggedIn()) {
      default_name = userService.getCurrentUser().getNickname();
      email = userService.getCurrentUser().getEmail();
    } else {
      default_name = DEFAULT_NAME;
      email = DEFAULT_EMAIL;
    }

    // Get the input from the form.
    String name = getParameter(request, FORM_INPUT_NAME, default_name);
    String message = getParameter(request, FORM_INPUT_MESSAGE, DEFAULT_MESSAGE);
    long timestamp = System.currentTimeMillis();

    if (message == null || message.isEmpty()) {
      response.sendRedirect(REDIRECT_URL_HOME);
      return;
    }

    Entity taskEntity = new Entity(COMMENT_ENTITY);
    taskEntity.setProperty(COMMENT_NAME, name);
    taskEntity.setProperty(COMMENT_EMAIL, email);
    taskEntity.setProperty(COMMENT_MESSAGE, message);
    taskEntity.setProperty(COMMENT_TIMESTAMP, timestamp);

    datastore.put(taskEntity);

    response.sendRedirect(REDIRECT_URL_HOME);
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
