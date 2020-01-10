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
<script>
  export default {
    name: 'DeckInfo',
    props: {
      cardDeck: {
        type: Object,
        required: true
      },
      title: {
        type: String,
        required: true
      }
    }
  }
</script>
