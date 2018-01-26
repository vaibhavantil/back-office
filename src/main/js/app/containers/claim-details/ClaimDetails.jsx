import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';

const ClaimDetails = ({ match }) => <h2>Claim details ({match.params.id}) </h2>;


export default withRouter(connect(() => ({}), {})(ClaimDetails));