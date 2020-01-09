<template>
  <div class="w-2/3 mb-4">
    <div v-for="(card, index) in cards"
         :key="index"
    >
      <div
        class="border-l-8 py-2 pl-2 pr-4 mb-1"
        :class="cardColors(card)"
      >
        {{ card.title }} ({{card.id}})
      </div>
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
    },
    methods: {
      cardColors(card) {
        return this.titleToColor[card.title];
      }
    },
    data() {
      return {
        titleToColor: {
          'write code': 'border-teal-400 bg-teal-200',
          'code smaller': 'border-teal-400 bg-teal-200',
          'predict': 'border-yellow-400 bg-yellow-200',
          'code bloat': 'border-red-400 bg-red-200',
          "can't assert": 'border-red-400 bg-red-200',
          'refactor': 'border-indigo-400 bg-indigo-200'
        }
      }
    }
  }
</script>
