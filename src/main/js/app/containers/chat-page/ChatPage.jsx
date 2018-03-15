import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import Chat from 'components/chat';
import { PageContainer } from 'components/shared';

const ChatPage = props => (
    <PageContainer>
        <Chat {...props} />
    </PageContainer>
);

const mapStateToProps = ({ messages, claims, client: { token } }) => ({
    messages,
    userClaims: claims.userClaims,
    token
});

export default withRouter(
    connect(mapStateToProps, {
        ...actions.messagesActions,
        ...actions.chatUserActions,
        ...actions.clientActions,
        claimsByUser: actions.claimsActions.claimsByUser
    })(ChatPage)
);
