import React from 'react';
import PropTypes from 'prop-types';
import Error from './Error';

const Notifications = ({ notifications, dismissNotification }) => (
    <React.Fragment>
        {notifications.map((item, id) => (
            <React.Fragment key={item.id || id}>
                <Error
                    closeHandler={dismissNotification}
                    content={{ ...item }}
                />
            </React.Fragment>
        ))}
    </React.Fragment>
);

Notifications.propTypes = {
    notifications: PropTypes.array.isRequired,
    dismissNotification: PropTypes.func.isRequired
};

export default Notifications;
