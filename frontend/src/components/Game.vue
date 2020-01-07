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
          @click="drawTestResultsCard"
        >
          Run Tests
        </button>
        <die/>
      </div>
    </div>

    <div class="card-area h-full px-2 pt-2">

      <card-table class="opponent-in-play" :cards="game.opponentInPlay.cards" source="opponent"/>

      <card-table :cards="game.inPlay.cards" source="in-play"/>

      <card-table :cards="game.hand.cards" source="hand"/>

      <modal :showing="showTestResultsModal" @close="showTestResultsModal = false">
        <test-results-card :title="testResultsCard.title"/>
      </modal>

    </div>
  </div>
</template>

<script>
  import PlayerScore from "./PlayerScore";
  import Modal from "./Modal";
  import Die from "./Die";
  import CardTable from "./CardTable";
  import TestResultsCard from "./TestResultsCard";

  export default {
    name: "Game",
    components: {
      TestResultsCard,
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
        fetch(this.apiUrl)
          .then(response => response.json())
          .then(jsonData => this.game = jsonData)
          .catch(error => console.log('Refresh error: ' + error));
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
        fetch(this.apiUrl + '/test-result-card-draws', {
            method: 'POST'
          }
        )
          .then(response => response.json())
          .then(jsonData => this.testResultsCard = jsonData)
          .catch(error => console.log('Draw Test Results Card error: ' + error));
        this.showTestResultsModal = true;
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
        testResultsCard: {id: -1, title: "none"},
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
