import {User} from './User';
import {Conversation} from './Conversation';

export class Message {
  id: number;
  sent: string;
  text: string;
  sender: User;
  conversation: Conversation;
  forwarded: Array<Message>;
}
