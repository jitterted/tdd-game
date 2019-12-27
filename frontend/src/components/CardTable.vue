<template>
  <div class="flex inline-flex">

    <playing-card v-for="card in cards"
                  :key="card.id"
                  v-bind="card"
                  :order="ordering.indexOf(card.id)"
                  @discard="discard"
    >
    </playing-card>

  </div>
</template>
<script>
  import PlayingCard from "./PlayingCard"

  export default {
    name: 'card-table',
    components: {PlayingCard},
    methods: {
      discard(cardId) {
        console.log("CardTable.discard, source=" + this.source + ": \n" + JSON.stringify({
          id: cardId,
          source: this.source
        }));
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
    },
    props: {
      cards: {
        type: Array,
        required: true
      },
      source: {
        type: String,
        required: true
      }
    },
    data() {
      return {
        ordering: []
      }
    },
    watch: {
      cards: function (newCards, oldCards) {
        let addedCards = newCards.filter(nc => !oldCards.some(oc => oc.id === nc.id));
        let removedCards = oldCards.filter(oc => !newCards.some(nc => nc.id === oc.id));
        this.ordering = this.ordering.filter(ord => !removedCards.some(c => c.id === ord));
        addedCards.forEach(c => this.ordering.push(c.id));
      }
    }
  }
</script>
