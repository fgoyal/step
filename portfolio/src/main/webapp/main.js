$(document).ready(function(){
  $('.header').height($(window).height());
})

particlesJS.load('particles-js', 'particles.js/particlesjs-config.json', function() {
  console.log('callback - particles.js config loaded');
});