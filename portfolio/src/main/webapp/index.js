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

const FETCH_COMMENTS_URL = '/data';
const COMMENT_CONTAINER = 'comments-container';
const COMMENT_CLASS_NAME = 'comment';
const NAME_CLASS_NAME = 'name-output';
const MESSAGE_CLASS_NAME = 'message-output';
const CLASS_TAG = 'class';
const LIST_CHILD_TAG = 'li';
const DIV_TAG = 'div';
const P_TAG = 'p';
const SMALL_TAG = 'small';


/**
 * 
 */
function bodyOnLoad(commentsLimit) {
  $(document).ready(function(){
    $('.header').height($(window).height());
  })

  particlesJS.load('particles-js', 'particles.js/particlesjs-config.json', function() {
    console.log('callback - particles.js config loaded');
  });

  fetchComments(commentsLimit);
}
/**
 * Add comments into the COMMENT_CONTAINER 
 * @param commentsLimit the number of comments to display
 */
function fetchComments(commentsLimit) {
  const parameters = {'limit': commentsLimit};
  const url = createQueryString(FETCH_COMMENTS_URL, parameters);
  
  fetch(url).then(response => response.json()).then((comments) => {
    const commentsListElement = document.getElementById(COMMENT_CONTAINER);
    
    commentsListElement.innerHTML  = "";
    comments.forEach((comment) => {
      commentsListElement.appendChild(createListElement(comment));
    })
  });
}

/**
 * Create query string from parameters
 */
function createQueryString(url, parameters) {
  const query = Object.entries(parameters)
        .map(pair => pair.map(encodeURIComponent).join('='))
        .join('&');
  return url + "?" + query;
}

/** 
 * Creates an <li> element containing text.
 */
function createListElement(text) {
  const liElement = document.createElement(LIST_CHILD_TAG);
  liElement.setAttribute(CLASS_TAG, "media");

  const bodyDiv = document.createElement(DIV_TAG); 
  bodyDiv.setAttribute(CLASS_TAG, "media-body");

  const nameElement = document.createElement(SMALL_TAG);
  nameElement.setAttribute(CLASS_TAG, "text-muted");
  nameElement.textContent = ''.concat(text.name,' says: ');

  const messageElement = document.createElement(P_TAG);
  nameElement.setAttribute(CLASS_TAG, "message-output");
  messageElement.innerText = text.message;

  liElement.appendChild(bodyDiv);
  bodyDiv.appendChild(nameElement);
  bodyDiv.appendChild(messageElement);

  return liElement;
}