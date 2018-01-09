import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Container } from 'semantic-ui-react';
import actions from 'app/store/actions';
import { checkAuthorization } from 'app/lib/checkAuth';
import ChatsList from 'components/messages/ChatsList';

class MessagesSearch extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        checkAuthorization(null, this.props.setClient);
    }

    render() {
        return (
            <Container>
                <h1>Chats List</h1>
                <ChatsList chats={this.props.messages.users} />
            </Container>
        );
    }
}

const mapStateToProps = ({ client, messages }) => ({
    client,
    messages
});

export default withRouter(connect(mapStateToProps, {
    ...actions.clientActions
})(MessagesSearch));
