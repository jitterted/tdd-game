<template>
  <div
    v-if="showing"
    class="fixed inset-0 w-full h-screen flex items-center justify-center bg-semi-50"
    @click.self="close"
  >
    <div class="relative max-w-2xl bg-white shadow-lg rounded p-10">
      <button
        v-if="allowClose"
        aria-label="close"
        class="absolute top-0 right-0 text-2xl text-gray-500 my-2 mx-4"
        @click.prevent="close"
      >
        ×
      </button>
      <slot />
      <button
        v-if="allowClose"
        tabindex="0"
        class="bg-blue-600 text-white px-4 py-2 text-sm uppercase tracking-wide font-bold rounded"
        @click="close"
      >
        Close
      </button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "modal",
    props: {
      showing: {
        required: true,
        type: Boolean
      },
      allowClose: {
        required: true,
        type: Boolean
      }
    },
    created() {
      window.addEventListener('keyup', this.doKeyUp);
    },
    destroyed() {
      window.removeEventListener('keyup', this.doKeyUp);
    },
    methods: {
      doKeyUp(event) {
        if (event.defaultPrevented) {
          return; // Should do nothing if the default action has been cancelled
        }

        var escape = false;
        if (event.key !== undefined) {
          if (event.key === "Escape") {
            escape = true;
          }
        } else if (event.keyCode !== undefined) {
          if (event.keyCode === 27) {
            escape = true;
          }
        }

        if (escape) {
          event.preventDefault();
          this.close();
        }
      },
      close() {
        if (this.allowClose) {
          this.$emit('close');
        }
      }
    }
  }
</script>

<style scoped>

.bg-semi-50 {
  background-color: rgba(0, 0, 0, 0.5);
}

</style>
