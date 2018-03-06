import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import SortedList from './SortedList';
import { history } from 'app/store';
import { Tab } from 'semantic-ui-react';

const ListContainer = styled.div`
    display: flex;
    flex-direction: column;
    width: 700px;
    margin: 50px auto;
`;

const answerClick = (id, msgId) => {
    history.push(`/members/${id}/${msgId}`);
};

const QuestionsList = ({ questions, users }) => {
    const panes = [
        { menuItem: 'Not Answered', render: () =>
            <Tab.Pane>
                <SortedList
                    list={questions.notAnswered}
                    users={users}
                    clickHandler={answerClick}
                />
            </Tab.Pane> },
        { menuItem: 'Answered', render: () =>
            <Tab.Pane>
                <SortedList
                    list={questions.answered}
                    users={users}
                    clickHandler={answerClick}
                />
            </Tab.Pane> }
    ];

    return (
        <ListContainer>
            <Tab renderActiveOnly={true} panes={panes} />
        </ListContainer>
    )
};

QuestionsList.propTypes = {
    questions: PropTypes.object.isRequired,
    users: PropTypes.array
};

export default QuestionsList;
