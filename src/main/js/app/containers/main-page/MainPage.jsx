import React from 'react';
import { connect } from 'react-redux';
import { Container } from 'semantic-ui-react';
import actions from 'app/store/actions';
import AssetList from 'components/asset-list/AssetList';
import ListControls from 'components/list-controls/ListControls';
import { checkAuthorization } from 'app/lib/checkAuth';

/* eslint-disable react/prop-types */
class MainPage extends React.Component {
    constructor(props) {
        super(props);
        this.fetchAssets = this.fetchAssets.bind(this);
    }

    fetchAssets() {
        const { assetRequest, client: { token } } = this.props;
        return assetRequest(token);
    }

    componentDidMount() {
        checkAuthorization(null, this.props.setClient);
        this.fetchAssets();
    }

    render() {
        const {
            pollStart,
            pollStop,
            unsetClient,
            assets,
            assetUpdate,
            client,
            poll
        } = this.props;
        return (
            <Container>
                <h1>Assets List</h1>
                <ListControls
                    polling={poll.polling}
                    pollStart={pollStart}
                    pollStop={pollStop}
                    unsetClient={unsetClient}
                    client={client}
                    fetchAssets={this.fetchAssets}
                />
                <AssetList
                    assetsList={assets.list}
                    errors={assets.errors}
                    assetUpdate={assetUpdate}
                    updateStatus={assets.requesting}
                />
            </Container>
        );
    }
}

const mapStateToProps = ({ assets, client, poll }) => ({
    assets,
    client,
    poll
});

export default connect(mapStateToProps, {
    ...actions.assetsActions,
    ...actions.clientActions,
    ...actions.pollActions
})(MainPage);
