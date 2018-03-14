import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Button, Form, TextArea } from 'semantic-ui-react';

const FormGroup = styled(Form.Group)`
    &&& {
        display: flex;
        align-items: flex-end;
        width: 100%;
        margin-top: 10px;
    }
`;

const FormTextArea = styled(Form.Field)`
    &&& {
        width: 100%;
    }
`;

export default class AnswerForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    answerChangeHandler = (e, { value }) => {
        this.setState({ answer: value });
    };

    answerClick = userId => {
        this.props.sendAnswer({ msg: this.state.answer, userId });
    };

    render() {
        const { hid, redirectClick } = this.props;
        return (
            <Form>
                <FormGroup>
                    <FormTextArea
                        control={TextArea}
                        label="Answer"
                        placeholder="Answer text..."
                        onChange={this.answerChangeHandler}
                    />
                    <div>
                        <Button
                            style={{ marginBottom: '3px' }}
                            content="Open Chat"
                            onClick={redirectClick.bind(this, hid)}
                        />
                        <Button
                            content="Send"
                            onClick={this.answerClick.bind(this, hid)}
                            primary
                        />
                    </div>
                </FormGroup>
            </Form>
        );
    }
}

AnswerForm.propTypes = {
    hid: PropTypes.string.isRequired,
    sendAnswer: PropTypes.func.isRequired,
    redirectClick: PropTypes.func.isRequired
};
