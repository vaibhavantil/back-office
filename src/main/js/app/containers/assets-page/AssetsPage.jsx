import React from 'react';
import { connect } from 'react-redux';
import { Container } from 'semantic-ui-react';
import { Link } from 'react-router-dom';
import { Icon } from 'semantic-ui-react';
import actions from 'app/store/actions';
import AssetList from 'components/assets/asset-list/AssetList';
import { checkAuthorization } from 'app/lib/checkAuth';

class MainPage extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const { client: { token } } = this.props;
        checkAuthorization(null, this.props.setClient);
        this.props.assetRequest(token);
    }

    render() {
        const {
            assets,
            assetUpdate,
            pollStart,
            pollStop,
            poll: { polling },
            client: { token }
        } = this.props;
        const pollingEvents = { pollStart, pollStop, polling };
        return (
            <Container>
                <h1>Assets List</h1>
                <Link to="/dashboard">
                    <Icon name="arrow left" /> Back
                </Link>
                <AssetList
                    assetsList={assets.list}
                    errors={assets.errors}
                    assetUpdate={assetUpdate}
                    updateStatus={assets.requesting}
                    polling={pollingEvents}
                    token={token}
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
