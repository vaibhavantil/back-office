import React from 'react';
import PropTypes from 'prop-types';
import { Header } from 'semantic-ui-react';
import BackLink from 'components/shared/link/BackLink';
import QuestionsList from './questions-list/QuestionsList';

export default class Questions extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.props.questionsRequest();
        this.props.usersRequest();
    }

    render() {
        const { questions, users: { list } } = this.props;
        return (
            <React.Fragment>
                <Header size="huge">Questions</Header>
                <BackLink path="dashboard" />
                <QuestionsList questions={questions.list} users={list} />
            </React.Fragment>
        );
    }
}

Questions.propTypes = {
    users: PropTypes.object.isRequired,
    questions: PropTypes.object.isRequired,
    usersRequest: PropTypes.func.isRequired,
    questionsRequest: PropTypes.func.isRequired
};
