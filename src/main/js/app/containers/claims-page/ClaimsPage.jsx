import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import Claims from 'components/claims/Claims';
import { PageContainer } from '../messages-page/ChatPage';

const ClaimsPage = () => (
    <PageContainer>
        <Claims />
    </PageContainer>
);

export default withRouter(connect(() => ({}), {})(ClaimsPage));
