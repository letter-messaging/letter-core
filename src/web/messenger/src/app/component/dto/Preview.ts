import {User} from './User';
import {Conversation} from './Conversation';
import {Message} from './Message';

export class Preview {
  conversation: Conversation;
  with: User;
  lastMessage: Message;
  unread: number;
}
