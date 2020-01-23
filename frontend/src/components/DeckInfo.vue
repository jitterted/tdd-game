<template>
  <div>
    <p class="uppercase font-bold text-xs">
      {{ title }}
    </p>
    <div class="flex">
      <div class="w-1/2">
        <p class="text-xs uppercase">Discard</p>
        {{ cardDeck.discardPile }}
      </div>
      <div class="w-1/2">
        <p class="text-xs uppercase">Draw</p>
        {{ cardDeck.drawPile }}
      </div>
    </div>
  </div>
</template>
<script lang="ts">
  import {Component, Prop, Vue} from "vue-property-decorator";

  @Component
  export default class DeckInfo extends Vue {
    @Prop() private cardDeck!: {
      discardPile: number,
      drawPile: number
    };
    @Prop() private title!: string;
  }
</script>
