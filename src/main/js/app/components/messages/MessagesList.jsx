import React from 'react';
import styled from 'styled-components';
import Message from './Message';

const MessagesListContainer = styled.div`
    height: 800px;
    box-sizing: border-box;
    overflow-y: auto;
    padding: 20px 20px 60px;
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
            this.messagesList.scrollTop = this.messagesList.scrollHeight;
        }
    }

    render() {
        const { messages, error } = this.props;
        const userId = this.props.userId;
        return (
            <MessagesListContainer innerRef={el => (this.messagesList = el)}>
                {messages.length ? (
                    messages.map((item, id) => (
                        <Message
                            key={id}
                            content={item.body}
                            left={item.header.fromId === userId}
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
