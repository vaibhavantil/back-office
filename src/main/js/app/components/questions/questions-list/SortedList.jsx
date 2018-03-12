import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Header, Segment } from 'semantic-ui-react';
import Message from 'components/chat/messages/Message';
import AnswerForm from './AnswerForm';
import Pagination from 'components/shared/pagination/Pagination';
import { getUserInfo } from 'app/lib/helpers';

const List = styled(Segment)`
    &&& {
        display: flex;
        flex-direction: column;
        margin: 0 auto;
        padding: 30px;
    }
`;

const UserQuestionItem = styled.div`
    border-bottom: solid 1px #22242626;
    padding: 10px 0;
`;

export default class SortedList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeList: {},
            answer: ''
        };
    }

    onChangePage = (listId, list) => {
        // eslint-disable-next-line no-undef
        setTimeout(() => {
            this.setState({
                activeList: {
                    ...this.state.activeList,
                    [listId]: list
                }
            });
        });
    };

    getSender = data =>
        data.personnel && data.personnel.email
            ? `${data.personnel.email}`
            : 'admin';

    getListContent = (data, user) => {
        const users = this.props.users;
        return (
            <React.Fragment>
                <Message
                    content={data.message.body}
                    left={!data.answer}
                    isQuestionMessage={true}
                    timestamp={data.date}
                    from={users.length && getUserInfo(users, user.hid)}
                />
                {data.answer ? (
                    <Message
                        content={data.answer.body}
                        left={true}
                        isQuestionMessage={false}
                        timestamp={data.answerDate}
                        from={this.getSender(data)}
                    />
                ) : null}
            </React.Fragment>
        );
    };

    render() {
        const { list, users, sendAnswer } = this.props;
        const { activeList } = this.state;
        return (
            <List>
                {list.length ? (
                    <React.Fragment>
                        {list.map(user => (
                            <UserQuestionItem key={user.id}>
                                <Header>
                                    Questions from:{' '}
                                    {getUserInfo(users, user.hid)}
                                </Header>
                                {activeList[user.hid] &&
                                    activeList[user.hid].map(data => (
                                        <div key={data.id}>
                                            {this.getListContent(data, user)}
                                        </div>
                                    ))}
                                {!user.answer ? (
                                    <AnswerForm
                                        hid={user.hid}
                                        sendAnswer={sendAnswer}
                                    />
                                ) : (
                                    <Header size="medium">
                                        Admin answer: {user.answer}
                                    </Header>
                                )}
                                <Pagination
                                    items={user.questions}
                                    onChangePage={this.onChangePage.bind(
                                        this,
                                        user.hid
                                    )}
                                    pageSize={10}
                                />
                            </UserQuestionItem>
                        ))}
                    </React.Fragment>
                ) : (
                    <Header>List is empty</Header>
                )}
            </List>
        );
    }
}

SortedList.propTypes = {
    list: PropTypes.array.isRequired,
    sendAnswer: PropTypes.func.isRequired,
    users: PropTypes.array
};
