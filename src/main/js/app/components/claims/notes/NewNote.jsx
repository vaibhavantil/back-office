import React from 'react';
import { Form } from 'semantic-ui-react';
import FileInput from 'components/chat/inputs/FileInput';

export default class NewNote extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            text: '',
            file: null,
            cleanupForm: false
        };
    }

    textChangeHandler = (e, { value }) => {
        this.setState({ text: value });
    };

    fileChangeHandler = (type, e, { value }) => {
        this.setState({ file: JSON.stringify(value) });
    };

    createClickHandler = () => {
        const { createNote, id } = this.props;
        const { text, file } = this.state;
        createNote(id, { text, file });
        this.setState(
            {
                text: '',
                file: '',
                cleanupForm: true
            },
            () => {
                this.setState({
                    cleanupForm: false
                });
            }
        );
    };

    render() {
        return (
            <Form onSubmit={this.createClickHandler}>
                <Form.Group>
                    <Form.Input
                        onChange={this.textChangeHandler}
                        placeholder="Note text..."
                    />
                    <Form.Button primary content="Add" />
                </Form.Group>
                <FileInput
                    changeHandler={this.fileChangeHandler}
                    cleanupForm={this.state.cleanupForm}
                />
            </Form>
        );
    }
}
