import React from 'react';
import PropTypes from 'prop-types';
import { Form, TextArea } from 'semantic-ui-react';
import { TEXT } from 'app/lib/messageTypes';

export default class TextInput extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            textValue: ''
        };
    }

    textChangeHandler = (e, { value }) => {
        this.setState({ textValue: value });
        this.props.changeHandler(TEXT, null, { value });
    };

    componentWillMount() {
        this.setState({ textValue: this.props.value || '' });
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.cleanupForm) {
            this.setState({ textValue: '' });
        }
    }

    render() {
        return (
            <Form.Field>
                <label>Text</label>
                <TextArea
                    autoHeight
                    onChange={this.textChangeHandler}
                    value={this.state.textValue}
                />
            </Form.Field>
        );
    }
}

TextInput.propTypes = {
    changeHandler: PropTypes.func.isRequired,
    cleanupForm: PropTypes.bool,
    value: PropTypes.string
};
