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

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Servlet responsible for authenticating a user before they can comment
 */
@WebServlet("/auth-check")
public class AuthenticationServlet extends DataServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    List<String> loginStatus = new ArrayList<>();

    if (userService.isUserLoggedIn()) {
      loginStatus.add("true");
      String logoutUrl = userService.createLogoutURL("/");
      loginStatus.add(logoutUrl);
    } else {
      loginStatus.add("false");
      String loginUrl = userService.createLoginURL("/");
      loginStatus.add(loginUrl);
      System.out.println("not logged in");
    }

    response.setContentType("application/json");
    String json = DataServlet.convertToJsonUsingGson(loginStatus);
    response.getWriter().println(json);
  }
}