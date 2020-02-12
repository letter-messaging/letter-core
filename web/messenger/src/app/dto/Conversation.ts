import {User} from './User';

export class Conversation {
    id: number;
    chatName: string;
    hidden: boolean;
    users: User[];
}
