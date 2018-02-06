import React from 'react';
import PropTypes from 'prop-types';
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

export default class ChatPanel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            messageType: types.TEXT,
            message: {},
            cleanupForm: false
        };
    }

    dropdownHander = (e, { value }) => {
        this.setState({ messageType: value, message: {} });
    };

    submitHandler = () => {
        const { message, messageType } = this.state;
        if (message && messageType) {
            this.props.addMessage(message, messageType);
            this.setState({ message: {}, cleanupForm: true }, () => {
                this.setState({ cleanupForm: false, messageType: types.TEXT });
            });
        }
    };

    inputHandler = (type, e, { value }) => {
        this.setState({
            message: {
                ...this.state.message,
                [type]: value
            }
        });
    };

    getChatInput = type => {
        const { messageType, cleanupForm } = this.state;
        const isFileInput = !!(messagesWithFiles.indexOf(type) + 1);
        const isSelectInput =
            type === types.MULTIPLE_SELECT || type === types.SINGLE_SELECT;

        let input;
        if (isFileInput) {
            input = chatInputs.file(this.inputHandler, type, cleanupForm);
        } else if (isSelectInput) {
            input = chatInputs.select(
                this.inputHandler,
                messageType,
                cleanupForm
            );
        } else {
            input = chatInputs[type](this.inputHandler, cleanupForm);
        }

        return <div style={{ width: '400px' }}>{input}</div>;
    };

    render() {
        const { messageType } = this.state;
        const chatInput = this.getChatInput(messageType);
        return (
            <Form onSubmit={this.submitHandler} className="chat-panel">
                <MessagesPanelContariner>
                    <Form.Field>
                        <label>Message type</label>
                        <Dropdown
                            onChange={this.dropdownHander}
                            options={messageTypes}
                            placeholder="Choose asset state"
                            selection
                            value={this.state.messageType}
                        />
                    </Form.Field>
                    {chatInput}
                    <Form.Button content="Send" primary />
                </MessagesPanelContariner>
            </Form>
        );
    }
}

ChatPanel.propTypes = {
    addMessage: PropTypes.func.isRequired
};
