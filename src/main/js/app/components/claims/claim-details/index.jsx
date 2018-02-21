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
        const { match, claimRequest, claimTypes } = this.props;
        const id = match.params.id;
        claimRequest(id);
        claimTypes();
    }

    render() {
        const {
            claimDetails,
            createNote,
            updateReserve,
            createPayment,
            match
        } = this.props;
        return (
            <ClaimDetailsContainer>
                <Header size="huge">Claim Details</Header>

                <BackLink path="claims" />
                {claimDetails.data ? (
                    <React.Fragment>
                        <ClaimInfo {...this.props} />
                        <Notes
                            notes={claimDetails.data.notes}
                            createNote={createNote}
                            id={match.params.id}
                        />
                        <Payments
                            claimDetails={claimDetails}
                            updateReserve={updateReserve}
                            createPayment={createPayment}
                            id={match.params.id}
                        />
                    </React.Fragment>
                ) : null}
            </ClaimDetailsContainer>
        );
    }
}

ClaimDetails.propTypes = {
    claimDetails: PropTypes.object.isRequired,
    createNote: PropTypes.func.isRequired,
    updateReserve: PropTypes.func.isRequired,
    match: PropTypes.object.isRequired,
    claimRequest: PropTypes.func.isRequired,
    claimTypes: PropTypes.func.isRequired,
    createPayment: PropTypes.func.isRequired
};
