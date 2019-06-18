import {User} from './User';

export class Document {
	id: number;
	user: User;
	path: string;
	uploaded: Date;
}
