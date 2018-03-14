import React from 'react';
import PropTypes from 'prop-types';
import { Header } from 'semantic-ui-react';
import { getUserInfo } from 'app/lib/helpers';
import Message from 'components/chat/messages/Message';

const Question = ({ activeList, question, usersList }) => {

    const userInfo = getUserInfo(usersList, question.hid);

    return (
        <React.Fragment>
            <Header>Questions from: {userInfo}</Header>
            {activeList[question.hid] &&
                activeList[question.hid].map(data => (
                    <div key={data.id}>
                        <Message
                            content={data.message.body}
                            left={!data.answer}
                            isQuestionMessage={true}
                            timestamp={data.date}
                            from={userInfo}
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
                    </div>
                ))}
        </React.Fragment>
    );
};

Question.propTypes = {
    question: PropTypes.object.isRequired,
    usersList: PropTypes.array,
    activeList: PropTypes.object
};

export default Question;
