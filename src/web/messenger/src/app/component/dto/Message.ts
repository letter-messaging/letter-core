import {User} from './User';
import {Conversation} from './Conversation';

export class Message {
  id: number;
  sent: Date;
  text: string;
  read: boolean;
  sender: User;
  conversation: Conversation;
  forwarded: Array<Message>;
  selected = false;
}
