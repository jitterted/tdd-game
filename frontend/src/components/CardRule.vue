<template>
  <p class="text-gray-800 text-sm mb-2">
    <slot></slot>
  </p>
</template>

<script lang="ts">
  import {Component, Vue} from "vue-property-decorator";

  @Component
  export default class CardRule extends Vue {
  }
</script>

<style scoped>

</style>
