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
/* 
const Header = styled.h2`
    color: #4c4b4b;
`;
 */
export default class Chat extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            userName: ''
        };
    }

    componentDidMount() {
        const { messages, userId, getMessages } = this.props;
        const user = messages.users.filter(user => user.id === parseInt(userId, 10));

        this.setState({
            userName: user.length && user[0].name
        });

        getMessages(userId);
    }

    render() {
        const { messages, addMessage, userId } = this.props;
        return (
            <div>
                <h1>Chat with {this.state.userName}</h1>
                <Link to="/messages">Back</Link>
                <ChatContainer>
                    <MessagesList
                        messages={messages.messages}
                        userId={userId}
                    />
                    <MessagesPanel addMessage={addMessage} />
                </ChatContainer>
            </div>
        );
    }
}
