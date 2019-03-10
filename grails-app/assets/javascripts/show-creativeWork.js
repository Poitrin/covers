// This code loads the IFrame Player API code asynchronously.
var tag = document.createElement('script');

tag.src = "https://www.youtube.com/iframe_api";
var firstScriptTag = document.getElementsByTagName('script')[0];
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

// This function creates an <iframe> (and YouTube player) after the API code downloads.
var player;
function onYouTubeIframeAPIReady() {
  const playerElement = document.getElementById('player');
  if (!playerElement) {
    // Player not found
    return;
  }
  const videoId = playerElement.getAttribute('data-youtube-video-id');
  if (!videoId) {
    // No YouTube Video ID found
    return;
  }
  player = new YT.Player('player', {
    height: '300px',
    width: '100%',
    host: 'https://www.youtube-nocookie.com',
    videoId,
    events: {
      // onReady: onPlayerReady,
      // onStateChange: onPlayerStateChange
    }
  });
}

// For each link with CSS class "timing": add event listener, so that
// the user can jump to the timing of the part
for (let element of document.getElementsByClassName("timing")) {
  const timing = +element.getAttribute('data-timing');

  element.addEventListener("click", () => {
    player.playVideo();
    player.seekTo(timing);
  });
};
