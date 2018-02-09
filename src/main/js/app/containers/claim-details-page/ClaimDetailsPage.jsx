import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import ClaimDetails from 'components/claims/claim-details/ClaimDetails';
import actions from 'app/store/actions';

const ClaimDetailsPage = props => <ClaimDetails {...props} />;

const mapStateToProps = ({ claimDetails, claims, messages }) => ({
    claimDetails,
    types: claims.types,
    user: messages.user
});

export default withRouter(
    connect(mapStateToProps, {
        ...actions.claimDetailsActions,
        ...actions.notesActions,
        ...actions.paymentActions,
        userRequest: actions.messagesActions.userRequest,
        claimTypes: actions.claimsActions.claimTypes
    })(ClaimDetailsPage)
);
