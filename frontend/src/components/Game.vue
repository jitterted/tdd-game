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

<script>
  import PlayerScore from "./PlayerScore";
  import Modal from "./Modal";
  import Die from "./Die";
  import TestResultsCard from "./TestResultsCard";
  import CardsRow from "./CardsRow";
  import Stomp from "webstomp-client";

  export default {
    name: "Game",
    components: {
      CardsRow,
      TestResultsCard,
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
      },
      discardTestResultsCard() {
        fetch(this.apiUrl + '/test-result-card-discards', {
          method: 'POST'
        });
        this.showTestResultsModal = false;
      },
      subscribeToTestCardEvents() {
        this.stompClient = Stomp.client('ws://localhost:8080/api/ws');
        let that = this;
        this.stompClient.connect({}, frame => {
          let subscription = that.stompClient.subscribe('/topic/testresultcard', testResultCardMessage => {
            let messageBody = testResultCardMessage.body;
            let testResultCardEvent = JSON.parse(messageBody);
            switch (testResultCardEvent.action) {
              case 'TestResultCardDrawn':
                that.testResultCardDrawnEvent = testResultCardEvent;
                that.showTestResultsModal = true;
                break;
              case 'TestResultCardDiscarded':
                if (testResultCardEvent.playerId !== that.playerId) {
                  that.showTestResultsModal = false;
                }
                break;
            }
          });
        });
      },
      playerChanged() {
        this.apiUrl = '/api/game/players/' + this.$route.params.playerId;
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
      this.subscribeToTestCardEvents();
    },
    beforeDestroy(){
      clearInterval(this.interval);
    },
    data() {
      return {
        stompClient: undefined,
        interval: undefined,
        showTestResultsModal: false,
        testResultCardDrawnEvent: {
          testResultCardView: {id: -1, title: "none"},
          playerId: -1
        },
        apiUrl: '/api/game/players/',
        game: {
          player: {
            name: '',
            id: ''
          },
          opponent: {
            name: '',
            id: ''
          },
          hand: {cards: []},
          inPlay: {cards: []},
          opponentInPlay: {cards: []}
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
