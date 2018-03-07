import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Button, Header, Segment } from 'semantic-ui-react';
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

const MessageList = styled.div`
    border-bottom: solid 1px #22242626;
    padding: 10px 0;
`;

const ChatControlsContainer = styled.div`
    text-align: right;
`;


const getUserInfo = (users, id) => {
    const user = users.find(user => user.hid === id);
    return user ? `${user.firstName} ${user.lastName || ''}` : `id: ${id}`;
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
                            <MessageList key={data.id}>
                                <Message
                                    content={data.message.body}
                                    left={!data.answer}
                                    isQuestionMessage={true}
                                    timestamp={data.date}
                                    from={users.length && getUserInfo(users, data.hid)}
                                />
                                {
                                    data.answer ?
                                        <Message
                                            content={data.answer.body}
                                            left={true}
                                            isQuestionMessage={false}
                                            timestamp={data.answerDate}
                                            from={
                                                data.personnel && data.personnel.email
                                                    ? `${data.personnel.email}`
                                                    : 'admin'
                                            }
                                        />
                                        : null
                                }
                                <ChatControlsContainer>
                                    <Button
                                        content="Open Chat"
                                        onClick={clickHandler.bind(
                                            this,
                                            data.hid,
                                            data.message.globalId
                                        )}
                                        primary
                                    />
                                </ChatControlsContainer>
                            </MessageList>
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
