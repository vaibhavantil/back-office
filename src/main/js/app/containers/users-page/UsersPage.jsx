import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import Users from 'components/users/users/Users';
import { ListPage } from 'components/shared';

const UsersPage = props => (
    <ListPage>
        <Users {...props} />
    </ListPage>
);

const mapStateToProps = ({ client, users, messages }) => ({
    client,
    users,
    messages
});

export default withRouter(
    connect(mapStateToProps, {
        ...actions.clientActions,
        ...actions.chatUserActions,
        ...actions.messagesActions
    })(UsersPage)
);
