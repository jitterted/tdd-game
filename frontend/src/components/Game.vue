<template>
  <div class="game main h-screen">

    <div class="score-info h-full bg-gray-800">
      <div class="opponent mb-10 bg-green-700">
        <player-score name="Opponent"/>
      </div>
      <div class="player">
        <player-score name="Ted"/>
      </div>
      <div class="text-center bg-black py-3">
        <button
          class="bg-green-300 rounded border-green-100 px-4 py-2 font-bold mb-4"
          @click="showTestResultsModal = true"
        >
          Run Tests
        </button>
        <die></die>
      </div>
    </div>

    <div class="card-area h-full px-2 pt-2">

      <div class="opponent-in-play inline-flex flex">

        <playing-card title="write code" category="code">
          <card-rule>
            This card is required to be <em>In Play</em> before you can <span class="card-title">predict</span>.
          </card-rule>
        </playing-card>

        <playing-card title="code smaller" category="code">
          <card-rule>
            Combine with <span class="card-title">write code</span> to make it more likely that
            <strong>Run Results</strong> matches your <strong>Prediction</strong>.
          </card-rule>
        </playing-card>

        <playing-card title="predict" category="predict">
          <card-rule>
            You predict that running the tests will <strong>NOT COMPILE</strong>,
            <strong>FAIL</strong> (in a specific way), or <strong>PASS</strong>.
          </card-rule>
          <card-rule>
            You can now <strong>RUN TESTS</strong> and see if the results match your prediction.
          </card-rule>
        </playing-card>

      </div>

      <div class="player-in-play flex inline-flex">
        <playing-card title="write code" category="code">
          <card-rule>
            This card is required to be <em>In Play</em> before you can <span class="card-title">predict</span>.
          </card-rule>
        </playing-card>

        <playing-card title="code smaller" category="code">
          <card-rule>
            Combine with <span class="card-title">write code</span> to make it more likely that
            <strong>Run Results</strong> matches your <strong>Prediction</strong>.
          </card-rule>
        </playing-card>

        <playing-card title="code smaller" category="code">
          <card-rule>
            Combine with <span class="card-title">write code</span> to make it more likely that
            <strong>Run Results</strong> matches your <strong>Prediction</strong>.
          </card-rule>
        </playing-card>

        <playing-card title="predict" category="predict">
          <card-rule>
            You predict that running the tests will <strong>NOT COMPILE</strong>,
            <strong>FAIL</strong> (in a specific way), or <strong>PASS</strong>.
          </card-rule>
          <card-rule>
            You can now <strong>RUN TESTS</strong> and see if the results match your prediction.
          </card-rule>
        </playing-card>

        <playing-card title="code bloat" category="negative">
          <card-rule>
            Deletes 1 <span class="card-title">code smaller</span> card.
          </card-rule>
        </playing-card>

        <playing-card title="can't assert" category="negative">
          <card-rule>
            When in <em>How Do You Know It Does It?</em>, you must stay an extra turn,
            or use a <span class="card-title">refactor</span> to cancel out this impediment.
          </card-rule>
        </playing-card>

      </div>

      <div class="player-hand flex inline-flex">
        <playing-card title="write code" category="code">
          <card-rule>
            This card is required to be <em>In Play</em> before you can <span class="card-title">predict</span>.
          </card-rule>
        </playing-card>

        <playing-card title="write code" category="code">
          <card-rule>
            This card is required to be <em>In Play</em> before you can <span class="card-title">predict</span>.
          </card-rule>
        </playing-card>

        <playing-card title="code bloat" category="negative">
          <card-rule>
            Deletes 1 <span class="card-title">code smaller</span> card.
          </card-rule>
        </playing-card>

        <playing-card title="predict" category="predict">
          <card-rule>
            You predict that running the tests will <strong>NOT COMPILE</strong>,
            <strong>FAIL</strong> (in a specific way), or <strong>PASS</strong>.
          </card-rule>
          <card-rule>
            You can now <strong>RUN TESTS</strong> and see if the results match your prediction.
          </card-rule>
        </playing-card>

        <playing-card title="refactor" category="refactor">
          <card-rule>
            Reduces Risk Level up to 2
          </card-rule>
          <card-rule>
            <span class="card-title"><div class="text-center">OR</div></span>
          </card-rule>
          <card-rule>
            Deletes 1 <span class="card-title">can't assert</span> card.
          </card-rule>
        </playing-card>


      </div>

      <modal :showing="showTestResultsModal" @close="showTestResultsModal = false">
        <div class="md:w-56 px-2 py-3 rounded overflow-hidden border shadow-md my-2" style="background-color: #ffa866">
          <div class="font-bold text-xl smallcaps mb-2">test results</div>
          <p class="text-black font-bold text-base mb-2">
            <code>&gt;= 2</code>
            <span class="card-title">code smaller</span>
            cards<br/>As Predicted
          </p>
          <p class="text-gray-800 text-sm px-1">
            The tests ran and matched your prediction.
          </p>
          <p class="text-black font-bold text-base my-2">
            <code>&lt;= 1</code>
            <span class="card-title">code smaller</span>
            cards<br/>Unexpected
          </p>
          <p class="text-gray-800 text-sm px-1">
            The tests did <strong>not</strong> match your prediction. Try again.
          </p>
        </div>

        <!--      <div class="md:w-56 p-1 rounded overflow-hidden border shadow-md mr-3 mb-4" style="background-color: #ffa866">-->
        <!--        <img class="w-full" src="https://placedog.net/300/100?random&5">-->
        <!--        <div class="px-2 py-2">-->
        <!--          <div class="font-bold text-lg smallcaps mb-2">Test Results</div>-->
        <!--          <p class="text-black text-sm mb-2">-->
        <!--            As Predicted.-->
        <!--          </p>-->
        <!--          <p class="text-gray-700 text-sm">-->
        <!--            The tests ran and matched your prediction.-->
        <!--          </p>-->
        <!--        </div>-->
        <!--      </div>-->
      </modal>

      <div class="hidden">

        <div class="md:w-56 p-1 rounded overflow-hidden border shadow-md mr-3 mb-4" style="background-color: #ffa866">
          <!--        <img class="w-full" src="https://placedog.net/300/100?random&6">-->
          <div class="px-2 py-2">
            <div class="font-bold text-lg smallcaps mb-2">Test Results</div>
            <p class="text-black text-base mb-2">
              1 or more Code Smaller cards: As Predicted
            </p>
            <p class="text-gray-800 text-sm">
              The tests ran and matched your prediction.
            </p>
            <hr/>
            <p class="text-black text-base mb-2">
              0 Code Smaller cards: Unexpected
            </p>
            <p class="text-gray-800 text-sm">
              The tests did <strong>not</strong> match your prediction.
            </p>
          </div>
        </div>

      </div>

    </div>

  </div>
</template>

<script>
  import PlayerScore from "./PlayerScore";
  import PlayingCard from "./PlayingCard";
  import Modal from "./Modal";
  import Die from "./Die";
  import CardRule from "./CardRule";

  export default {
    name: "Game",
    components: {
      PlayerScore,
      PlayingCard,
      Modal,
      Die,
      CardRule
    },
    data() {
      return {
        showTestResultsModal: false
      }
    }
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .main {
    display: grid;
    grid-template-columns: 10rem auto;
  }
</style>
