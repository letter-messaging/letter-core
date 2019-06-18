import {User} from './User';
import {Conversation} from './Conversation';
import {Image} from './Image';
import {Document} from './Document';

export class Message {
	id: number;
	sent: Date;
	text: string;
	read: boolean;
	sender: User;
	conversation: Conversation;
	forwarded: Message[];
	images: Image[];
	documents: Document[];
	selected = false;
}
