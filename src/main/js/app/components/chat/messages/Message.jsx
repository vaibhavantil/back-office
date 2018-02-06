import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import moment from 'moment';
import 'moment/locale/sv';
import SelectMessage from './SelectMessage';
import AudioMessage from './AudioMessage';
import ImageMessage from './ImageMessage';
import * as types from 'app/lib/messageTypes';

const MessageRow = styled.div`
    display: flex;
    justify-content: ${props => (props.left ? 'flex-start' : 'flex-end')};
    margin: 20px 0;
    width: 100%;
    box-sizing: border-box;
`;

const MessageBox = styled.div`
    position: relative;
    max-width: 400px;
    padding: 0.8em 1em;
    white-space: pre-wrap;
    word-wrap: break-word;
    z-index: 2000;
    border: 1px solid #d4d4d5;
    color: #4b4b4b;
    line-height: 1.4em;
    background: #fff;
    border-radius: 0.3rem;
    box-shadow: 0 2px 4px 0 rgba(34, 36, 38, 0.12),
        0 2px 10px 0 rgba(34, 36, 38, 0.15);

    &:before {
        position: absolute;
        content: '';
        width: 0.7em;
        height: 0.7em;
        background: #fff;
        -webkit-transform: rotate(45deg);
        transform: rotate(45deg);
        z-index: 2;
        box-shadow: 1px 1px 0 0 #bababc;
        bottom: -0.3em;
        left: ${props => (props.left ? '1em' : 'auto')};
        top: auto;
        right: ${props => (!props.left ? '1em' : 'auto')};
        margin-left: 0;
    }
`;

const Message = ({ left, content }) => (
    <MessageRow left={left}>
        <MessageBox left={left}>
            {content.text}
            <br />
            <MessageContent content={content} />
        </MessageBox>
    </MessageRow>
);

Message.propTypes = {
    left: PropTypes.bool.isRequired,
    content: PropTypes.object.isRequired
};

const MessageContent = ({ content }) => {
    switch (content.type) {
        case types.DATE:
            return <p>Date: {moment(content.date).format('MMMM Do YYYY')}</p>;
        case types.AUDIO:
            return <AudioMessage content={content.URL} />;
        case types.VIDEO:
            return (
                <video src={content.URL} controls style={{ width: '350px' }} />
            );
        case types.PHOTO:
        case types.PARAGRAPH:
        case types.HERO:
            return <ImageMessage content={content} />;
        case types.MULTIPLE_SELECT:
        case types.SINGLE_SELECT:
            return <SelectMessage content={content} />;
        default:
            return null;
    }
};

MessageContent.propTypes = {
    content: PropTypes.object.isRequired
};

export default Message;
