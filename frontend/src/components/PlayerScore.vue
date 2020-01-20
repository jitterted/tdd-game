<template>
  <div class="px-4 pt-2">
    <div class="xl:block uppercase font-bold text-gray-200 mb-4">
      {{ name }}
    </div>
    <div class="text-white mb-1">
      Stories Done: {{ score }}
    </div>
    <div class="mb-6">
      <button class="bg-white hover:bg-gray-300 text-black font-bold px-3 rounded mr-1" @click="decrementScore">-
      </button>
      <button class="bg-white hover:bg-gray-300 text-black font-bold px-3 rounded mr-1" @click="incrementScore">+
      </button>
    </div>

    <div class="text-white mb-1">
      Risk Level: {{ riskLevel }}
    </div>
    <div>
      <button class="bg-white hover:bg-gray-300 text-black font-bold px-3 rounded mr-1" @click="decrementRisk">-
      </button>
      <button class="bg-white hover:bg-gray-300 text-black font-bold px-3 rounded mr-1" @click="incrementRisk">+
      </button>
    </div>

    <div class="mt-4">
      <button class="bg-red-800 hover:bg-red-500 text-white font-bold py-1 px-2 rounded border"
              @click="resetAll">Reset
      </button>
    </div>
  </div>
</template>

<script>
  import Stomp from "webstomp-client";

  export default {
    name: "player-score",
    props: {
      name: {
        type: String,
        required: true
      },
      playerId: {
        type: String,
        required: true
      }
    },
    methods: {
      decrementScore() {
        this.score -= 1;
      },
      incrementScore() {
        this.score += 1;
        this.send(JSON.stringify({
          score: this.score,
          playerId: this.playerId
        }));
      },
      resetScore() {
        this.score = 0;
      },
      decrementRisk() {
        this.riskLevel -= 1;
      },
      incrementRisk() {
        this.riskLevel += 1;
      },
      resetRisk() {
        this.riskLevel = 0;
      },
      resetAll() {
        this.resetScore();
        this.resetRisk();
      },
      send(message) {
        this.stompClient.send("/app/score", message);
      },
      disconnect() {
        if (this.stompClient != null) {
          this.stompClient.disconnect();
          console.log("Disconnected");
        }
      }
    },
    data() {
      return {
        score: 0,
        riskLevel: 0,
        stompClient: null
      }
    },
    mounted() {
      this.stompClient = Stomp.client('ws://localhost:8080/api/ws');
      let that = this;
      this.stompClient.connect({}, frame => {
        let subscription = that.stompClient.subscribe('/topic/score', scoreUpdateMessage => {
          let messageBody = scoreUpdateMessage.body;
          console.log('Score Update message body: ' + messageBody);
          let scoreUpdate = JSON.parse(messageBody);
          console.log('This component playerId = ' + that.playerId);
          if (scoreUpdate.playerId === that.playerId) {
            console.log("Updating score for player " + that.playerId);
            that.score = scoreUpdate.score;
          }
        });
        console.log('Subscription successful.');
      });
    }
  };
</script>

<style scoped>
</style>
