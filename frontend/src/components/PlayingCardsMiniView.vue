<template>
  <div class="w-2/3 mb-4">
    <div v-for="(card, index) in cards"
         :key="index"
    >
      <div
        class="border-l-8 py-2 pl-2 pr-3 mb-1"
        :class="cardColors(card)"
      >
        {{ card.title }}
        <div class="font-light opacity-25 float-right">
          {{ card.id }}
        </div>

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
        let colorName = this.titleToColorName[card.title];
        return 'border-' + colorName + '-400 bg-' + colorName + '-200';
      }
    },
    data() {
      return {
        titleToColorName: {
          'write code':   'teal',
          'code smaller': 'teal',
          'predict':      'yellow',
          'code bloat':   'red',
          "can't assert": 'red',
          'refactor':     'indigo'
        }
      }
    }
  }
</script>
