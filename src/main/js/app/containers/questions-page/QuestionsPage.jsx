import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import Questions from 'components/questions';

const QuestionsPage = props => <Questions {...props} />;

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
    })(QuestionsPage)
);
