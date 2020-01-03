<template>
  <div class="game main h-screen">

    <div class="score-info h-full">
      <div class="bg-green-700">
        <player-score :name="game.opponentName"/>
      </div>
      <div class="bg-gray-800">
        <player-score :name="game.name"/>
      </div>
      <div class="text-center bg-black py-3">
        <button
          class="bg-blue-300 rounded border-blue-100 font-bold px-4 py-2 mb-4"
          @click="drawCard"
          :disabled="handIsFull"
          :class="{ 'opacity-50 cursor-not-allowed' : handIsFull }"
        >
          Draw Card
        </button>
        <button
          class="bg-green-300 rounded border-green-100 font-bold px-4 py-2 mb-4"
          @click="showTestResultsModal = true"
        >
          Run Tests
        </button>
        <die></die>
      </div>
    </div>

    <div class="card-area h-full px-2 pt-2">

      <card-table class="opponent-in-play" :cards="game.opponentInPlay.cards" source="opponent"/>

      <card-table :cards="game.inPlay.cards" source="in-play"/>

      <card-table :cards="game.hand.cards" source="hand"/>

      <modal :showing="showTestResultsModal" @close="showTestResultsModal = false">
        <div class="md:w-56 px-2 py-3 rounded overflow-hidden border shadow-md my-2" style="background-color: #ffa866">
          <div class="font-bold text-xl smallcaps mb-2">test results</div>
          <p class="text-black font-bold text-base mb-2">
            <code>&gt;= 2 </code>
            <span class="card-title">code smaller</span>
            cards<br/>As Predicted
          </p>
          <p class="text-gray-800 text-sm px-1">
            The tests ran and matched your prediction.
          </p>
          <p class="text-black font-bold text-base my-2">
            <code>&lt;= 1 </code>
            <span class="card-title">code smaller</span>
            cards<br/>Unexpected
          </p>
          <p class="text-gray-800 text-sm px-1">
            The tests did <strong>not</strong> match your prediction. Try again.
          </p>
        </div>

        <!--      <div class="md:w-56 p-1 rounded overflow-hidden border shadow-md mr-3 mb-4" style="background-color: #ffa866">-->
        <!--        <img class="w-full" src="https://placedog.net/300/100?random&5">-->
        <!--        <div class="px-2 py-2">-->
        <!--          <div class="font-bold text-lg smallcaps mb-2">Test Results</div>-->
        <!--          <p class="text-black text-sm mb-2">-->
        <!--            As Predicted.-->
        <!--          </p>-->
        <!--          <p class="text-gray-700 text-sm">-->
        <!--            The tests ran and matched your prediction.-->
        <!--          </p>-->
        <!--        </div>-->
        <!--      </div>-->
      </modal>

      <div class="hidden">

        <div class="md:w-56 p-1 rounded overflow-hidden border shadow-md mr-3 mb-4" style="background-color: #ffa866">
          <!--        <img class="w-full" src="https://placedog.net/300/100?random&6">-->
          <div class="px-2 py-2">
            <div class="font-bold text-lg smallcaps mb-2">Test Results</div>
            <p class="text-black text-base mb-2">
              1 or more Code Smaller cards: As Predicted
            </p>
            <p class="text-gray-800 text-sm">
              The tests ran and matched your prediction.
            </p>
            <hr/>
            <p class="text-black text-base mb-2">
              0 Code Smaller cards: Unexpected
            </p>
            <p class="text-gray-800 text-sm">
              The tests did <strong>not</strong> match your prediction.
            </p>
          </div>
        </div>

      </div>

    </div>

  </div>
</template>

<script>
  import PlayerScore from "./PlayerScore";
  import Modal from "./Modal";
  import Die from "./Die";
  import CardTable from "./CardTable";

  export default {
    name: "Game",
    components: {
      CardTable,
      PlayerScore,
      Modal,
      Die
    },
    computed: {
      handIsFull() {
        return this.game.hand.cards.length === 5;
      }
    },
    methods: {
      refresh() {
        console.log('Getting game for player: ' + this.playerId + ' at ' + this.apiUrl);
        fetch(this.apiUrl)
          .then((response) => response.json())
          .then((jsonData) => {
            this.game = jsonData;
          })
          .catch((error) => console.log(' Refresh error: ' + error));
      },
      drawCard() {
        fetch(this.apiUrl + '/actions', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({action: 'DRAW_CARD'})
          }
        );

        this.refresh();
      },
      drawTestResultsCard() {
        fetch(this.apiUrl + '/actions', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({action: 'TEST_RESULTS'})
          }
        )
          .then(response => response.json())
          .then(jsonData => this.testResultsCard = jsonData)
          .catch();
        // TODO: show the test result card in the modal
      },
      playerChanged() {
        this.playerId = this.$route.params.playerId;
        this.apiUrl = '/api/game/players/' + this.playerId;
        this.refresh();
      }
    },
    created() {
      this.playerChanged();

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
        showTestResultsModal: false,
        playerId: 'no player',
        apiUrl: '/api/game/players/',
        game: {
          "name": "",
          "opponentName": "",
          "hand": {"cards": []},
          "inPlay": {"cards": []},
          "opponentInPlay": {"cards": []}
        }
      }
    }
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .main {
    display: grid;
    grid-template-columns: 10rem auto;
  }

  .score-info {
    display: grid;
    grid-template-rows: 37% 33% 30%;
  }

  .card-area {
    display: grid;
    grid-template-rows: repeat(3, 1fr);
  }

  .opponent-in-play {
    background-color: gainsboro;
    padding: 4px 6px;
  }
</style>
