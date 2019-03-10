//= require vue.min
// TODO: only extract functions that are used in the application
//= require lodash.min

/**
 * Queries WikiData with a YouTube Video ID,
 * in order to find the corresponding song title and artist name(s)
 */
function fetchCreativeWorkByYoutubeVideoId(youtubeVideoId) {
  const sparqlQuery = `SELECT ?creativeWorkLabel
  (GROUP_CONCAT(DISTINCT ?performerLabel; SEPARATOR=", ") AS ?performerList)
WHERE {
  ?creativeWork wdt:P1651 "${youtubeVideoId}".
  ?creativeWork wdt:P175 ?performer.
  SERVICE wikibase:label {
    bd:serviceParam wikibase:language "[AUTO_LANGUAGE],en".
    ?performer rdfs:label ?performerLabel.
    ?creativeWork rdfs:label ?creativeWorkLabel
  }
}
GROUP BY ?creativeWork ?creativeWorkLabel
LIMIT 1`;
  const endpointUrl = 'https://query.wikidata.org/sparql';
  const fullUrl = endpointUrl + '?query=' + encodeURIComponent(sparqlQuery);
  const headers = {
    'Accept': 'application/sparql-results+json'
  };

  return fetch(fullUrl, {
      headers
    })
    .then(body => body.json())
    .then(response => {
      const hasFoundResult = response.results.bindings.length === 1;
      if (hasFoundResult) {
        const row = response.results.bindings[0];
        return {
          artist: row.performerList.value,
          title: row.creativeWorkLabel.value
        }
      }
    });
}

let app = new Vue({
  el: '#create-creativeWork',
  data: {
    artist: '',
    // artistMBID: '',
    title: '',
    /*
    recordingMBID: '',
    artists: [],
    titles: [],
    */
    youtubeVideoId: '',
    hasSearched: false,
    hasFoundResult: false,
    isLoading: false
  },
  methods: {
    fetchCreativeWorkByYoutubeVideoId: _.debounce(async function () {
      this.youtubeVideoId = this.youtubeVideoId.replace(/^.+youtube.com.watch.v=/, '')
      // See comment about min/maxSize in CreativeWork.groovy!
      const YOUTUBE_VIDEO_ID_LENGTH = 11;
      if (this.youtubeVideoId.length !== YOUTUBE_VIDEO_ID_LENGTH) {
        return;
      }
      this.isLoading = true;
      const response = await fetchCreativeWorkByYoutubeVideoId(this.youtubeVideoId)
        .catch(error => {
          console.error(error);
        });
      this.isLoading = false;
      this.hasSearched = true;
      if (this.hasFoundResult = !!response) {
        Object.assign(this, response);
      } else {
        this.artist = '';
        this.title = '';
      }
    }, 250)
  }
})