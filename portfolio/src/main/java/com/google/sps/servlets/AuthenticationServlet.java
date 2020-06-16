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
import com.google.gson.Gson;

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

  class LoginStatus {
    private boolean loggedIn;
    private String url;
    
    public LoginStatus(boolean loggedIn, String url) {
      this.loggedIn = loggedIn;
      this.url = url;
    }
 }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    LoginStatus loginStatus;

    if (userService.isUserLoggedIn()) {
      String url = userService.createLogoutURL("/");
      loginStatus = new LoginStatus(true, url);
    } else {
      String url = userService.createLoginURL("/");
      loginStatus = new LoginStatus(false, url);
    }

    response.setContentType("application/json");
    Gson gson = new Gson();
    response.getWriter().println(gson.toJson(loginStatus));
  }
}