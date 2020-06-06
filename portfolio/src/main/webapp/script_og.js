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
const LIST_CHILD_TAG = 'li';
const SPAN_TAG = 'span';

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
  liElement.className = COMMENT_CLASS_NAME;

  const nameElement = document.createElement(SPAN_TAG);
  nameElement.className = NAME_CLASS_NAME;
  nameElement.innerText = text.name;

  const messageElement = document.createElement(SPAN_TAG);
  messageElement.className = MESSAGE_CLASS_NAME;
  messageElement.innerText = text.message;

  liElement.appendChild(nameElement);
  liElement.appendChild(messageElement);
  return liElement;
}
