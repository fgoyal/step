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

const COMMENTS_NO_ACCESS = 'comment-no-access';
const FORM_INPUTS = "form-inputs";
const COMMENT_SUBMIT_BTN = "comment-submit";
const LOGIN_BTN = "login-status-btn";
const LOGIN_LINK = "login-link";
const DISPLAY_SHOW = "inline";
const DISPLAY_HIDE = "none";

window.addEventListener('load', function() {
  console.log("authentication.js");
  fetchLogInStatus();
});

function fetchLogInStatus() {
  fetch("/auth-check").then(response => response.json()).then((loginStatus) => {
    var link = document.getElementById(LOGIN_LINK);

    if (loginStatus[0] == "true") {
      console.log("logged in");
      changeElementDisplay(FORM_INPUTS, DISPLAY_SHOW);
      changeElementDisplay(COMMENT_SUBMIT_BTN, DISPLAY_SHOW);
      changeElementDisplay(COMMENTS_NO_ACCESS, DISPLAY_HIDE);
      link.href = loginStatus[1];
      link.innerText = "Log out";
    } else {
      console.log("logged out");
      changeElementDisplay(FORM_INPUTS, DISPLAY_HIDE);
      changeElementDisplay(COMMENT_SUBMIT_BTN, DISPLAY_HIDE);
      changeElementDisplay(COMMENTS_NO_ACCESS, DISPLAY_SHOW);
      link.href = loginStatus[1];
      link.innerText = "Log In";
    }
  });
}

function changeElementDisplay(elemName, display) {
  var elem = document.getElementById(elemName);
  elem.style.display = display;
}