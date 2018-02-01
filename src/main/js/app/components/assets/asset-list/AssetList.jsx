import React from 'react';
import { Link } from 'react-router-dom';
import { Card, Dimmer, Loader, Message } from 'semantic-ui-react';
import AssetCard from '../asset-card/AssetCard';
import Pagination from 'components/shared/pagination/Pagination';
import Fliter from '../assets-list-filter/AssetsListFilter';
import { filterList } from 'app/lib/helpers';
import { checkAuthorization } from 'app/lib/checkAuth';
import BackLink from 'components/shared/link/BackLink';
import { Header } from 'components/chat/chat/Chat';
import { ListContainer } from 'components/shared';

class AssetList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeList: [],
            filteredList: [],
            activeFilter: 'ALL'
        };
        this.onChangePage = this.onChangePage.bind(this);
        this.filterChangeHandler = this.filterChangeHandler.bind(this);
        this.assetUpdateHandler = this.assetUpdateHandler.bind(this);
        this.pollingHandler = this.pollingHandler.bind(this);
    }

    onChangePage(activeList) {
        this.setState({ activeList });
    }

    assetUpdateHandler(id, value) {
        const { assetUpdate } = this.props;
        assetUpdate(id, value);
        this.filterChangeHandler(null, { value: this.state.activeFilter });
    }

    filterChangeHandler(e, { value }) {
        this.setState(
            {
                activeFilter: value
            },
            () => {
                const filteredList = filterList(
                    this.state.activeFilter,
                    this.props.assets.list
                );
                this.setState({
                    filteredList
                });
            }
        );
    }

    pollingHandler() {
        const { poll: { polling }, pollStart, pollStop } = this.props;
        if (polling) pollStop(2000);
        else pollStart(2000);
    }

    componentDidMount() {
        checkAuthorization(null, this.props.setClient);
        this.props.assetRequest();
    }

    componentWillUnmount() {
        const { poll: { polling }, pollStop } = this.props;
        if (polling) pollStop();
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
                <Header>Claims List</Header>
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
                            polling={polling}
                            pollingHandler={this.pollingHandler}
                        />
                        <Card.Group itemsPerRow={4}>
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
                                <p className="filter__message">
                                    No items by this filter. Select other.
                                </p>
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
