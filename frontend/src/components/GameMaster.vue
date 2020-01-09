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
        <div class="flex w-11/12 mx-auto flex-wrap">
          <div class="w-1/3 border-t-2 border-gray-500">IN PLAY</div>
          <PlayingCardsMiniView
            :cards="game.players[0].inPlay.cards"
            class="mb-4"
          />
          <div class="w-1/3 border-t-2 border-gray-500">HAND</div>
          <PlayingCardsMiniView
            :cards="game.players[0].hand.cards"
          />
        </div>
      </div>
      <div class="w-1/2 border shadow-lg">
        <div class="bg-green-700 text-white uppercase font-bold p-2 px-12 text-center mb-2">
          <span class="block tracking-widest text-sm">Player Two</span>
          <span class="block tracking-wider text-2xl">{{ game.players[1].name }}</span>
        </div>
        <div class="flex w-11/12 mx-auto flex-wrap">
          <div class="w-1/3 border-t-2 border-gray-500">IN PLAY</div>
          <PlayingCardsMiniView
            :cards="game.players[1].inPlay.cards"
            class="mb-4"
          />
          <div class="w-1/3 border-t-2 border-gray-500">HAND</div>
          <PlayingCardsMiniView
            :cards="game.players[1].hand.cards"
          />
        </div>
      </div>
    </div>

    <div class="flex text-lg">
      <div class="shadow-lg bg-green-100 border p-4 w-1/3">
        <div>
          <p>Test Result Card</p>
        </div>
      </div>
      <div class="shadow-lg bg-green-100 border p-4 w-1/3">
        <div>
          <p>Test Result Deck</p>
        </div>
      </div>
      <div class="shadow-lg bg-green-100 border p-4 w-1/3">
        <div>
          <p>Playing Card Deck</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import PlayingCardsMiniView from "./PlayingCardsMiniView";

  export default {
    name: "GameMaster",
    components: {PlayingCardsMiniView},
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

