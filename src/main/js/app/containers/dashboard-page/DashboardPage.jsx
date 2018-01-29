import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import Dashboard from 'components/dashboard/Dashboard';

const DashboardPage = props => <Dashboard {...props} />;

export default withRouter(
    connect(
        ({ client, messages, dashboard }) => ({
            client,
            messages,
            dashboard
        }),
        {
            ...actions.dashboardActions,
            setActiveConnection: actions.messagesActions.setActiveConnection
        }
    )(DashboardPage)
);
