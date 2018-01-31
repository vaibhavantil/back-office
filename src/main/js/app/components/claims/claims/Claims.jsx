import React from 'react';
import BackLink from 'components/common/link/BackLink';
import ClaimsList from '../claims-list/ClaimsList';
export default class Claims extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const { client: { token }, claimsRequest } = this.props;
        claimsRequest(token);
    }

    render() {
        return (
            <React.Fragment>
                <h1>Claims page</h1>
                <BackLink />
                <ClaimsList />
            </React.Fragment>
        );
    }
}
