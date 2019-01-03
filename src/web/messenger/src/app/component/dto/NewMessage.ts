import {Message} from './Message';

export class NewMessage {
  senderId: number;
  conversationId: number;
  text: string;
  forwarded: Array<Message>;
}
