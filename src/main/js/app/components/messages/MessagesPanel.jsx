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

/* eslint-disable react/prop-types */
export default class MessgesPanel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            messageType: 'Text'
        };
        this.dropdownHander = this.dropdownHander.bind(this);
        this.clickHandler = this.clickHandler.bind(this);
    }

    dropdownHander(e, { value }) {
        this.setState({ messageType: value });
    }

    clickHandler() {
        this.props.addMessage(this.state.msgContent);
    }

    inputHandler() {
        /* eslint-disable */
        console.log(arguments)
    }

    getChatInput(type) {
        return chatInputs[type](this.inputHandler);
    }

    render() {

        const chatInput = this.getChatInput(this.state.messageType);
        return (
            <Form onSubmit={this.clickHandler}>
                <MessagesPanelContariner>
                    <Dropdown
                        onChange={this.dropdownHander}
                        options={messageTypes}
                        placeholder="Choose asset state"
                        selection
                        value={this.state.messageType}
                    />
                    { chatInput }
                    <Form.Button content='Send' primary />
                </MessagesPanelContariner>
            </Form>
        );
    }
}
