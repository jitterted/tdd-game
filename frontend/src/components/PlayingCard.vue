<template>
  <div
    class="md:w-48 rounded overflow-hidden mr-3 my-2 pb-2"
    :class="[backgroundColor, selectedClasses]"
    @click.prevent="toggleSelect"
  >
    <div class="font-bold text-xl smallcaps mb-2 p-2" :class="titleBackgroundColor">{{ title }}</div>
    <div class="px-2 py-1">
      <slot>fallback content</slot>
    </div>
  </div>
</template>

<script>
  export default {
    name: "playing-card",
    props: ['title', 'category'],
    computed: {
      backgroundColor() {
        if (this.category) {
          return this.categoryColors[this.category].background;
        } else {
          return 'bg-white';
        }
      },
      titleBackgroundColor() {
        if (this.category) {
          return this.categoryColors[this.category].title;
        } else {
          return 'bg-gray-100';
        }
      },
      selectedClasses() {
        if (this.selected) {
          return 'border border-gray-600 shadow-2xl';
        } else {
          return 'border shadow-md';
        }
      }
    },
    methods: {
      toggleSelect() {
        this.selected = !this.selected;
      }
    },
    data() {
      return {
        categoryColors: {
          'code': {background: 'bg-teal-300', title: 'bg-teal-200'},
          'predict': {background: 'bg-yellow-300', title: 'bg-yellow-200'},
          'negative': {background: 'messy-code', title: 'bg-red-200'},
          'refactor': {background: 'bg-indigo-200', title: 'bg-indigo-100'},
        },
        selected: false
      }
    }
  }
</script>

<style scoped>

</style>
