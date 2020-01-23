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

<script lang="ts">
  import {Component, Prop, Vue} from "vue-property-decorator";
  import Stomp, {Client} from "webstomp-client";

  @Component
  export default class PlayerScore extends Vue {
    @Prop() private name!: string;
    @Prop() private playerId!: string;

    private score = 0;
    private riskLevel = 0;
    private stompClient?: Client;

    decrementScore() {
      this.score -= 1;
    }

    incrementScore() {
      this.score += 1;
      this.send(JSON.stringify({
        score: this.score,
        playerId: this.playerId
      }));
    }

    resetScore() {
      this.score = 0;
    }

    decrementRisk() {
      this.riskLevel -= 1;
    }

    incrementRisk() {
      this.riskLevel += 1;
    }

    resetRisk() {
      this.riskLevel = 0;
    }

    resetAll() {
      this.resetScore();
      this.resetRisk();
    }

    send(message: string) {
      this.stompClient!.send("/app/score", message);
    }

    disconnect() {
      if (this.stompClient != null) {
        this.stompClient.disconnect();
        console.log("Disconnected");
      }
    }

    mounted() {
      this.stompClient = Stomp.client('ws://localhost:8080/api/ws');
      let that = this;
      this.stompClient.connect({}, frame => {
        let subscription = that.stompClient!.subscribe('/topic/score', scoreUpdateMessage => {
          let messageBody = scoreUpdateMessage.body;
          console.log('Score Update message body: ' + messageBody);
          let scoreUpdate = JSON.parse(messageBody);
          if (scoreUpdate.playerId === that.playerId) {
            that.score = scoreUpdate.score;
          }
        });
      });
    }
  }
</script>

<style scoped>
</style>
