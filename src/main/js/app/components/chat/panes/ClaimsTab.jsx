import React from 'react';
import PropTypes from 'prop-types';
import { Header } from 'semantic-ui-react';
import PaginatorList from 'components/shared/paginator-list/PaginatorList';
import {
    TableRow,
    TableHeader
} from 'components/claims/claims-list/ClaimsList';
export default class ClaimsTab extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const { claimsByUser, messages: { user } } = this.props;
        if (user) claimsByUser(user.hid);
    }

    render() {
        const claims = this.props.userClaims;
        return claims.length ? (
            <PaginatorList
                list={this.props.userClaims}
                itemContent={item => <TableRow item={item} />}
                tableHeader={<TableHeader />}
            />
        ) : (
            <Header>Claims list is empty</Header>
        );
    }
}

ClaimsTab.propTypes = {
    userClaims: PropTypes.array,
    claimsByUser: PropTypes.func.isRequired,
    messages: PropTypes.object.isRequired
};
