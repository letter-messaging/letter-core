import {User} from './User';

export class Conversation {
	id: number;
	hidden: boolean;
	users: Array<User>;
}
