import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import styled from 'styled-components';
import { Button, Checkbox, Grid, Segment } from 'semantic-ui-react';
import { history } from 'app/store';

const List = styled.div`
    display: flex;
    flex-direction: column;
    margin: 0 auto;
    width: 700px;
`;

const QuestionContent = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
`;

const GridColumn = styled(Grid.Column)`
    &&& {
        display: flex !important;
        flex-direction: row;
        justify-content: space-between;
        align-items: flex-start;
    }
`;

const answerClick = id => {
    history.push(`/members/${id}`);
};

const QuestionsList = ({ questions }) => (
    <List>
        {questions.map(data => (
            <Grid key={data.id}>
                <Grid.Row columns={1}>
                    <Grid.Column>
                        <QuestionContent>
                            <span>
                                {moment(data.date.slice(0, -1)).format(
                                    'DD MM YYYY HH:mm'
                                )}
                            </span>
                            <span>
                                User:{' '}
                                {`${data.member.firstName} ${data.member
                                    .lastName || ''}`}
                            </span>
                            <span>Admin: {`${data.personnel.email}`}</span>
                            <span>
                                <Checkbox disabled checked={!!data.answer} />
                            </span>
                        </QuestionContent>
                    </Grid.Column>
                </Grid.Row>
                <Grid.Row columns={1}>
                    <GridColumn>
                        <Segment>Question: {data.message.body.text}</Segment>
                        <Button
                            content="Answer"
                            onClick={answerClick.bind(this, data.member.hid)}
                            primary
                        />
                    </GridColumn>
                </Grid.Row>
            </Grid>
        ))}
    </List>
);

QuestionsList.propTypes = {
    questions: PropTypes.array.isRequired
};

export default QuestionsList;
