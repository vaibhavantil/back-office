import React from 'react';
import PropTypes from 'prop-types';
import { Header } from 'semantic-ui-react';
import QuestionsList from './questions-list/QuestionsList';

export default class Questions extends React.Component {
    constructor(props) {
        super(props);
    }

    tabChange = () => {
        this.props.questionsRequest();
    };

    componentDidMount() {
        this.props.questionsRequest();
        this.props.membersRequest();
    }

    render() {
        const { questions, members: { list }, sendAnswer } = this.props;
        return (
            <React.Fragment>
                <Header size="huge">Questions</Header>
                <QuestionsList
                    questions={questions.list}
                    sendAnswer={sendAnswer}
                    tabChange={this.tabChange}
                    members={list}
                />
            </React.Fragment>
        );
    }
}

Questions.propTypes = {
    members: PropTypes.object.isRequired,
    questions: PropTypes.object.isRequired,
    membersRequest: PropTypes.func.isRequired,
    questionsRequest: PropTypes.func.isRequired,
    sendAnswer: PropTypes.func.isRequired
};
