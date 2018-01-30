import React from 'react';
import BackLink from '../link/BackLink';

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
            </React.Fragment>
        );
    }
}
