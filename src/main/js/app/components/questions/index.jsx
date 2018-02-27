import React from 'react';
import PropTypes from 'prop-types';
import { Header } from 'semantic-ui-react';
import BackLink from 'components/shared/link/BackLink';
import QuestionsList from './QuestionsList';

export default class Questions extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.props.questionsRequest();
    }

    render() {
        return (
            <React.Fragment>
                <Header size="huge">Questions</Header>
                <BackLink />
                <QuestionsList questions={this.props.questions.list} />
            </React.Fragment>
        );
    }
}

Questions.propTypes = {
    questions: PropTypes.object.isRequired,
    questionsRequest: PropTypes.func.isRequired
};
