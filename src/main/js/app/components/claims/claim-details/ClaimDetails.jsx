import React from 'react';
import BackLink from 'components/shared/link/BackLink';
import ClaimInfo from '../claim-info/ClaimInfo';
import Notes from '../notes/Notes';
export default class ClaimDetails extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const { match: { params }, claimRequest, claimTypes } = this.props;
        claimRequest(params.id);
        claimTypes();
    }

    render() {
        const {
            match,
            claimDetails,
            types,
            userRequest,
            user,
            claimUpdate
        } = this.props;
        return (
            <React.Fragment>
                <BackLink path="claims" />
                {claimDetails.data &&
                    types && (
                        <ClaimInfo
                            id={match.params.id}
                            typeOptions={types}
                            claim={claimDetails.data}
                            userRequest={userRequest}
                            claimUpdate={claimUpdate}
                            user={user}
                        />
                    )}
                <Notes />
            </React.Fragment>
        );
    }
}
