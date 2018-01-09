import React from 'react';
import { connect } from 'react-redux';
import styled from 'styled-components';
import actions from 'app/store/actions';
import Chat from 'components/messages/Chat';

const ChatContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100%;
    background-color: #f5f5f5;
`;

/* eslint-disable react/prop-types */
class MessagesPage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <ChatContainer>
                <Chat
                    messages={this.props.messages}
                    addMessage={this.props.addMessage}
                    userId={this.props.match.params.id}
                />
            </ChatContainer>
        );
    }
}

const mapStateToProps = ({ client, messages }) => ({
    client,
    messages
});

export default connect(mapStateToProps, {
    ...actions.messagesActions,
    ...actions.clientActions
})(MessagesPage);
