import React from 'react';
import PropTypes from 'prop-types';
import { Header } from 'semantic-ui-react';
import PaginatorList from 'components/shared/paginator-list/PaginatorList';
import {
    TableRow,
    TableHeader
} from 'components/claims/claims-list/ClaimsList';

const ClaimsTab = ({ userClaims }) =>
    userClaims.length ? (
        <PaginatorList
            list={userClaims}
            itemContent={item => <TableRow item={item} />}
            tableHeader={<TableHeader />}
        />
    ) : (
        <Header>Claims list is empty</Header>
    );

ClaimsTab.propTypes = {
    userClaims: PropTypes.array
};

export default ClaimsTab;
