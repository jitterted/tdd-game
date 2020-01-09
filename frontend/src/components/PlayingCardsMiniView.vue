<template>
  <div class="w-2/3 mb-4">
    <div v-for="(card, index) in cards"
         :key="index"
         class="border-l-8 border-yellow-400 bg-yellow-200 py-2 pl-2 pr-4 mb-1">
      <p>{{ card.title }} ({{card.id}})</p>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'PlayingCardsMiniView',
    props: {
      cards: {
        type: Array,
        required: true
      }
    }
  }
</script>
