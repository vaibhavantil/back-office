import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import Dashboard from 'components/dashboard/Dashboard';

const DashboardPage = ({ unsetClient }) => (
    <Dashboard unsetClient={unsetClient} />
);

export default withRouter(
    connect(
        ({ client }) => ({
            client
        }),
        { ...actions.clientActions }
    )(DashboardPage)
);
