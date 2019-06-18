import {Message} from './Message';
import {Image} from './Image';

export class EditMessage {
	id: number;
	text: string;
	forwarded: Message[];
	images: Image[];
}
