import React from 'react';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import Users from 'components/users/Users';
import BackLink from 'components/link/BackLink';
import { Header } from 'components/chat/Chat';

const UsersListPage = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    max-width: 500px;
    margin: 0 auto;
`;

const UsersPage = ({ users, searchUserRequest, client, usersRequest }) => (
    <UsersListPage>
        <Header>Chats List</Header>
        <BackLink path="dashboard" />
        <Users
            users={users}
            search={searchUserRequest}
            client={client}
            usersRequest={usersRequest}
        />
    </UsersListPage>
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
