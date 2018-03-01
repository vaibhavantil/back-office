import React from 'react';
import PropTypes from 'prop-types';
import { Header } from 'semantic-ui-react';
import ClaimsList from './claims-list/ClaimsList';
import BackLink from 'components/shared/link/BackLink';
export default class Claims extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const { claimsRequest } = this.props;
        claimsRequest();
    }

    render() {
        return (
            <React.Fragment>
                <Header size="huge">Claims List</Header>
                <BackLink path="dashboard" />
                <ClaimsList claims={this.props.claims.list} />
            </React.Fragment>
        );
    }
}

Claims.propTypes = {
    claimsRequest: PropTypes.func.isRequired,
    claims: PropTypes.object.isRequired
};
