import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { Dimmer, Header, Loader, Message } from 'semantic-ui-react';
import { checkAuthorization } from 'app/lib/checkAuth';
import AssetsList from './assets-list/AssetsList';

class AssetList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeList: [],
            filteredList: [],
            activeFilter: 'ALL'
        };
    }

    assetUpdateHandler = (id, value) => {
        const { assetUpdate } = this.props;
        assetUpdate(id, value);
    };

    pollingHandler = () => {
        const { poll: { polling }, pollStart, pollStop } = this.props;
        if (polling) pollStop(2000);
        else pollStart(2000);
    };

    componentWillUnmount() {
        const { poll: { polling }, pollStop } = this.props;
        if (polling) pollStop();
    }

    componentDidMount() {
        const { setClient, assetRequest, assets } = this.props;
        checkAuthorization(null, setClient);
        if (!assets.list.length) assetRequest();
    }

    render() {
        const { assets: { list, errors } } = this.props;
        return (
            <React.Fragment>
                <Header size="huge">Assets</Header>
                <Dimmer active={list && !list.length} inverted>
                    <Loader size="large">Loading</Loader>
                </Dimmer>
                {errors && errors.length ? (
                    <AssetListErrors errors={errors} />
                ) : (
                    <AssetsList
                        {...this.props}
                        assetUpdate={this.assetUpdateHandler}
                        pollingHandler={this.pollingHandler}
                    />
                )}
            </React.Fragment>
        );
    }
}

const AssetListErrors = errors => {
    return errors.errors.map((err, id) => (
        <Message negative key={id}>
            <p>
                {err.message}.
                {(err.status === 403 || err.status === 401) && (
                    <span>
                        Go to <Link to="/login">login</Link>
                    </span>
                )}
            </p>
        </Message>
    ));
};

export default AssetList;

AssetList.propTypes = {
    assetUpdate: PropTypes.func.isRequired,
    assets: PropTypes.object,
    poll: PropTypes.object,
    pollStart: PropTypes.func,
    pollStop: PropTypes.func,
    setClient: PropTypes.func.isRequired,
    assetRequest: PropTypes.func.isRequired
};
