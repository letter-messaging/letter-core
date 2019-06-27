import {User} from './User';
import {Conversation} from './Conversation';
import {Message} from './Message';
import {PreviewType} from './enum/PreviewType';
import {Avatar} from './Avatar';

export class Preview {
	type: PreviewType;
	conversation: Conversation;
	with: User;
	lastMessage: Message;
	avatar: Avatar;
	unread: number;
	kicked: boolean;
}
