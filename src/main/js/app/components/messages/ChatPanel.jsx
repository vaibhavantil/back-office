import React from 'react';
import { Form, Dropdown } from 'semantic-ui-react';
import styled from 'styled-components';
import { messageTypes } from 'app/lib/selectOptions';
import chatInputs from './ChatInputs';
import * as types from 'app/lib/messageTypes';

const messagesWithFiles = [
    types.AUDIO,
    types.VIDEO,
    types.PHOTO,
    types.PARAGRAPH,
    types.HERO,
    types.BANK_ID_COLLECT
];

const MessagesPanelContariner = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-start;
    padding: 20px;
    border-top: solid 2px #e8e5e5;
`;

export default class MessgesPanel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            messageType: types.TEXT,
            message: {}
        };
        this.dropdownHander = this.dropdownHander.bind(this);
        this.submitHandler = this.submitHandler.bind(this);
        this.inputHandler = this.inputHandler.bind(this);
    }

    dropdownHander(e, { value }) {
        this.setState({ messageType: value, message: {} });
    }

    submitHandler() {
        const { message, messageType } = this.state;
        if (message && messageType) {
            this.props.addMessage(message, messageType);
            this.setState({ message: {} });
        }
    }

    inputHandler(type, e, { value }) {
        this.setState({
            message: {
                ...this.state.message,
                [type]: value
            }
        });
    }

    getChatInput(type) {
        const { messageType } = this.state;
        const isFileInput = !!(messagesWithFiles.indexOf(type) + 1);
        const isSelectInput =
            type === types.MULTIPLE_SELECT || type === types.SINGLE_SELECT;

        let input;
        if (isFileInput) {
            input = chatInputs.file(this.inputHandler, type);
        } else if (isSelectInput) {
            input = chatInputs.select(this.inputHandler, messageType);
        } else {
            input = chatInputs[type](this.inputHandler, messageType);
        }

        return <div style={{ width: '400px' }}>{input}</div>;
    }

    render() {
        const { messageType } = this.state;
        const chatInput = this.getChatInput(messageType);
        return (
            <Form onSubmit={this.submitHandler} style={{ zIndex: 10000 }}>
                <MessagesPanelContariner>
                    <Form.Field>
                        <label>Message type</label>
                        <Dropdown
                            onChange={this.dropdownHander}
                            options={messageTypes}
                            placeholder="Choose asset state"
                            selection
                            value={this.state.messageType}
                            style={{ minWidth: '100px' }}
                        />
                    </Form.Field>
                    {chatInput}
                    <Form.Button
                        content="Send"
                        primary
                        style={{ marginTop: '23px' }}
                    />
                </MessagesPanelContariner>
            </Form>
        );
    }
}
