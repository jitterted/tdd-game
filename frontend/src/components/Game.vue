<template>
  <div class="game main h-screen">

    <div class="score-info h-full">
      <div class="bg-green-700">
        <player-score :name="game.opponent.name" :playerId="game.opponent.id"/>
      </div>
      <div class="bg-gray-800">
        <player-score :name="game.player.name" :playerId="game.player.id"/>
      </div>
      <div class="text-center bg-black py-3">
        <button
          class="bg-blue-300 rounded border-blue-100 font-bold px-2 py-2 mx-2 mb-4"
          @click="drawToFullHand"
          :disabled="handIsFull"
          :class="{ 'opacity-50 cursor-not-allowed' : handIsFull }"
        >
          Draw To Full Hand
        </button>
        <button
          class="bg-green-300 rounded border-green-100 font-bold px-4 py-2 mb-4"
          @click="drawTestResultsCard"
        >
          Draw<br/><span class="card-title">run tests</span><br/>Card
        </button>
        <die/>
      </div>
    </div>

    <div class="card-area h-full px-2 pt-2">

      <cards-row class="opponent-in-play" :cards="game.opponentInPlay.cards" source="opponent"/>

      <cards-row :cards="game.inPlay.cards" source="in-play"/>

      <cards-row :cards="game.hand.cards" source="hand"/>

      <modal
        :showing="showTestResultsModal"
        :allowClose="testResultCardDrawnEvent.playerId === game.player.id"
        @close="discardTestResultsCard"
      >
        <test-results-card :title="testResultCardDrawnEvent.testResultCardView.title"/>
      </modal>

    </div>
  </div>
</template>

<script lang="ts">
  import PlayerScore from "./PlayerScore.vue";
  import Modal from "./Modal.vue";
  import Die from "./Die.vue";
  import TestResultsCard from "./TestResultsCard.vue";
  import CardsRow from "./CardsRow.vue";
  import {Component, Vue} from "vue-property-decorator";
  import StompChannel from "@/StompChannel";

  interface Card {
    id: number;
    title: string;
  }

  interface Player {
    name: string;
    inPlay: { cards: Card[] };
    hand: { cards: Card[] };
  }

  interface GameState {
    players: {
      [key: string]: Player
    }
  }

  interface TestResultsCardEvent {
    action: string;
    testResultCardView: Card,
    playerId: string;
  }

  @Component({
    components: {
      CardsRow,
      TestResultsCard,
      PlayerScore,
      Modal,
      Die
    }
  })
  export default class Game extends Vue {
    private readonly gameStateChannel =
      new StompChannel<GameState>('/topic/gamestate');
    private readonly testResultsCardChannel =
      new StompChannel<TestResultsCardEvent>('/topic/testresultcard');

    private showTestResultsModal = false;
    private testResultCardDrawnEvent: TestResultsCardEvent = {
      action: '',
      testResultCardView: {id: -1, title: "none"},
      playerId: ''
    };
    private apiUrl = '/api/game/players/';
    private game = {
      player: {
        name: '',
        id: ''
      },
      opponent: {
        name: '',
        id: ''
      },
      hand: {cards: [{ title : "predict",
          id : 40}]},
      inPlay: {cards: [{ title : "predict",
          id : 40}]},
      opponentInPlay: {cards: [{ title : "predict",
          id : 40}]}
    };
    private playerId!: string;

    get handIsFull() {
      return this.game.hand.cards.length === 5;
    }

    loadInitialGameState() {
      fetch(this.apiUrl)
        .then(response => response.json())
        .then(jsonData => this.game = jsonData)
        .catch(error => console.log('Refresh error: ' + error));
    }

    drawToFullHand() {
      fetch(this.apiUrl + '/actions', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({action: 'DRAW_HAND'})
        }
      );
    }

    drawTestResultsCard() {
      fetch(this.apiUrl + '/test-result-card-draws', {
          method: 'POST'
        }
      )
    }

    discardTestResultsCard() {
      fetch(this.apiUrl + '/test-result-card-discards', {
        method: 'POST'
      });
      this.showTestResultsModal = false;
    }

    subscribeToTestCardEvents() {
      this.testResultsCardChannel.onMessage(testResultsCardEvent => {
        switch (testResultsCardEvent.action) {
          case 'TestResultCardDrawn':
            this.testResultCardDrawnEvent = testResultsCardEvent;
            this.showTestResultsModal = true;
            break;
          case 'TestResultCardDiscarded':
            if (testResultsCardEvent.playerId !== this.game.player.id) {
              this.showTestResultsModal = false;
            }
            break;
        }
      });
    }

    subscribeToGameChangedEvents() {
      this.gameStateChannel.onMessage(gameStateChangeEvent => {
        const player = gameStateChangeEvent.players[this.playerId];
        this.game.hand = player.hand;
        this.game.inPlay = player.inPlay;
        const opponent = gameStateChangeEvent.players[this.game.opponent.id];
        this.game.opponentInPlay = opponent.inPlay;
        this.game.opponent.name = opponent.name;
      });
    }

    playerChanged() {
      this.playerId = this.$route.params.playerId;
      this.apiUrl = '/api/game/players/' + this.playerId;
      this.loadInitialGameState();
    }

    // noinspection JSUnusedGlobalSymbols
    created() {
      this.playerChanged();

      this.subscribeToTestCardEvents();
      this.subscribeToGameChangedEvents();
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .main {
    display: grid;
    grid-template-columns: 10rem auto;
  }

  .score-info {
    display: grid;
    grid-template-rows: 35% 35% 30%;
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
