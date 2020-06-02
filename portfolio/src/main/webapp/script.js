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

/**
 * Adds a random greeting to the page.
 */

// ---------------- ADD RANDOM GREETING ---------------- //
function addRandomGreeting() {
  const greetings =
      ['I have a dog called Cheeku', 'I am a huge Harry Potter fan'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function fetchData() {
  fetch("/data").then(response => response.text()).then((quote) => {
    document.getElementById('fetch-container').innerText = quote;
  });
  
  fetch('/data').then(response => response.json()).then((messages) => {
    console.log(messages);
  });

}