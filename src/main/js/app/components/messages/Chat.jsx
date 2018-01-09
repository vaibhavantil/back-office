import React from 'react';
import styled from 'styled-components';
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

/* eslint-disable */
export default class Chat extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const { messages, addMessage, userId } = this.props;
        return (
            <div>
                <Header>Chat with {messages.user.name}</Header>
                <ChatContainer>
                    <MessagesList
                        messages={messages.messages}
                        userId={userId}
                    />
                    <MessagesPanel />
                </ChatContainer>
            </div>
        );
    }
}
