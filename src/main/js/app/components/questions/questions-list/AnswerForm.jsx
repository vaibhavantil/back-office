import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Button, Form, TextArea } from 'semantic-ui-react';
import { history } from 'app/store';

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

    chatRedirectClick = (id, msgId) => {
        history.push(`/members/${id}/${msgId}`);
    };

    render() {
        const id = this.props.hid;
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
                            onClick={this.chatRedirectClick.bind(this, id)}
                        />
                        <Button
                            content="Send"
                            onClick={this.answerClick.bind(this, id)}
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
    sendAnswer: PropTypes.func.isRequired
};
