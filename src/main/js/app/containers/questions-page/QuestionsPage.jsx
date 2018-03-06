import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import Questions from 'components/questions';
import { ListPage } from 'components/shared';

const QuestionsPage = props =>
    <ListPage>
        <Questions {...props} />
    </ListPage>;

const mapStateToProps = ({ client, questions, users }) => ({
    client,
    questions,
    users
});

export default withRouter(
    connect(mapStateToProps, {
        ...actions.clientActions,
        ...actions.questionsActions,
        ...actions.chatUserActions
    })(QuestionsPage)
);
