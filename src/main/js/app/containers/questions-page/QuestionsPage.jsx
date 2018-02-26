import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import Questions from 'components/questions';

const QuestionsPage = props => <Questions {...props} />;

const mapStateToProps = ({ client, questions, messages }) => ({
    client,
    questions,
    messages
});

export default withRouter(
    connect(mapStateToProps, {
        ...actions.clientActions,
        ...actions.questionsActions,
        ...actions.messagesActions
    })(QuestionsPage)
);
