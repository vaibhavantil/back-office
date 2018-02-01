import React from 'react';
import { Form, Input } from 'semantic-ui-react';
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
                <Input
                    fluid
                    onChange={this.textChangeHandler}
                    value={this.state.textValue}
                />
            </Form.Field>
        );
    }
}
