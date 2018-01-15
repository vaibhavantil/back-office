import React from 'react';
import { Icon } from 'semantic-ui-react'; 
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import MessagesList from '../messages/MessagesList';
import MessagesPanel from '../messages/MessagesPanel';

const ChatContainer = styled.div`
    width: 700px;
    border: solid 2px #e8e5e5;
    border-radius: 5px;
`;

export const Header = styled.h2`
    color: #4c4b4b;
`;

const Chat = ({ messages, addMessage, user, error }) => (
    <div>
        <Header>Chat with {user ? `${user.firstName} ${user.lastName || ''}` : 'User'}</Header>
        <Link to="/messages">
            <Icon name="arrow left" /> Back
        </Link>
        <ChatContainer>
            <MessagesList messages={messages.list} error={error} />
            <MessagesPanel addMessage={addMessage} />
        </ChatContainer>
    </div>
);

export default Chat;
