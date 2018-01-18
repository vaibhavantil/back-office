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
            messages
        } = this.props;

        const { stompClient, subscription } = sockets.subscribe(
            { messageReceived, getMessagesHistory },
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
            setActiveConnection
        } = this.props;

        sockets
            .reconnect({ messageReceived, getMessagesHistory }, match.params.id)
            .then(reslut => {
                const { stompClient, subscription } = reslut;
                this.setState({ socket: stompClient, subscription });
                setActiveConnection(stompClient);
            });
    }

    componentDidMount() {
        const { stompClient, subscription } = this.subscribeSocket();
        if (!stompClient) {
            this.reconnectSocket();
        }
        this.setState({ socket: stompClient, subscription });
    }

    componentWillUnmount() {
        const { subscription } = this.state;
        sockets.disconnect(null, subscription);
        this.props.clearMessagesList();
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
                    userId={userId}
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
