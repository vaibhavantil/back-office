import React from 'react';
import { Form, Dropdown } from 'semantic-ui-react';
import styled from 'styled-components';
import { messageTypes } from 'app/lib/selectOptions';
import chatInputs from './ChatInputs';

const MessagesPanelContariner = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    align-items: flex-start;
    padding: 20px 0;
    border-top: solid 2px #e8e5e5;
`;

export default class MessgesPanel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            messageType: 'Text',
            message: null
        };
        this.dropdownHander = this.dropdownHander.bind(this);
        this.submitHandler = this.submitHandler.bind(this);
        this.inputHandler = this.inputHandler.bind(this);
    }

    dropdownHander(e, { value }) {
        this.setState({ messageType: value });
    }

    submitHandler() {
        this.props.addMessage(this.state.message);
        this.setState({ message: '' });
    }

    inputHandler(value) {
        this.setState({ message: value });
    }

    getChatInput(type) {
        return chatInputs[type](this.inputHandler);
    }

    render() {
        const chatInput = this.getChatInput(this.state.messageType);
        return (
            <Form onSubmit={this.submitHandler}>
                <MessagesPanelContariner>
                    <Dropdown
                        onChange={this.dropdownHander}
                        options={messageTypes}
                        placeholder="Choose asset state"
                        selection
                        value={this.state.messageType}
                    />
                    {chatInput}
                    <Form.Button content="Send" primary />
                </MessagesPanelContariner>
            </Form>
        );
    }
}
