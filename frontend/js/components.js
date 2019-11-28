Vue.component('playing-card', {
  props: ['title', 'category'],
  template: `
    <div
      class="md:w-48 rounded overflow-hidden mr-3 my-2 pb-2"
      :class="[backgroundColor, selectedClasses]"
      @click.prevent="toggleSelect"
      >
      <div class="font-bold text-xl smallcaps mb-2 p-2" :class="titleBackgroundColor">{{ title }}</div>
      <div class="px-2 py-1">
        <slot></slot>
      </div>
    </div>
  `,
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
});

Vue.component('card-title', {
  template: '<span class="font-bold smallcaps"><slot></slot></span>'
});

Vue.component('card-rule', {
  template: `
    <p class="text-gray-800 text-sm mb-2">
      <slot></slot>
    </p>
  `
});

Vue.component('player-score', {
  props: ['name'],
  template: `
    <div class="px-4 pt-2">
      <div class="xl:block uppercase font-bold text-gray-200 mb-4">
        {{ name }}'s Score
      </div>
      <div class="text-white mb-2">
        Passing Tests: {{ passingTests }}
      </div>
      <div class="mb-6">
        <button class="bg-white hover:bg-gray-300 text-black font-bold py-1 px-3 rounded" @click="decrementTests">-</button>
        <button class="bg-white hover:bg-gray-300 text-black font-bold py-1 px-3 rounded" @click="incrementTests">+</button>
      </div>

      <div class="text-white">
        Risk Level: {{ riskLevel }}
      </div>
      <div>
        <button class="bg-white hover:bg-gray-300 text-black font-bold py-1 px-3 rounded" @click="decrementRisk">-</button>
        <button class="bg-white hover:bg-gray-300 text-black font-bold py-1 px-3 rounded" @click="incrementRisk">+</button>
      </div>

      <div class="mt-6">
        <button class="bg-red-800 hover:bg-red-500 text-white font-bold py-1 px-2 rounded border"
          @click="resetAll">Reset Scores</button>
      </div>
    </div>
  `,
  methods: {
    decrementTests() {
      this.passingTests -= 1;
    },
    incrementTests() {
      this.passingTests += 1;
    },
    resetTests() {
      this.passingTests = 0;
    },
    decrementRisk() {
      this.riskLevel -= 1;
    },
    incrementRisk() {
      this.riskLevel += 1;
    },
    resetRisk() {
      this.riskLevel = 0;
    },
    resetAll() {
      this.resetTests();
      this.resetRisk();
    }
  },
  data() {
    return {
      passingTests: 0,
      riskLevel: 0
    }
  }
});

Vue.component('die', {
  template: '<button class="bg-white border-2 border-gray-500 py-2 px-5 rounded-sm text-3xl" @click="roll">{{ result }}</button>',
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
});

Vue.component('modal', {
  template: `
   <div
    v-if="showing"
    class="fixed inset-0 w-full h-screen flex items-center justify-center bg-semi-50"
    @click.self="close"
    >
    <div class="relative max-w-2xl bg-white shadow-lg rounded p-10">
      <button
        aria-label="close"
        class="absolute top-0 right-0 text-2xl text-gray-500 my-2 mx-4"
        @click.prevent="close"
      >
        ×
      </button>
      <slot />
      <button
        tabindex="0"
        class="bg-blue-600 text-white px-4 py-2 text-sm uppercase tracking-wide font-bold rounded"
        @click="close"
      >
        Close
      </button>
      </div>
  </div>
  `,
  props: {
    showing: {
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
      this.$emit('close');
    }
  }
});
