import {Message} from './Message';
import {NewImage} from './NewImage';

export class NewMessage {
	senderId: number;
	conversationId: number;
	text: string;
	forwarded: Message[] = [];
	images: NewImage[] = [];
}
