import React from 'react';
import { Icon, Message } from 'semantic-ui-react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import MessagesList from '../messages/MessagesList';
import ChatPanel from './ChatPanel';
import * as sockets from 'app/lib/sockets';

const ChatContainer = styled.div`
    width: 700px;
    border: solid 2px #e8e5e5;
    border-radius: 5px;
`;

export const Header = styled.h2`
    color: #4c4b4b;
`;

export const ChatHeader = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    width: 700px;
`;

export default class Chat extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            socket: null
        };
        this.addMessageHandler = this.addMessageHandler.bind(this);
        this.subscribeSocket = this.subscribeSocket.bind(this);
        this.getChatTitle = this.getChatTitle.bind(this);
    }

    addMessageHandler(message, messageType) {
        const { socket } = this.state;
        const { addMessage, match } = this.props;
        if (socket) addMessage(message, messageType, match.params.id, socket);
    }

    subscribeSocket() {
        const {
            messageReceived,
            match,
            getMessagesHistory,
            messages,
            errorReceived
        } = this.props;

        const { stompClient, subscription } = sockets.subscribe(
            { messageReceived, getMessagesHistory, errorReceived },
            match.params.id,
            messages.activeConnection
        );
        return { stompClient, subscription };
    }

    reconnectSocket() {
        const {
            messageReceived,
            getMessagesHistory,
            match,
            setActiveConnection,
            errorReceived
        } = this.props;

        sockets
            .reconnect(
                { messageReceived, getMessagesHistory, errorReceived },
                match.params.id
            )
            .then(reslut => {
                const { stompClient, subscription } = reslut;
                this.setState({ socket: stompClient, subscription });
                setActiveConnection(stompClient);
            });
    }

    getChatTitle() {
        const { messages: { user } } = this.props;

        return `Chat with ${
            user && (user.firstName || user.lastName)
                ? user.firstName + ' ' + (user.lastName || '')
                : 'User'
        }`;
    }

    componentDidMount() {
        const { client: { token }, match, userRequest } = this.props;
        const { stompClient, subscription } = this.subscribeSocket();
        if (!stompClient) {
            this.reconnectSocket();
        }
        this.setState({ socket: stompClient, subscription });
        userRequest(token, match.params.id);
    }

    componentWillUnmount() {
        const { subscription } = this.state;
        sockets.disconnect(null, subscription);
        this.props.clearMessagesList();
    }

    render() {
        const { messages, error, match } = this.props;
        return (
            <React.Fragment>
                <ChatHeader>
                    <Header>{this.getChatTitle()}</Header>
                    <Link to="/users">
                        <Icon name="arrow left" /> Back
                    </Link>
                </ChatHeader>

                <ChatContainer>
                    <MessagesList
                        messages={messages.list}
                        error={!!this.state.socket}
                        userId={match.params.id}
                    />
                    <ChatPanel
                        addMessage={this.addMessageHandler}
                        select={messages.select}
                    />
                    {error && <Message negative>{error.message}</Message>}
                </ChatContainer>
            </React.Fragment>
        );
    }
}
