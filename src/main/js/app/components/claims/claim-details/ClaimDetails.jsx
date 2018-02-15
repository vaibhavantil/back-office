import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Header } from 'semantic-ui-react';
import BackLink from 'components/shared/link/BackLink';
import ClaimInfo from '../claim-info/ClaimInfo';
import Notes from '../notes/Notes';
import Payments from '../payments/Payments';

const ClaimDetailsContainer = styled.div`
    max-width: 1000px;
    margin: 0 auto 50px;
`;
export default class ClaimDetails extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const {
            match,
            claimRequest,
            claimTypes,
            notesRequest,
            paymentsRequest
        } = this.props;
        const id = match.params.id;
        claimRequest(id);
        claimTypes();
        notesRequest(id);
        paymentsRequest(id);
    }

    render() {
        const {
            claimDetails,
            createNote,
            removeNote,
            updateResume,
            createPayment,
            updatePayment,
            removePayment,
            match
        } = this.props;
        return (
            <ClaimDetailsContainer>
                <Header size="huge">Claim Details</Header>

                <BackLink path="claims" />
                {claimDetails.data ? <ClaimInfo {...this.props} /> : null}
                <Notes
                    notes={claimDetails.notes}
                    removeNote={removeNote}
                    createNote={createNote}
                    id={match.params.id}
                />
                <Payments
                    claimDetails={claimDetails}
                    updateResume={updateResume}
                    createPayment={createPayment}
                    updatePayment={updatePayment}
                    removePayment={removePayment}
                    id={match.params.id}
                    notes={claimDetails.notes}
                />
            </ClaimDetailsContainer>
        );
    }
}

ClaimDetails.propTypes = {
    claimDetails: PropTypes.object.isRequired,
    createNote: PropTypes.func.isRequired,
    removeNote: PropTypes.func.isRequired,
    updateResume: PropTypes.func.isRequired,
    match: PropTypes.object.isRequired,
    claimRequest: PropTypes.func.isRequired,
    claimTypes: PropTypes.func.isRequired,
    notesRequest: PropTypes.func.isRequired,
    paymentsRequest: PropTypes.func.isRequired,
    createPayment: PropTypes.func.isRequired,
    updatePayment: PropTypes.func.isRequired,
    removePayment: PropTypes.func.isRequired
};
