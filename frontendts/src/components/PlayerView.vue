<template>
  <div class="flex w-11/12 mx-auto flex-wrap">
    <div class="w-1/3 border-t-2 border-gray-500">IN PLAY</div>
    <PlayingCardsMiniView
      :cards="player.inPlay.cards"
      class="mb-4"
    />
    <div class="w-1/3 border-t-2 border-gray-500">HAND</div>
    <PlayingCardsMiniView
      :cards="player.hand.cards"
    />
  </div>
</template>
<script lang="ts">
  import {Component, Prop, Vue} from "vue-property-decorator";
  import PlayingCardsMiniView from "./PlayingCardsMiniView.vue"

  @Component({
    components: {PlayingCardsMiniView}
  })
  export default class PlayerView extends Vue {
    @Prop() private player!: object;
  }
</script>
