import {Message} from './Message';
import {NewImage} from './NewImage';
import {NewDocument} from './NewDocument';

export class NewMessage {
	senderId: number;
	conversationId: number;
	text: string;
	forwarded: Message[] = [];
	images: NewImage[] = [];
	documents: NewDocument[] = [];
}
