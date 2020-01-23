<template>
  <div class="flex inline-flex">

    <playing-card v-for="card in cards"
                  :key="card.id"
                  v-bind="card"
                  :order="ordering"
                  :rowName="source"
                  @discard="discard"
                  @moveright="moveRight"
                  @moveleft="moveLeft"
    >
    </playing-card>

  </div>
</template>
<script lang="ts">
  import {Component, Prop, Vue, Watch} from "vue-property-decorator";
  import PlayingCard from "./PlayingCard.vue"

  @Component({
    components: {PlayingCard}
  })
  export default class CardsRow extends Vue {
    @Prop() private cards!: any[];
    @Prop() private source!: string;
    private ordering: number[] = [];

    @Watch('cards')
    onCardsChanged(newCards: any[], oldCards: any[]) {
      let addedCards = newCards.filter(nc => !oldCards.some(oc => oc.id === nc.id));
      let removedCards = oldCards.filter(oc => !newCards.some(nc => nc.id === oc.id));
      this.ordering = this.ordering.filter(ord => !removedCards.some(c => c.id === ord));
      addedCards.forEach(c => this.ordering.push(c.id));
    }

    discard(cardId: number) {
      fetch('/api/game/players/' + this.$route.params.playerId + '/discards', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            id: cardId,
            source: this.source
          })
        }
      );
    }

    moveRight(cardId: number) {
      let cardIndex = this.ordering.indexOf(cardId);
      let cardToTheRight = this.ordering[cardIndex + 1];
      this.$set(this.ordering, cardIndex, cardToTheRight);
      this.$set(this.ordering, cardIndex + 1, cardId);
    }

    moveLeft(cardId: number) {
      let cardIndex = this.ordering.indexOf(cardId);
      let cardToTheLeft = this.ordering[cardIndex - 1];
      this.$set(this.ordering, cardIndex, cardToTheLeft);
      this.$set(this.ordering, cardIndex - 1, cardId);
    }


  }
</script>
