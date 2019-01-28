import {Message} from './Message';
import {NewImage} from './NewImage';

export class NewMessage {
  senderId: number;
  conversationId: number;
  text: string;
  forwarded: Array<Message> = [];
  images: Array<NewImage> = [];
}
