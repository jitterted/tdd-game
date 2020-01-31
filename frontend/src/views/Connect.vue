<template>
  <div class="m-2 p-4 rounded border border-gray-300 bg-gray-200">
    <form>
      <p>
        <label>
          Your Name:
          <input
            v-model="userName"
            type="text"
            name="userName"
            class="border border-green-900 mb-2 ml-1"
          >
        </label>

      </p>
      <button
        class="bg-blue-600 hover:bg-blue-400 text-white font-bold py-1 px-2 rounded border"
        type="submit"
        @click="connect"
      >Connect
      </button>
    </form>
  </div>
</template>

<script lang="ts">
  import {Component, Vue} from "vue-property-decorator";

  @Component
  export default class Connect extends Vue {
    private userName = '';

    connect(event: Event) {
      event.preventDefault();
      console.log("Connecting user to fetch player number: ", {userName: this.userName});
      fetch('/api/game/players', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({userName: this.userName})
        }
      )
        .then((response) => {
          if (response.ok) {
            response.json().then((jsonData) => {
              let playerId = jsonData.playerId;
              this.$router.push('/game/player/' + playerId);
            })
          } else {
            console.error('Bad response: status=' + response.statusText);
          }
        })
        .catch((error) => console.log('Connect Player error: ' + error));
    }
  }
</script>
