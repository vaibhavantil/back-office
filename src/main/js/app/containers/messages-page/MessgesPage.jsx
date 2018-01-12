import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import styled from 'styled-components';
import actions from 'app/store/actions';
import Chat from 'components/messages/Chat';
// import { setupSocket } from 'app/lib/sockets';

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
    }

    componentDidMount() {
        // const { messageReceived } = this.props;
        // setupSocket({ messageReceived }, this.props.match.params.id);
    }

    render() {
        const userId = this.props.match.params.id;
        const { messages, addMessage, chats } = this.props;
        const user = chats.list.filter(
            user => user.id === parseInt(userId, 10)
        )[0];
        return (
            <ChatContainer>
                <Chat
                    messages={messages}
                    addMessage={addMessage}
                    userId={userId}
                    user={user}
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
