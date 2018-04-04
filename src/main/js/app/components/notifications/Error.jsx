import React from 'react';
import PropTypes from 'prop-types';
import { Message } from 'semantic-ui-react';

export default class Error extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        //eslint-disable-next-line
        setTimeout(() => {
            this.props.closeHandler(this.props.content.id);
        }, 5000);
    }

    render() {
        const { closeHandler, content: { header, id, message } } = this.props;
        return (
            <Message onDismiss={closeHandler.bind(this, id)} negative>
                {header ? <Message.Header>{header}</Message.Header> : null}
                <Message.Content>{message}</Message.Content>
            </Message>
        );
    }
}

Error.propTypes = {
    closeHandler: PropTypes.func.isRequired,
    content: PropTypes.object.isRequired
};
