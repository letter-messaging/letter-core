import {User} from './User';
import {Conversation} from './Conversation';

export class Message {
  id: number;
  sent: Date;
  text: string;
  sender: User;
  conversation: Conversation;
  forwarded: Array<Message>;
}
