import {Message} from './Message';

export class EditMessage {
  id: number;
  text: string;
  forwarded: Array<Message>;
}
