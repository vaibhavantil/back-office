import React from 'react';
import PropTypes from 'prop-types';
import { List, Segment, Header } from 'semantic-ui-react';
import moment from 'moment';

const EventsLog = ({ events }) => (
    <Segment>
        <Header>Events log</Header>
        <List selection>
            {events.map((event, id) => (
                <List.Item key={event.id || id}>
                    <List.Content floated="left">
                        {moment(event.date).format('DD MM YYYY')}
                    </List.Content>
                    <List.Content floated="right">{event.text}</List.Content>
                </List.Item>
            ))}
        </List>
    </Segment>
);

EventsLog.propTypes = {
    events: PropTypes.array.isRequired
};

export default EventsLog;
