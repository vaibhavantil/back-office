import React from 'react';
import { connect } from 'react-redux';
import { Container } from 'semantic-ui-react';
import actions from 'app/store/actions';
import AssetList from 'components/assets/asset-list/AssetList';

const AssetsPage = props => (
    <Container>
        <AssetList {...props} />
    </Container>
);
const mapStateToProps = ({ assets, poll }) => ({
    assets,
    poll
});

export default connect(mapStateToProps, {
    ...actions.assetsActions,
    ...actions.clientActions,
    ...actions.pollActions
})(AssetsPage);
