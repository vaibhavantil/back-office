import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import styled from 'styled-components';
import actions from 'app/store/actions';
import Chat from 'components/chat/Chat';
import * as sockets from 'app/lib/sockets';

const ChatContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100%;
    background-color: #f5f5f5;
`;

class ChatPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            socket: null
        };
        this.addMessageHandler = this.addMessageHandler.bind(this);
        this.subscribeSocket = this.subscribeSocket.bind(this);
    }

    addMessageHandler(message, messageType) {
        const { socket } = this.state;
        const userId = this.props.match.params.id;
        if (socket) this.props.addMessage(message, messageType, userId, socket);
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

    componentDidMount() {
        const { client, match } = this.props;
        const { stompClient, subscription } = this.subscribeSocket();
        if (!stompClient) {
            this.reconnectSocket();
        }
        this.setState({ socket: stompClient, subscription });
        this.props.userRequest(client.token, match.params.id);
    }

    componentWillUnmount() {
        const { subscription } = this.state;
        sockets.disconnect(null, subscription);
        this.props.clearMessagesList();
    }

    render() {
        const userId = this.props.match.params.id;
        const { messages } = this.props;
        return (
            <ChatContainer>
                <Chat
                    messages={messages}
                    addMessage={this.addMessageHandler}
                    user={messages.user}
                    error={messages.error}
                    lostConnection={!!this.state.socket}
                    userId={userId}
                />
            </ChatContainer>
        );
    }
}

const mapStateToProps = ({ client, messages }) => {
    return {
        client,
        messages
    };
};

export default withRouter(
    connect(mapStateToProps, {
        ...actions.messagesActions,
        ...actions.chatUserActions,
        ...actions.clientActions
    })(ChatPage)
);
