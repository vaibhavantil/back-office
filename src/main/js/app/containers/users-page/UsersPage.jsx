import React from 'react';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import Users from 'components/users/users/Users';
import BackLink from 'components/common/link/BackLink';
import { Header } from 'components/chat/chat/Chat';

const UsersListPage = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    max-width: 500px;
    margin: 0 auto;
`;

const UsersPage = props => (
    <UsersListPage>
        <Header>Chats List</Header>
        <BackLink path="dashboard" />
        <Users {...props} />
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
