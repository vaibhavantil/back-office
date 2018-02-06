import React from 'react';
import PropTypes from 'prop-types';
import { Checkbox } from 'semantic-ui-react';
import chatInputs from '../../chat/chat/ChatInputs';
import * as types from 'app/lib/messageTypes';

const messagesWithFiles = [
    types.AUDIO,
    types.VIDEO,
    types.PHOTO,
    types.PARAGRAPH,
    types.HERO,
    types.BANK_ID_COLLECT
];

export default class ClaimInfoField extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            inputIsVisible: false,
            messageType: types.TEXT,
            message: {},
            cleanupForm: false
        };
    }

    submitHandler = () => {};

    toggleInput = () => {
        this.setState({
            inputIsVisible: !this.state.inputIsVisible
        });
    };

    inputHandler = (type, e, { value }) => {
        this.setState({
            message: {
                ...this.state.message,
                [type]: value
            }
        });
    };

    getInput = (type = 'text') => {
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

        return <React.Fragment>{input}</React.Fragment>;
    };

    render() {
        const { fieldData } = this.props;
        const fieldInput = this.state.inputIsVisible
            ? this.getInput(types[fieldData.type])
            : null;
        return (
            <div>
                <h3>{fieldData.name}</h3>
                <Checkbox onChange={this.toggleInput} />
                {fieldInput}
            </div>
        );
    }
}

ClaimInfoField.propTypes = {
    fieldData: PropTypes.object.isRequired
};
