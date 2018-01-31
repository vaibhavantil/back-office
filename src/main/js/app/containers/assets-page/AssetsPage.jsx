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
        checkAuthorization(null, this.props.setClient);
        this.props.assetRequest();
    }

    render() {
        const {
            assets,
            assetUpdate,
            pollStart,
            pollStop,
            poll: { polling }
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
                />
            </Container>
        );
    }
}

const mapStateToProps = ({ assets, poll }) => ({
    assets,
    poll
});

export default connect(mapStateToProps, {
    ...actions.assetsActions,
    ...actions.clientActions,
    ...actions.pollActions
})(MainPage);
