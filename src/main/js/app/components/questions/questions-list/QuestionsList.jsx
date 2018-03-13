import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import SortedList from './SortedList';
import { Tab } from 'semantic-ui-react';

const ListContainer = styled.div`
    display: flex;
    flex-direction: column;
    width: 700px;
    margin: 50px auto;
`;

const QuestionsList = ({ questions, users, sendAnswer, tabChange }) => {
    const notAnswered = () => (
        <Tab.Pane>
            <SortedList
                list={questions.notAnswered}
                users={users}
                sendAnswer={sendAnswer}
            />
        </Tab.Pane>
    );

    const answered = () => (
        <Tab.Pane>
            <SortedList
                list={questions.answered}
                users={users}
                sendAnswer={sendAnswer}
            />
        </Tab.Pane>
    );

    const panes = [
        { menuItem: 'Not Answered', render: notAnswered },
        { menuItem: 'Answered', render: answered }
    ];

    return (
        <ListContainer>
            <Tab
                renderActiveOnly={true}
                panes={panes}
                onTabChange={tabChange}
            />
        </ListContainer>
    );
};

QuestionsList.propTypes = {
    questions: PropTypes.object.isRequired,
    sendAnswer: PropTypes.func.isRequired,
    tabChange: PropTypes.func.isRequired,
    users: PropTypes.array
};

export default QuestionsList;
