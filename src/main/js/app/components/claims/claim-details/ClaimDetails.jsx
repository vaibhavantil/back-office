import React from 'react';
import BackLink from 'components/shared/link/BackLink'
export default class ClaimDetails extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const { match: { params }, claimRequest } = this.props;
        claimRequest(params.id);
    }

    render() {
        const { claimDetails } = this.props;
        return (
            <React.Fragment>
                <BackLink path="claims"/>
                <pre>
                    <code>{JSON.stringify(claimDetails.data, null, 4)}</code>
                </pre>
            </React.Fragment>
        );
    }
}
