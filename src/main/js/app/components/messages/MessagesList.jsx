import React from 'react';
import styled from 'styled-components';
import Message from './Message';

const MessagesListContainer = styled.div`
    max-height: 800px;
    box-sizing: border-box;
    overflow-y: auto;
    padding: 20px;
`;

/* eslint-disable react/prop-types*/
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
        const { messages, userId } = this.props;
        return (
            <MessagesListContainer innerRef={el => (this.messagesList = el)}>
                {messages &&
                    messages.map(message => (
                        <Message
                            key={message.id}
                            content={message.content}
                            left={message.author === parseInt(userId, 10)}
                        />
                    ))}
            </MessagesListContainer>
        );
    }
}
