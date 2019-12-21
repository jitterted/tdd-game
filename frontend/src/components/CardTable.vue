<template>
  <div class="flex inline-flex">

    <playing-card v-for="card in cards"
                  :key="card.id"
                  v-bind="card"
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
        fetch('/api/game/players/0/discards', {
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
    }
  }
</script>
