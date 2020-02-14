import {Client, StompHeaders} from "@stomp/stompjs";

const stompClient = new Client({
  brokerURL: 'ws://localhost:8080/api/ws',
  debug: function (str: string) {
    console.log('StompChannel:', str);
  },
  reconnectDelay: 3000
});

const connectedStompClientPromise = new Promise<Client>(resolve => {
  stompClient.onConnect = _ => resolve(stompClient);
  stompClient.activate();
});


export default class StompChannel<T> {
  private readonly destination: string;
  private highWaterMessageNumber = -1;

  constructor(destination: string) {
    this.destination = destination;
  }

  onMessage(callback: (message: T) => void): void {
    connectedStompClientPromise.then(connectedStomp =>
      connectedStomp.subscribe(this.destination, message => {
          if (this.shouldDiscardMessage(message.headers)) {
            return;
          }
          callback(JSON.parse(message.body))
        }
      )
    );
  }

  publish(message: T): void {
    stompClient.publish({destination: this.destination, body: JSON.stringify(message)});
  }

  private shouldDiscardMessage(stompHeaders: StompHeaders) {
    const messageNumberHeader = stompHeaders["message-number"];
    if (messageNumberHeader) {
      const messageNumber: number = Number(messageNumberHeader);
      if (messageNumber <= this.highWaterMessageNumber) {
        console.log("Message discarded: messageNumber = " + messageNumber + ", highWater = " + this.highWaterMessageNumber);
        return true;
      }
      this.highWaterMessageNumber = messageNumber;
    }

    return false;
  }
}
