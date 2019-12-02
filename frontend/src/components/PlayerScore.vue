<template>
  <div class="px-4 pt-2">
    <div class="xl:block uppercase font-bold text-gray-200 mb-4">
      {{ name }}'s Score
    </div>
    <div class="text-white mb-2">
      Passing Tests: {{ passingTests }}
    </div>
    <div class="mb-6">
      <button class="bg-white hover:bg-gray-300 text-black font-bold py-1 px-3 rounded mr-1" @click="decrementTests">-</button>
      <button class="bg-white hover:bg-gray-300 text-black font-bold py-1 px-3 rounded mr-1" @click="incrementTests">+</button>
    </div>

    <div class="text-white">
      Risk Level: {{ riskLevel }}
    </div>
    <div>
      <button class="bg-white hover:bg-gray-300 text-black font-bold py-1 px-3 rounded mr-1" @click="decrementRisk">-</button>
      <button class="bg-white hover:bg-gray-300 text-black font-bold py-1 px-3 rounded mr-1" @click="incrementRisk">+</button>
    </div>

    <div class="mt-6">
      <button class="bg-red-800 hover:bg-red-500 text-white font-bold py-1 px-2 rounded border"
              @click="resetAll">Reset Scores</button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "player-score",
    props: ['name'],
    methods: {
      decrementTests() {
        this.passingTests -= 1;
      },
      incrementTests() {
        this.passingTests += 1;
      },
      resetTests() {
        this.passingTests = 0;
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
        this.resetTests();
        this.resetRisk();
      }
    },
    data() {
      return {
        passingTests: 0,
        riskLevel: 0
      }
    }
  };
</script>

<style scoped>
</style>
