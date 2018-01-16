import React from 'react';
import styled from 'styled-components';
import Message from './Message';

const MessagesListContainer = styled.div`
    height: 800px;
    box-sizing: border-box;
    overflow-y: auto;
    padding: 20px;
`;

const EmptyList = styled.h3`
    text-align: center;
`;

export default class MessagesList extends React.Component {
    constructor(props) {
        super(props);
    }

    componentWillReceiveProps() {
        if (this.messagesList) {
            this.messagesList.scrollTop = this.messagesList.scrollHeight + 150;
        }
    }

    render() {
        const { messages, error } = this.props;
        return (
            <MessagesListContainer innerRef={el => (this.messagesList = el)}>
                {messages.length ? (
                    messages.map((item, id) => (
                        <Message
                            key={id}
                            content={item.message}
                            left={item.message && item.message.body}
                        />
                    ))
                ) : (
                    <EmptyList>
                        {error
                            ? 'No messages with this User'
                            : 'Lost connection to server'}
                    </EmptyList>
                )}
            </MessagesListContainer>
        );
    }
}
