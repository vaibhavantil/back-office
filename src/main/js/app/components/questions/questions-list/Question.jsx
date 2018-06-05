import React from "react";
import PropTypes from "prop-types";
import { Header } from "semantic-ui-react";
import { getMemberInfo } from "app/lib/helpers";
import Message from "components/chat/messages/Message";
import { history } from "app/store";

const Question = ({ activeList, question, membersList }) => {
  const memberInfo = getMemberInfo(membersList, question.memberId);

  return (
    <React.Fragment>
      <Header>
        Questions from:{" "}
        <a
          href="#"
          onClick={() =>
            history.push(`/members/${question.memberId}`, { to: "details" })
          }
        >
          {memberInfo}
        </a>
      </Header>
      {activeList[question.memberId] &&
        activeList[question.memberId].map(data => (
          <div key={data.id}>
            <Message
              content={data.message.body}
              left={!data.answer}
              isQuestionMessage={true}
              timestamp={data.date}
              from={memberInfo}
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
  membersList: PropTypes.array,
  activeList: PropTypes.object
};

export default Question;
