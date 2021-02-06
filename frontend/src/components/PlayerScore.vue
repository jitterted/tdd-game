<template>
  <div class="px-4 pt-2">
    <div class="xl:block uppercase font-bold text-gray-200 mb-4">
      {{ name }}
    </div>
    <div class="text-white mb-1">
      Location: {{ location }}
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
import StompChannel from "@/StompChannel";

@Component
  export default class PlayerScore extends Vue {
    @Prop() private name!: string;
    @Prop() private playerId!: string;

    private score = 0;
    private riskLevel = 0;
    private location = ''

    private readonly scoreChannel = new StompChannel<{score: number, playerId: string}>('/topic/score');
    private readonly locationChannel =
      new StompChannel<{location: string, playerId: string}>('/topic/location');

    decrementScore() {
      this.score -= 1;
    }

    incrementScore() {
      this.score += 1;
      this.scoreChannel.publish({
        score: this.score,
        playerId: this.playerId
      });
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

    // noinspection JSUnusedGlobalSymbols
    created() {
      this.scoreChannel.onMessage(scoreUpdate => {
        if (scoreUpdate.playerId === this.playerId) {
          this.score = scoreUpdate.score;
        }
      })
      this.locationChannel.onMessage(locationUpdate => {
        if (this.playerId === locationUpdate.playerId) {
          this.location = locationUpdate.location
        }
      })
    }

  }
</script>

<style scoped>
</style>
