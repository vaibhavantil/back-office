import React from 'react';
import styled from 'styled-components';

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

/* eslint-disable react/prop-types */
const Message = ({ left, content }) => (
    <MessageRow left={left}>
        <MessageBox left={left}>{content}</MessageBox>
    </MessageRow>
);

export default Message;
