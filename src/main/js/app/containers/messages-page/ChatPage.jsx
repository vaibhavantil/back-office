import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import Chat from 'components/chat/chat/Chat';
import { PageContainer } from 'components/shared';

const ChatPage = props => (
    <PageContainer>
        <Chat {...props} />
    </PageContainer>
);

const mapStateToProps = ({ messages }) => {
    return {
        messages
    };
};

export default withRouter(
    connect(mapStateToProps, {
        ...actions.messagesActions,
        ...actions.chatUserActions,
        ...actions.clientActions
    })(ChatPage)
);
