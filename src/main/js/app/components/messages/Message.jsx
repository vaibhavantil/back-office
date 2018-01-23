import React from 'react';
import styled from 'styled-components';
import moment from 'moment';
import 'moment/locale/sv';

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

const MessageImage = styled.img`
    margin-top: 5px;
    background-image: url(${props => props.src});
    height: 300px;
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

const Image = ({ src }) => (
    <a target="_blank" href={src}>
        <MessageImage src={src} />
    </a>
);

const SelectList = ({ content }) => {
    const list = content.choices.map((item, id) => {
        if (item.type === 'link') {
            return (
                <li key={id}>
                    <a href={item.appUrl || item.webUrl || item.view}>
                        {item.text}
                    </a>
                </li>
            );
        } else {
            return <li key={id}>{item.text}</li>;
        }
    });

    return <ul>{list}</ul>;
};

const MessageContent = ({ content }) => {
    /* eslint-disable no-case-declarations */
    switch (content.type) {
        case types.DATE:
            return (
                <span>
                    Date:{' '}
                    {Array.isArray(content.date)
                        ? moment(content.date).format('MMMM Do YYYY')
                        : moment(JSON.parse(content.date)).format(
                              'MMMM Do YYYY'
                          )}
                </span>
            );
        case types.AUDIO:
            return <audio src={content.URL} controls="controls" />;
        case types.VIDEO:
            return (
                <video
                    src={content.URL}
                    controls="controls"
                    style={{ width: '350px' }}
                />
            );
        case types.PHOTO:
        case types.PARAGRAPH:
        case types.HERO:
            const { URL, imageUri, imageURL } = content;
            const url = URL || imageUri || imageURL;
            return url ? <Image src={url} /> : null;
        case types.MULTIPLE_SELECT:
        case types.SINGLE_SELECT:
            return <SelectList content={content} />;
        default:
            return null;
    }
};

export default Message;
