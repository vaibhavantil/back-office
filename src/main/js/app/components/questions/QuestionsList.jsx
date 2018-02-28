import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import styled from 'styled-components';
import { Button, Checkbox, Grid } from 'semantic-ui-react';
import Message from 'components/chat/messages/Message';
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

const GridSelection = styled(Grid)`
    &&& {
        border-bottom: solid 1px #ccc;
    }
`

const answerClick = (id, msgId) => {
    history.push(`/members/${id}/${msgId}`);
};

const getUserInfo = (users, id) => {
    const user = users.find(user => user.hid === id);
    return user ? (
        <span>User: {`${user.firstName} ${user.lastName || ''}`}</span>
    ) : (
        <span>User</span>
    );
};

const QuestionsList = ({ questions, users }) => (
    <List>
        {questions.map(data => (
            <GridSelection key={data.id}>
                <Grid.Row columns={1}>
                    <Grid.Column>
                        <QuestionContent>
                            <span>
                                {moment(data.date).format('DD/MM/YYYY HH:mm')}
                            </span>
                            {users.length && getUserInfo(users, data.hid)}
                            <span>
                                Admin:{' '}
                                {`${data.personnel && data.personnel.email}`}
                            </span>
                            <span>
                                <Checkbox disabled checked={!!data.answer} />
                            </span>
                        </QuestionContent>
                    </Grid.Column>
                </Grid.Row>
                <Grid.Row columns={1}>
                    <GridColumn>
                        <Message content={data.message.body} left={true} isQuestionMessage={true}/>
                        <Button
                            content="Answer"
                            onClick={answerClick.bind(this, data.hid, data.message.globalId)}
                            primary
                        />
                    </GridColumn>
                </Grid.Row>
            </GridSelection>
        ))}
    </List>
);

QuestionsList.propTypes = {
    questions: PropTypes.array.isRequired,
    users: PropTypes.array
};

export default QuestionsList;
