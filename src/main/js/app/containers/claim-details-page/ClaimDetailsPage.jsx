import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import ClaimDetails from 'components/claims/claim-details/ClaimDetails';

const ClaimDetailsPage = props => <ClaimDetails {...props} />;

export default withRouter(connect(() => ({}), {})(ClaimDetailsPage));
