<template>
  <div class="w-screen min-h-screen py-8 items-stretch justify-center bg-gray-100">

    <h1 class="p-2 text-4xl text-center bg-black text-white">
      GAME MASTER VIEW
    </h1>
    <div class="flex">
      <div class="w-1/2 border shadow-lg">
        <div class="bg-blue-700 text-white uppercase font-bold p-2 px-12 text-center mb-2">
          <span class="block tracking-widest text-sm">Player One</span>
          <span class="block tracking-wider text-2xl">{{ game.players[0].name }}</span>
        </div>
        <PlayerView :player="game.players[0]"/>
      </div>
      <div class="w-1/2 border shadow-lg">
        <div class="bg-green-700 text-white uppercase font-bold p-2 px-12 text-center mb-2">
          <span class="block tracking-widest text-sm">Player Two</span>
          <span class="block tracking-wider text-2xl">{{ game.players[1].name }}</span>
        </div>
        <PlayerView :player="game.players[1]"/>
      </div>
    </div>

    <div class="flex text-lg">
      <div class="shadow-lg bg-green-100 border p-4 w-1/3">
        <div>
          <p class="uppercase font-bold text-xs">Test Result Card</p>
          <div
            class="border-l-8 py-2 pl-2 pr-4 mb-1"
            style="background: #ffc499; border-color: #ffa866;"
          >
            {{ game.showingTestResultCard.title }}
          </div>
        </div>
      </div>
      <div class="shadow-lg bg-green-100 border p-4 w-1/3">
        <DeckInfo :card-deck="game.testResultCardDeck" title="Test Result Deck"/>
      </div>
      <div class="shadow-lg bg-green-100 border p-4 w-1/3">
        <DeckInfo :card-deck="game.playingCardDeck" title="Playing Deck"/>
      </div>
    </div>
  </div>
</template>

<script>
  import PlayerView from "./PlayerView";
  import DeckInfo from "./DeckInfo";

  export default {
    name: "GameMaster",
    components: {DeckInfo, PlayerView},
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
    beforeDestroy() {
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

