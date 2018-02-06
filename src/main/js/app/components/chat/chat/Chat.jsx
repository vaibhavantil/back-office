import React from 'react';
import PropTypes from 'prop-types';
import { Icon, Message } from 'semantic-ui-react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import MessagesList from '../messages/MessagesList';
import ChatPanel from './ChatPanel';
import * as sockets from 'sockets';

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
            socket: null,
            subscription: null
        };
    }

    addMessageHandler = (message, messageType) => {
        const { socket } = this.state;
        const { addMessage, match } = this.props;
        if (socket) addMessage(message, messageType, match.params.id, socket);
    };

    subscribeSocket = () => {
        const { messageReceived, match, messages, errorReceived } = this.props;

        const { stompClient, subscription } = sockets.chatSubscribe(
            { messageReceived, errorReceived },
            match.params.id,
            messages.activeConnection
        );
        return { stompClient, subscription };
    };

    reconnectSocket = () => {
        const {
            messageReceived,
            match,
            setActiveConnection,
            errorReceived
        } = this.props;

        sockets
            .chatReconnect({ messageReceived, errorReceived }, match.params.id)
            .then(reslut => {
                const { stompClient, subscription } = reslut;
                this.setState({ socket: stompClient, subscription });
                setActiveConnection(stompClient);
            });
    };

    getChatTitle = () => {
        const { messages: { user } } = this.props;

        return `Chat with ${
            user && (user.firstName || user.lastName)
                ? user.firstName + ' ' + (user.lastName || '')
                : 'User'
        }`;
    };

    componentDidMount() {
        const { match, userRequest } = this.props;
        const { stompClient, subscription } = this.subscribeSocket();
        if (!stompClient) {
            this.reconnectSocket();
        }
        this.setState({ socket: stompClient, subscription });
        userRequest(match.params.id);
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

Chat.propTypes = {
    messageReceived: PropTypes.func.isRequired,
    match: PropTypes.object.isRequired,
    messages: PropTypes.object.isRequired,
    errorReceived: PropTypes.func,
    addMessage: PropTypes.func.isRequired,
    setActiveConnection: PropTypes.func.isRequired,
    userRequest: PropTypes.func.isRequired,
    error: PropTypes.object,
    clearMessagesList: PropTypes.func.isRequired
};
