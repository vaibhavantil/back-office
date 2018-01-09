import React from 'react';
import { Link } from 'react-router-dom';
import { List } from 'semantic-ui-react';

/* eslint-disable */
const ChatsList = ({ chats }) => (
    <List selection verticalAlign="middle" celled size="big">
        {chats.map(item => (
            <Link key={item.id} to={`/messages/${item.id}`} replace>
                <List.Item>
                    <List.Content>
                        <List.Header>{item.name}</List.Header>
                    </List.Content>
                </List.Item>
            </Link>
        ))}
    </List>
);

export default ChatsList;
