<template>
  <button class="bg-white border-2 border-gray-500 py-2 px-5 rounded-sm text-3xl" @click="roll">{{ result }}</button>
</template>

<script lang="ts">
  import {Component, Vue} from "vue-property-decorator";

  @Component
  export default class Die extends Vue {
    private result = 0;

    roll() {
      this.result = Math.floor(Math.random() * 6) + 1;
    }
  }
</script>

<style scoped>

</style>
