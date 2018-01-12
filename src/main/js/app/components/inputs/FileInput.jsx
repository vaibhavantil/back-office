import React from 'react';
import { Input } from 'semantic-ui-react';

export default class FileInput extends React.Component {
    constructor(props) {
        super(props);
        this.changeHandler = this.changeHandler.bind(this);
    }

    changeHandler(e) {
        // eslint-disable-next-line
        const reader = new FileReader();
        const file = e.target.files[0];

        reader.onloadend = () => {
            this.props.changeHandler(reader.result);
        };

        reader.readAsDataURL(file);
    }

    render() {
        return <Input type="file" onChange={this.changeHandler} />;
    }
}
