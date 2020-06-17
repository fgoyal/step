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
    private String loginURL;
    private String logoutURL;
    
    public LoginStatus(boolean loggedIn) {
      this.loggedIn = loggedIn;
      this.loginURL = userService.createLoginURL(REDIRECT_URL_HOME);
      this.logoutURL = userService.createLogoutURL(REDIRECT_URL_HOME);
    }

    public void setLoggedIn(boolean loggedIn) {
      this.loggedIn = loggedIn;
    }
  }
  
  // create class variables so we don't have to create a URL every request
  private UserService userService = UserServiceFactory.getUserService();
  private LoginStatus loginStatus = new LoginStatus(userService.isUserLoggedIn());
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(JSON_CONTENT_TYPE);
    loginStatus.setLoggedIn(userService.isUserLoggedIn());

    Gson gson = new Gson();
    response.getWriter().println(gson.toJson(loginStatus));
  }
}