import React from 'react';
import PropTypes from 'prop-types';
import { Header } from 'semantic-ui-react';
import PaginatorList from 'components/shared/paginator-list/PaginatorList';
import {
    TableRow,
    TableHeader
} from 'components/claims/claims-list/ClaimsList';

const ClaimsTab = ({ memberClaims }) =>
    memberClaims.length ? (
        <PaginatorList
            list={memberClaims}
            itemContent={item => <TableRow item={item} />}
            tableHeader={<TableHeader />}
        />
    ) : (
        <Header>Claims list is empty</Header>
    );

ClaimsTab.propTypes = {
    memberClaims: PropTypes.array
};

export default ClaimsTab;
