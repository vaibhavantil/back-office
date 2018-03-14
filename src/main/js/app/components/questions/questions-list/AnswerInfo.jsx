import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import moment from 'moment';
import { Button, Header, Label, Segment } from 'semantic-ui-react';

const ChatLinkButton = styled(Button)`
    &&& {
        float: right;
    }
`;

const AnswerInfo = ({ user, redirectClick }) => (
    <Segment>
        <Label ribbon>
            <Label.Detail>
                {moment(user.date).format('Do MM YYYY HH:mm')}
            </Label.Detail>
        </Label>
        <ChatLinkButton
            content="Open Chat"
            onClick={redirectClick.bind(this, user.hid)}
        />
        <Header size="medium">Admin answer:</Header>
        <Header size="small">{user.answer}</Header>
    </Segment>
);

AnswerInfo.propTypes = {
    user: PropTypes.object.isRequired,
    redirectClick: PropTypes.func.isRequired
};

export default AnswerInfo;
