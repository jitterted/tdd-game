<template>
  <div class="m-2 p-4 rounded border border-gray-300 bg-gray-200">
    <form>
      <p>
        <label for="playerName" class="mr-1">Your Name: </label>
        <input
          id="playerName"
          v-model="playerName"
          type="text"
          name="playerName"
          class="border border-green-900 mb-2"
        >
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

<script>
export default {
  data() {
    return {
      playerName: ''
    }
  },
  methods: {
    connect(event) {
      event.preventDefault();
      console.log("Connecting to fetch player number...");
      fetch('/api/game/players', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({playerName: this.playerName})
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
}
</script>
