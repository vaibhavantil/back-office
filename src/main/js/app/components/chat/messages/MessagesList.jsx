import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import Message from './Message';

const MessagesListContainer = styled.div`
    height: 100%;
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
        /* eslint-disable */
        setTimeout(() => {
            const list = this.messagesList;
            this.messagesList.scrollTop = list ? list.scrollHeight : 0;
            const id = this.props.messageId;
            const msgNode = document.getElementById(`msg-${id}`);
            if (id && msgNode) {
                msgNode.scrollIntoView();
            }
        });
        /* eslint-enable */
    }

    render() {
        const { messages, error } = this.props;
        const userId = parseInt(this.props.userId);
        return (
            <MessagesListContainer innerRef={el => (this.messagesList = el)}>
                {messages.length ? (
                    messages.map((item, id) => (
                        <Message
                            key={id}
                            content={item.body}
                            left={item.header.fromId !== userId}
                            msgId={item.globalId}
                            timestamp={item.timestamp}
                            from={
                                item.header.fromId !== userId ? 'bot' : 'member'
                            }
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

MessagesList.propTypes = {
    userId: PropTypes.string.isRequired,
    messageId: PropTypes.string,
    messages: PropTypes.array,
    error: PropTypes.bool
};
