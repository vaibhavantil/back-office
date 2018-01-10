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
        this.props.chatsRequest();
    }

    render() {
        return (
            <Container>
                <h1>Chats List</h1>
                <ChatsList chats={this.props.chats.list} />
            </Container>
        );
    }
}

const mapStateToProps = ({ client, chats, messages }) => ({
    client,
    chats,
    messages
});

export default withRouter(
    connect(mapStateToProps, {
        ...actions.clientActions,
        ...actions.chatUserActions
    })(MessagesSearch)
);
