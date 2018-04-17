import React from 'react';
import PropTypes from 'prop-types';
import { Message } from 'semantic-ui-react';

export default class Notification extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const { content: { id }, closeHandler } = this.props;
        //eslint-disable-next-line
        setTimeout(() => {
            closeHandler(id);
        }, 10000);
    }

    render() {
        const {
            closeHandler,
            content: { header, id, message, success }
        } = this.props;
        return (
            <Message
                onDismiss={closeHandler.bind(this, id)}
                negative={!success}
                positive={success}
            >
                {header ? <Message.Header>{header}</Message.Header> : null}
                <Message.Content>{message}</Message.Content>
            </Message>
        );
    }
}

Notification.propTypes = {
    closeHandler: PropTypes.func.isRequired,
    content: PropTypes.object.isRequired
};
