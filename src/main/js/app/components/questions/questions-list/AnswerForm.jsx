import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Button, Form, TextArea, Message } from 'semantic-ui-react';

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
        this.state = {
            answer: ''
        };
    }

    answerChangeHandler = (e, { value }) => {
        this.setState({ answer: value });
    };

    answerClick = id => {
        if (this.state.answer.trim().length) {
            this.props.sendAnswer({ msg: this.state.answer, id });
        }
    };

    doneClick = id => {
        this.props.sendDoneMsg({ id });
    };

    render() {
        const { hid, redirectClick, error } = this.props;
        return (
            <React.Fragment>
                <Form>
                    <FormGroup>
                        <FormTextArea
                            control={TextArea}
                            label="Answer"
                            placeholder="Answer text..."
                            onChange={this.answerChangeHandler}
                            value={this.state.answer}
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
                            <Button
                                content="Done"
                                onClick={this.doneClick.bind(this, hid)}
                                primary
                            />
                        </div>
                    </FormGroup>
                </Form>
                {error ? <Message negative>{error}</Message> : null}
            </React.Fragment>
        );
    }
}

AnswerForm.propTypes = {
    hid: PropTypes.string.isRequired,
    sendAnswer: PropTypes.func.isRequired,
    sendDoneMsg: PropTypes.func.isRequired,
    redirectClick: PropTypes.func.isRequired,
    error: PropTypes.string
};
