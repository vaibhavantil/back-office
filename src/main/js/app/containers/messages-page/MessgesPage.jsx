import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import styled from 'styled-components';
import actions from 'app/store/actions';
import Chat from 'components/messages/Chat';
import * as sockets from 'app/lib/sockets';

const ChatContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100%;
    background-color: #f5f5f5;
`;

class MessagesPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            socket: null
        };
        this.addMessageHandler = this.addMessageHandler.bind(this);
        this.subscribeSocket = this.subscribeSocket.bind(this);
    }

    addMessageHandler(message) {
        const { socket } = this.state;
        if (socket) this.props.addMessage(message, socket);
    }

    subscribeSocket() {
        const { messageReceived, match, getMessagesHistory } = this.props;

        const { stompClient, subscription } = sockets.subscribe(
            { messageReceived, getMessagesHistory },
            match.params.id
        );
        return { stompClient, subscription };
    }

    componentDidMount() {
        let { stompClient, subscription } = this.subscribeSocket();
        // trying to reconnect if ws-connection lost
        if (!stompClient) {
            sockets.connect();
            const socketConnection = this.subscribeSocket();
            stompClient = socketConnection.stompClient;
            subscription = socketConnection.subscription;
        }
        this.setState({ socket: stompClient, subscription });
    }

    componentWillUnmount() {
        sockets.unsubscribe(this.state.subscription);
    }

    render() {
        const userId = this.props.match.params.id;
        const { messages, chats } = this.props;
        const user = chats.list.filter(user => user.hid === userId)[0];
        return (
            <ChatContainer>
                <Chat
                    messages={messages}
                    addMessage={this.addMessageHandler}
                    user={user}
                    error={!!this.state.socket}
                />
            </ChatContainer>
        );
    }
}

const mapStateToProps = ({ client, messages, chats }) => {
    return {
        client,
        messages,
        chats
    };
};

export default withRouter(
    connect(mapStateToProps, {
        ...actions.messagesActions,
        ...actions.chatUserActions,
        ...actions.clientActions
    })(MessagesPage)
);
