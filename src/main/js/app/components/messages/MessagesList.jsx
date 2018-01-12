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

    componentDidMount() {
        if (this.messagesList) {
            this.messagesList.scrollTop = this.messagesList.scrollHeight;
        }
    }

    render() {
        const { messages, userId, error } = this.props;
        return (
            <MessagesListContainer innerRef={el => (this.messagesList = el)}>
                {messages.length ? (
                    messages.map((item, id) => (
                        <Message
                            key={id}
                            content={item.message}
                            left={item.author === parseInt(userId, 10)}
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
