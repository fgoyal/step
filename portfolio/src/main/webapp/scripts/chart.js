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

window.addEventListener('load', function() {
  console.log("chart.js");
  google.charts.load('current', {'packages':['geochart']});
  google.charts.setOnLoadCallback(drawRegionsMap);
});

function drawRegionsMap() {
  var data = google.visualization.arrayToDataTable([
    ['Country', 'Year'],
    ['United States', 1999],
    ['India', 2000],
    ['Singapore', 2007],
    ['China', 2008],
    ['United Kingdom', 2009],
    ['France', 2009],
    ['Canada', 2012],
    ['Jamaica', 2016],
    ['Italy', 2018],
    ['Austria', 2018],
    ['Mexico', 2019]
  ]);

  var options = {
    colorAxis: {colors: ['#7ec8c2', '3da3a0', '#135a66']},
    backgroundColor: 'none',
    datalessRegionColor: '#fdfefe',
    defaultColor: '#fdfefe',
  };
  var chart = new google.visualization.GeoChart(document.getElementById('regions_div'));
  chart.draw(data, options);
}