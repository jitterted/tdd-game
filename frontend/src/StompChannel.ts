import {Client} from "@stomp/stompjs";

const stompClient = new Client({
  brokerURL: 'ws://localhost:8080/api/ws',
  debug: function (str: string) {
    console.log('StompChannel.debug:', str);
  }
});

const connectedStompClientPromise = new Promise<Client>(resolve => {
  stompClient.onConnect = _ => resolve(stompClient);
  stompClient.activate();
});


export default class StompChannel<T> {
  private readonly destination: string;

  constructor(destination: string) {
    this.destination = destination;
  }

  onMessage(callback: (message: T) => void): void {
    connectedStompClientPromise.then(connectedStomp =>
      connectedStomp.subscribe(this.destination, message => callback(JSON.parse(message.body))));
  }

  publish(message: T): void {
    stompClient.publish({destination: this.destination, body: JSON.stringify(message)});
  }
}
