import {Client} from "@stomp/stompjs";

export default class Stomp<T> {
  private static readonly stompClient = new Client({
    brokerURL: 'ws://localhost:8080/api/ws',
    debug: function (str: string) {
      console.log('StompChannel.debug:', str);
    }
  });
  private static readonly connectedStompClientPromise = new Promise<Client>(resolve => {
    Stomp.stompClient.onConnect = _ => resolve(Stomp.stompClient);
    Stomp.stompClient.activate();
  });
  private readonly destination: string;

  constructor(destination: string) {
    this.destination = destination;
  }

  onMessage(callback: (message: T) => void): void {
    Stomp.connectedStompClientPromise.then(connectedStomp =>
      connectedStomp.subscribe(this.destination, message => callback(JSON.parse(message.body))));
  }

  publish(message: T): void {
    Stomp.stompClient.publish({destination: this.destination, body: JSON.stringify(message)});
  }
}
