import React from 'react';
import PropTypes from 'prop-types';
import ClaimsList from '../claims-list/ClaimsList';
import BackLink from 'components/shared/link/BackLink';
import { Header } from 'components/chat/chat/Chat';
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
                <Header>Claims List</Header>
                <BackLink />
                <ClaimsList claims={this.props.claims.list} />
            </React.Fragment>
        );
    }
}

Claims.propTypes = {
    claimsRequest: PropTypes.func.isRequired,
    claims: PropTypes.object.isRequired
};
