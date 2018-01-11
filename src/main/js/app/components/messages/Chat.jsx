import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import MessagesList from '../messages/MessagesList';
import MessagesPanel from '../messages/MessagesPanel';

const ChatContainer = styled.div`
    width: 700px;
    border: solid 2px #e8e5e5;
    border-radius: 5px;
`;

const Header = styled.h2`
    color: #4c4b4b;
`;

const Chat = ({ messages, addMessage, userId, user }) => (
    <div>
        <Header>Chat with {user ? user.name : 'User'}</Header>
        <Link to="/messages">Back</Link>
        <ChatContainer>
            <MessagesList messages={messages.list} userId={userId} />
            <MessagesPanel addMessage={addMessage} />
        </ChatContainer>
    </div>
);

export default Chat;
