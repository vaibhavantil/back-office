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

const mapStateToProps = ({ messages, claims, insurance, client }) => ({
    memberClaims: claims.memberClaims,
    messages,
    insurance,
    client
});

export default withRouter(
    connect(mapStateToProps, {
        claimsByMember: actions.claimsActions.claimsByMember,
        ...actions.insuranceActions,
        ...actions.messagesActions,
        ...actions.membersActions,
        ...actions.clientActions,
        ...actions.notificationsActions
    })(ChatPage)
);
