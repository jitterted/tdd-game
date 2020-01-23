<template>
  <div class="md:w-56 rounded overflow-hidden border shadow-md my-2" style="background-color: #ffa866">
    <div class="font-bold text-xl smallcaps mb-2 p-2" style="background-color: #ffc499">test results</div>
    <div class="px-2 py-1 mb-3" style="min-height: 14rem" v-html="card"/>
  </div>
</template>

<script lang="ts">
  import {Component, Prop, Vue} from "vue-property-decorator";

  @Component
  export default class TestResultsCard extends Vue {
    @Prop() private readonly title!: string;

    get card() {
      return this.testResultCards[this.title];
    }

    private testResultCards: { [key: string]: string; } = {
      'as predicted': `
            <p class="text-black font-bold text-base mb-2">
              As Predicted
            </p>
            <p/>
            <p class="text-gray-800 text-sm px-1">
              The tests ran and <strong>matched</strong> your prediction.
            </p>
            <p/>
            `,
      'require 1': `
              <p class="text-black font-bold text-base mb-2">
                <code>&gt;= 1 </code>
                <span class="card-title">refactor code</span>
                cards<br/>As Predicted
              </p>
              <p class="text-gray-800 text-sm px-1">
                The tests ran and <strong>matched</strong> your prediction.
              </p>
              <p class="text-black font-bold text-base my-2">
                <code>No </code>
                <span class="card-title">refactor code</span>
                cards<br/>Unexpected
              </p>
              <p class="text-gray-800 text-sm px-1">
                The tests did <strong>not</strong> match your prediction. Try again.
              </p>
          `,
      'require 2': `
              <p class="text-black font-bold text-base mb-2">
                <code>&gt;= 2 </code>
                <span class="card-title">refactor code</span>
                cards<br/>As Predicted
              </p>
              <p class="text-gray-800 text-sm px-1">
                The tests ran and <strong>matched</strong> your prediction.
              </p>
              <p class="text-black font-bold text-base my-2">
                <code>&lt;= 1 </code>
                <span class="card-title">refactor code</span>
                cards<br/>Unexpected
              </p>
              <p class="text-gray-800 text-sm px-1">
                The tests did <strong>not</strong> match your prediction. Try again.
              </p>
          `
    }
  }
</script>
