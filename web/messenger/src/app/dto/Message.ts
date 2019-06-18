import {User} from './User';
import {Conversation} from './Conversation';
import {Image} from './Image';

export class Message {
	id: number;
	sent: Date;
	text: string;
	read: boolean;
	sender: User;
	conversation: Conversation;
	forwarded: Message[];
	images: Image[];
	selected = false;
}
