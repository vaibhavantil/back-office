import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import Claims from 'components/claims/Claims';

const ClaimsPage = () => <Claims />;

export default withRouter(connect(() => ({}), {})(ClaimsPage));
