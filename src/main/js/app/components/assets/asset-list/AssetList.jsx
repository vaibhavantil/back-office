import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { Button, Card, Dimmer, Header, Loader, Message } from 'semantic-ui-react';
import AssetCard from '../asset-card/AssetCard';
import Pagination from 'components/shared/pagination/Pagination';
import Fliter from 'components/shared/filter/Filter';
import { checkAuthorization } from 'app/lib/checkAuth';
import BackLink from 'components/shared/link/BackLink';
import { ListContainer } from 'components/shared';
import { assetStates } from 'app/lib/selectOptions';

const FilterMessage = styled.p`
    display: inline-block;
    width: 100%;
    text-align: center;
    font-size: 1.5rem;
`;
class AssetList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeList: [],
            filteredList: [],
            activeFilter: 'ALL'
        };
    }

    onChangePage = activeList => {
        this.setState({ activeList });
    };

    assetUpdateHandler = (id, value) => {
        const { assetUpdate } = this.props;
        assetUpdate(id, value);
        this.setState({ activeFilter: value });
    };

    filterChangeHandler = (activeFilter, filteredList) => {
        this.setState({ activeFilter, filteredList });
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
        const {
            assets: { list, errors, requesting },
            poll: { polling }
        } = this.props;
        const { activeList, filteredList, activeFilter } = this.state;
        const items = activeFilter === 'ALL' ? list : filteredList;
        return (
            <React.Fragment>
                <Header size="huge">Assets</Header>
                <BackLink />
                <Dimmer active={list && !list.length} inverted>
                    <Loader size="large">Loading</Loader>
                </Dimmer>
                {errors && errors.length ? (
                    <AssetListErrors errors={errors} />
                ) : (
                    <ListContainer autoWidth={true}>
                        <Fliter
                            list={list}
                            activeFilter={this.state.activeFilter}
                            filterChange={this.filterChangeHandler}
                            options={assetStates}
                            fieldName="state"
                        />
                        <Button onClick={this.pollingHandler} size="mini">
                            Poll {polling ? 'stop' : 'start'}
                        </Button>
                        <Card.Group
                            itemsPerRow={4}
                            style={{ margin: '30px 0' }}
                        >
                            {list && !!activeList.length ? (
                                activeList.map(asset => (
                                    <AssetCard
                                        key={asset.id}
                                        asset={asset}
                                        assetUpdate={this.assetUpdateHandler}
                                        updateStatus={requesting}
                                    />
                                ))
                            ) : (
                                <FilterMessage>
                                    No items by this filter. Select other.
                                </FilterMessage>
                            )}
                        </Card.Group>
                        <Pagination
                            items={items}
                            onChangePage={this.onChangePage}
                        />
                    </ListContainer>
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
