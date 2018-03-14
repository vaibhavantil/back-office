import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import { Button, Header } from 'semantic-ui-react';

const AnswerInfo = ({ user, redirectClick }) => (
    <React.Fragment>
        <Header size="medium">Admin answer: {user.answer}</Header>
        <Header size="medium">
            Date: {moment(user.date).format('Do MMMM YYYY HH:mm')}
        </Header>
        <Button
            style={{ marginBottom: '3px' }}
            content="Open Chat"
            onClick={redirectClick.bind(this, user.hid)}
        />
    </React.Fragment>
);

AnswerInfo.propTypes = {
    user: PropTypes.object.isRequired,
    redirectClick: PropTypes.func.isRequired
};

export default AnswerInfo;
