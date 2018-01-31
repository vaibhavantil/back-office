import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import styled from 'styled-components';
import actions from 'app/store/actions';
import Chat from 'components/chat/chat/Chat';

export const PageContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100%;
    background-color: #f5f5f5;
`;
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
