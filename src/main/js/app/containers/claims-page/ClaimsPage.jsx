import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import Claims from 'components/claims/claims/Claims';
import { PageContainer } from '../messages-page/ChatPage';
import actions from 'app/store/actions';

const ClaimsPage = props => (
    <PageContainer>
        <Claims {...props} />
    </PageContainer>
);

export default withRouter(
    connect(({ claims }) => ({ claims }), {
        ...actions.claimsActions
    })(ClaimsPage)
);
