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

export default class Chat extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        // this.props.getMessages(this.props.userId);
    }

    render() {
        const { messages, addMessage, userId, user } = this.props;
        return (
            <div>
                {
                    user && <Header>Chat with {user.name}</Header>
                }
                <Link to="/messages">Back</Link>
                <ChatContainer>
                    <MessagesList
                        messages={messages.list}
                        userId={userId}
                    />
                    <MessagesPanel addMessage={addMessage} />
                </ChatContainer>
            </div>
        );
    }
}
