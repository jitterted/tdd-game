<template>

</template>

<script>
  export default {
    name: "GameMaster",
    methods: {
      refresh() {
        fetch(this.apiUrl)
          .then(response => response.json())
          .then(jsonData => this.game = jsonData)
          .catch(error => console.log('Refresh error: ' + error));
      }
    },
    created() {
      // set refresh to happen every 1 second
      this.interval = setInterval(function () {
          this.refresh();
        }.bind(this),
        1000);
    },
    beforeDestroy(){
      clearInterval(this.interval);
    },
    data() {
      return {
        interval: undefined,
        apiUrl: '/api/game/',
        game: {
          "players": [{
            "name": "nobody",
            "hand": {"cards": []},
            "inPlay": {"cards": []}
          }, {
            "name": "nobody",
            "hand": {"cards": []},
            "inPlay": {"cards": []}
          }],
          "showingTestResultCard": {"id": 0, "title": "none"},
          "playingCardDeck": {"discardPile": 0, "drawPile": 0},
          "testResultCardDeck": {"discardPile": 0, "drawPile": 0}
        }
      }
    }

  }

</script>

<style scoped>

</style>
