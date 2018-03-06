import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import moment from 'moment';
import { Button, Checkbox, Grid, Header, Segment } from 'semantic-ui-react';
import Message from 'components/chat/messages/Message';
import Pagination from 'components/shared/pagination/Pagination';

const List = styled(Segment)`
    &&& {
        display: flex;
        flex-direction: column;
        margin: 0 auto;
        padding: 30px;
    }
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
        border-bottom: solid 1px #22242626;
    }
`;

const getUserInfo = (users, id) => {
    const user = users.find(user => user.hid === id);
    return user ? (
        <span>User: {`${user.firstName} ${user.lastName || ''}`}</span>
    ) : (
        <span>User Id: {`${id}`}</span>
    );
};

export default class SortedList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeList: []
        };
    }

    onChangePage = activeList => {
        this.setState({ activeList });
    };

    render() {
        const { list, users, clickHandler } = this.props;
        const { activeList } = this.state;
        return (
            <List>
                {list.length ? (
                    <React.Fragment>
                        {activeList.map(data => (
                            <GridSelection key={data.id}>
                                <Grid.Row columns={1}>
                                    <Grid.Column>
                                        <QuestionContent>
                                            <span>
                                                {moment(data.date).format(
                                                    'DD/MM/YYYY HH:mm'
                                                )}
                                            </span>
                                            {users.length &&
                                                getUserInfo(users, data.hid)}
                                            <span>
                                                {
                                                    data.personnel && data.personnel.email
                                                        ? `Admin: ${data.personnel.email}`
                                                        : null
                                                }
                                            </span>
                                            <span>
                                                <Checkbox
                                                    disabled
                                                    checked={!!data.answer}
                                                />
                                            </span>
                                        </QuestionContent>
                                    </Grid.Column>
                                </Grid.Row>
                                <Grid.Row columns={1}>
                                    <GridColumn>
                                        <Message
                                            content={data.message.body}
                                            left={!data.answer}
                                            isQuestionMessage={true}
                                        />
                                        {!data.answer ? (
                                            <Button
                                                content="Answer"
                                                onClick={clickHandler.bind(
                                                    this,
                                                    data.hid,
                                                    data.message.globalId
                                                )}
                                                primary
                                            />
                                        ) : null}
                                    </GridColumn>
                                </Grid.Row>
                                {
                                    data.answer ?
                                        <Grid.Row columns={1}>
                                            <GridColumn>
                                                <Message
                                                    content={data.answer.body}
                                                    left={true}
                                                    isQuestionMessage={true}
                                                />
                                            </GridColumn>
                                        </Grid.Row>
                                    : null
                                }
                            </GridSelection>
                        ))}
                        <Pagination
                            items={list}
                            onChangePage={this.onChangePage}
                            pageSize={10}
                        />
                    </React.Fragment>
                ) : (
                    <Header>List is empty</Header>
                )}
            </List>
        );
    }
}

SortedList.propTypes = {
    clickHandler: PropTypes.func.isRequired,
    list: PropTypes.array.isRequired,
    users: PropTypes.array
};
