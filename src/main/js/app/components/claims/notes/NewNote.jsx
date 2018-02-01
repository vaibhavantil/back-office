import React from 'react';
import { Input } from 'semantic-ui-react';
import FileInput from 'components/chat/inputs/FileInput';

export default class NewNote extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            text: '',
            file: null
        };
    }

    textChangeHandler = (e, { value }) => {
        this.setState({ text: value });
    };

    fileChangeHandler = (e, { value }) => {
        this.setState({ file: value });
    };

    removeClickHandler = () => {};

    createClickHandler = () => {};

    render() {
        return (
            <React.Fragment>
                <Input
                    onChange={this.textChangeHandler}
                    placeholder="Note text..."
                />
                <FileInput changeHandler={this.fileChangeHandler} />
            </React.Fragment>
        );
    }
}
