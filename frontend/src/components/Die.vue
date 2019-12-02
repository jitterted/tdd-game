<template>
  <button class="bg-white border-2 border-gray-500 py-2 px-5 rounded-sm text-3xl" @click="roll">{{ result }}</button>
</template>

<script>
  export default {
    name: "die",
    data() {
      return {
        result: 1
      }
    },
    methods: {
      roll() {
        this.result = Math.floor(Math.random() * 6) + 1;
      }
    }
  }
</script>

<style scoped>

</style>
